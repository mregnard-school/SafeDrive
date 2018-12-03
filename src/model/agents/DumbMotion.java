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
    Intent myIntent = getMyIntent();
    removeIntentFromList(myIntent);

    List<Intent> conflictIntents = intents
        .stream()
        .filter(intent -> intent.equals(myIntent))
        .collect(Collectors.toList());

    conflictCount = conflictIntents.size();

    if (conflictIntents.isEmpty()) {    //No conflict is found
      agent.setNextPos(myIntent.getTo());
      agent.log("Moved without conflict");
      return myIntent;
    }

    agent.log("I have  " + conflictIntents.size() + " conflict !!");

    for (Intent intent : conflictIntents) {
      return resolveConflict(intent, myIntent);
    }
    return idle();
  }

  public Intent resolveConflict(Intent myIntent, Intent conflictIntent) {
    if (availablePoints
        .stream()
        .anyMatch(point -> !point.equals(myIntent.getTo()))) {
      sendNoOption(conflictIntent.getAgent());

      //@TODO need to flip a coin
      agent.setNextPos(myIntent.getTo());
      return myIntent;
    }
    Point newClosest = availablePoints
        .stream()
        .filter(point -> !point.equals(myIntent.getTo()))
        .min(Comparator
            .comparing(point ->
                getEuclidianDistance(point, agent.getDestination()))
        ).orElseThrow(IllegalStateException::new);

    //@TODO if no available points from other guy, take the closest one
    //@TODO send closest cost
    //@TODO wait for answer
    double myCost = availablePoints
        .stream()
        .filter(point -> !point.equals(myIntent.getTo()))
        .mapToDouble(point ->
            getEuclidianDistance(point, agent.getDestination()))
        .average().orElseThrow(
            IllegalStateException::new);   //We have check if available points is empty so it should return a double
    sendCost(myCost, myIntent);

    try {
      agent.getSem().acquire(conflictCount);
      List<Double> otherCosts = agent.getCosts();

      double maxOfOthers = otherCosts.stream().max(Double::compareTo).get();
      if (myCost > maxOfOthers) {
        //It's over Anakin, I have the high cost
        agent.setNextPos(myIntent.getTo());
        return myIntent;
      }

      if (myCost < maxOfOthers) {
        agent.setNextPos(newClosest);
        return createIntent(newClosest);
      }

      agent.log("We have the same priority");
      return idle();

      //@TODO flip coin
    } catch (InterruptedException e) {
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

  private void removeIntentFromList(Intent myIntent) {
    intents.removeIf(intent -> intent.getAgent().getId() == myIntent.getAgent().getId());
  }

  private void sendCost(double myCost, Intent myIntent) {
    myIntent.getAgent().invoke(new Information(agent, myCost, myIntent));
  }

  private void sendNoOption(Vehicle vehicle) {
    //@TODO send message
  }
}
