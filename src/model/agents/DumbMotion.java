package model.agents;

import static util.PointOperations.getEuclidianDistance;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import model.communication.message.Information;
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
    addExtraPoints();
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

  private Intent resolveWithoutOption() throws InterruptedException {
    awaitResponses();
    if (/*all others have other choices */ true) {
      return myIntent;
    }

    // @todo [irindul-2018-12-03] : Flip a coin

    return idle();
  }

  private void awaitResponses() throws InterruptedException {
    agent.getSem().acquire(conflictCount);
  }

  private Intent resolveWithOptions(double myCost) throws InterruptedException {
    Point newClosest = getSecondClosest();

    awaitResponses();

    List<Double> otherCosts = agent.getCosts();

    double maxOfOthers = otherCosts.stream().max(Double::compareTo).get();
    if (myCost > maxOfOthers) {
      //It's over Anakin, I have the high cost
      agent.setNextPos(myIntent.getTo());
      return myIntent;
    }

    if (myCost < maxOfOthers) {
      // I hate you
      agent.setNextPos(newClosest);
      return createIntent(newClosest);
    }

    agent.log("We have the same priority");
    // @todo [irindul-2018-12-03] : flip a coin
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
    myIntent.getAgent().invoke(new Information(agent, myCost, myIntent, intent.getAgent()));
  }

  private void sendNoOption(Vehicle vehicle) {
    //@TODO send message
  }
}
