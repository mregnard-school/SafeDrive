package model.agents;

import static util.PointOperations.getEuclidianDistance;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import model.environment.Road;
import util.Intent;
import util.IntentList;

public class DumbMotion implements MotionStrategy {

  private Vehicle agent;
  private List<Point> availablePoints;

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
    return createIntent(agent.getCurrentPos());
  }

  private Intent createIntent(Point to) {
    return new Intent(agent.getCurrentPos(), to, agent);
  }

  @Override
  public void run(IntentList intentList) {
    List<Intent> intents = intentList.stream().collect(Collectors.toList());

    Optional<Intent> opt = intents
        .stream()
        .filter(intent -> intent
            .getAgent()
            .equals(agent))
        .findAny();
    if (opt.isPresent()) {
      System.out.println("No intent for this agent: " + agent);
      return;
    }
    Intent myIntent = opt.get();
    intents.remove(myIntent);
    if (intents.stream().anyMatch(intent -> intent.equals(myIntent))) {
      agent.setNextPos(myIntent.getTo());
      return; //@TODO get to next position
    }
    if (availablePoints.isEmpty()) {
      //@TODO warn other guy
      agent.setNextPos(myIntent.getTo());
      return;
    }
    Optional<Point> optionalPoint = availablePoints      //@TODO can be interesting to sort the list of availables points
        .stream()
        .filter(point -> !point.equals(myIntent.getTo()))
        .min(Comparator
            .comparing(point ->
                getEuclidianDistance(point, agent.getDestination()))
        );
    if (!optionalPoint.isPresent()) {
      System.out.println("Closest is not present why ?");
      return;
    }
    //@TODO send closest
    //@TODO receive closest
    Point newClosest = optionalPoint.get();
    Intent otherIntent = null;
    Vehicle vehicle = (Vehicle) otherIntent.getAgent();
    if (getEuclidianDistance(otherIntent.getTo(), vehicle.getDestination()) < getEuclidianDistance(newClosest, agent.getDestination())) {   // I have the max cost so I should go first
      agent.setNextPos(myIntent.getTo());
      return;
    }
    idle();

  }
}
