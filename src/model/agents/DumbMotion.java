package model.agents;

import static util.PointOperations.getEuclidianDistance;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import model.environment.Road;
import util.Intent;
import util.IntentList;

public class DumbMotion implements MotionStrategy {

  private Vehicle agent;
  private List<Point> availablePoints;
  private List<Intent> intents;
  private Map<Point, Semaphore> locks;

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
    return createIntent(agent.getCurrentPos());
  }

  private Intent createIntent(Point to) {
    return new Intent(agent.getCurrentPos(), to, agent);
  }

  public void setIntents(IntentList intents) {
    this.intents = intents.stream().collect(Collectors.toList());
  }

  public void setLocks(Map<Point, Semaphore> locks) {
    this.locks = locks;
  }

  @Override
  public Intent call() {
    Intent myIntent = intents
        .stream()
        .filter(intent -> intent
            .getAgent()
            .equals(agent))
        .findAny()
        .orElseThrow(IllegalStateException::new);
    intents.remove(myIntent);
    if (intents.stream().noneMatch(intent -> intent.equals(myIntent))) {
      agent.setNextPos(myIntent.getTo());
      return myIntent;
    }

    //Send intent to conflicted
    Semaphore sem = locks.get(myIntent.getTo());
    this.agent.setSem(sem);
    try {
      sem.acquire();
      if (availablePoints.isEmpty()) {
        //@TODO warn other guy
        //@TODO need to flip a coin
        agent.setNextPos(myIntent.getTo());
        return myIntent;
      }
      Point newClosest = availablePoints      //@TODO can be interesting to sort the list of availables points
          .stream()
          .filter(point -> !point.equals(myIntent.getTo()))
          .min(Comparator
              .comparing(point ->
                  getEuclidianDistance(point, agent.getDestination()))
          ).orElseThrow(IllegalStateException::new);
      //@TODO if no available points from other guy, take the closest one

      //@TODO send closest cost
      //@TODO wait for answer
      double otherCost = 1625676;
      double myCost = getEuclidianDistance(newClosest, agent.getDestination());
      if (otherCost < myCost) {   // I have the max cost so I should go first
        agent.setNextPos(myIntent.getTo());
        return myIntent;
      }

      if (otherCost > myCost) {
        agent.setNextPos(newClosest);
        return createIntent(newClosest);
      }

      return idle();
      // We are in the case we have the same cost
      //@TODO flip coin
    } catch (InterruptedException e) {
      return idle();
    } finally {
      sem.release();
    }


  }
}
