package model.agents;

import static util.PointOperations.getEuclidianDistance;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import model.communication.message.Information;
import model.communication.message.NoOption;
import model.environment.Road;
import util.Intent;
import util.IntentList;

public class DumbMotion implements MotionStrategy {

  private Vehicle agent;
  private List<Point> availablePoints;
  private List<Intent> intents;
  private int conflictCount;
  private Intent myIntent;

  @Override
  public Intent getIntent(Vehicle agent) {
    this.agent = agent;
    processAvailablePoints();
    Optional<Point> closest = findClosestPoint();
    return closest.map(this::createIntent).orElseGet(this::idle);
  }

  private void processAvailablePoints() {
    availablePoints = new ArrayList<>();
    processInRangeMovement();
    if (availablePoints.isEmpty()) {
      addExtraPoints();
    }
  }

  private void processInRangeMovement() {
    agent.getLand()
        .getRoadsForPoint(agent.getCurrentPos())
        .map(Road::getAxis)
        .map(direction -> direction.next(agent.getCurrentPos()))
        .filter(point -> agent.getLand().isInLand(point))
        .forEach(point -> availablePoints.add(point));
  }

  private void addExtraPoints() {
    List<Road> roads = agent
        .getLand()
        .getRoadsForPoint(agent.getCurrentPos())
        .collect(Collectors.toList());
    if (roads.isEmpty()) {
      return;
    }
    roads.forEach(road -> {
      List<Point> points = agent.getLand().roadExit(road, agent.getCurrentPos());
      availablePoints.addAll(points);
    });
  }

  private Optional<Point> findClosestPoint() {
    if (availablePoints.isEmpty()) {
      return Optional.empty();
    }

    Point closest = Collections.min(
        availablePoints,
        Comparator.comparing(point -> getEuclidianDistance(point, agent.getDestination()))
    );

    return Optional.of(closest);
  }

  private Intent idle() {
    agent.setNextPos(
        agent.getCurrentPos());    // need to be set, otherwise we can have null pointer exception
    return createIntent(agent.getCurrentPos());
  }

  private Intent createIntent(Point to) {
    return new Intent(agent.getCurrentPos(), to, agent);
  }

  public void setIntents(IntentList intents) {
    this.intents = intents.stream().collect(Collectors.toList());
  }

  @Override
  public Intent call() {
    this.myIntent = getMyIntent();
    removeIntentFromList();

    List<Intent> conflictedIntents = intents
        .stream()
        .filter(intent -> intent.equals(myIntent))
        .collect(Collectors.toList());

    conflictCount = conflictedIntents.size();

    if (conflictedIntents.isEmpty()) {
      agent.setNextPos(myIntent.getTo());
      agent.log("Moved without conflict");
      return myIntent;
    }

    agent.log("I have  " + conflictedIntents.size() + " conflict !!");
    try {
      return resolveConflicts(conflictedIntents);
    } catch (InterruptedException e) {
      agent.log("Interrupted!!");
      return idle();
    }
  }

  private Intent getMyIntent() {
    for (Intent intent : intents) {
      if (intent.getAgent().getId() == agent.getId()) {
        return intent;
      }
    }

    throw new IllegalStateException("No intent declared for agent " + agent.getId());
  }

  private void removeIntentFromList() {
    intents.removeIf(intent -> intent.getAgent().getId() == myIntent.getAgent().getId());
  }

  public Intent resolveConflicts(List<Intent> conflictedIntents) throws InterruptedException {
    if (hasNoOtherOptions(myIntent)) {
      conflictedIntents.forEach(intent -> sendNoOption(intent.getAgent()));
      return resolveWithoutOption();
    }

    double myCost = calculateMyAverageLoss();
    conflictedIntents.forEach(intent -> sendCost(myCost, intent));
    return resolveWithOptions(myCost);
  }

  private boolean hasNoOtherOptions(Intent intent) {
    return availablePoints
        .stream()
        .allMatch(point -> point.equals(intent.getTo()));
  }

  private void sendNoOption(Vehicle vehicle) {
    agent.invoke(new NoOption(vehicle));
  }

  private Intent resolveWithoutOption() throws InterruptedException {
    awaitResponses();
    if (!agent.hasNoOption()) {
      agent.setNextPos(myIntent.getTo());
      return myIntent;
    }

    if (flipACoin()) {
      agent.log("Won the coin flip");
      agent.setNextPos(myIntent.getTo());
      return myIntent;
    }

    agent.log("Lost the coin flip");
    agent.setNextPos(myIntent.getTo());
    return idle();
  }

  private boolean flipACoin() {
    // @todo [irindul-2018-12-03] : test but whould be working
    return Handler.flipACoinFor(myIntent.getTo());
  }

  private void awaitResponses() throws InterruptedException {
    agent.log("Trying to acquire " + conflictCount);
    if (!agent.getSem().tryAcquire(conflictCount, 1000, TimeUnit.MILLISECONDS)) {
      // @todo [irindul-2018-12-03] : Sometimes (with 3 agent itération 13) the lock is never acquired
      // @todo [irindul-2018-12-03] : After investigation, the message is never received... I don't know y but afterward all message are never received
      // @todo [irindul-2018-12-03] : Maybe check with UDP or maybe a bug somewhere else but it's 4:29 am and I don't give a shit right now
      throw new InterruptedException("Timeout exceeeded");
    }
    agent.log("Acquired ");
  }

  private Intent resolveWithOptions(double myCost) throws InterruptedException {
    Point newClosest = getSecondClosest();

    awaitResponses();
    List<Double> otherCosts = agent.getCosts();

    double maxOfOthers = otherCosts.stream().max(Double::compareTo).get();
    if (myCost > maxOfOthers) {
      agent.log("Fuck it I go");
      //It's over Anakin, I have the high cost
      agent.setNextPos(myIntent.getTo());
      return myIntent;
    }

    if (myCost < maxOfOthers) {
      agent.log("Alright I let you go");
      // You underestimate my cost
      agent.setNextPos(newClosest);
      return createIntent(newClosest);
    }

   if(flipACoin()) {
     agent.log("Won the coin flip");
     agent.setNextPos(myIntent.getTo());
     return myIntent;
   }

    agent.log("Lost the coin flip");
    return idle();
  }

  private Point getSecondClosest() {
    return availablePoints
        .stream()
        .filter(point -> !point.equals(myIntent.getTo()))
        .min(Comparator
            .comparing(point ->
                getEuclidianDistance(point, agent.getDestination()))
        ).orElseThrow(IllegalStateException::new);
  }

  private double calculateMyAverageLoss() {
    return availablePoints
        .stream()
        .filter(point -> !point.equals(myIntent.getTo()))
        .mapToDouble(point ->
            getEuclidianDistance(point, agent.getDestination()))
        .average()
        .orElseThrow(IllegalStateException::new);
  }

  private void sendCost(double myCost, Intent intent) {
    agent.log("Sending cost " + myCost + " to" + intent.getAgent().getId());
    agent.invoke(new Information(agent, myCost, intent.getAgent()));
  }
}
