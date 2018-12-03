package model.agents;

import static util.PointOperations.getEuclidianDistance;
import static util.PointOperations.pointToString;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;
import model.communication.Receiver;
import model.communication.message.Command;
import model.communication.message.Priority;
import model.communication.message.RequestMove;
import model.environment.Land;
import model.environment.Road;
import util.Intent;

public class DumbMotion implements MotionStrategy {

  private Queue<Command> commands;
  private Vehicle agent;
  private List<Point> availablePoints;

  @Override
  public Intent getIntent(Vehicle agent, Land land) {
    this.agent = agent;
    commands = agent.getCommands();

    processAvailablePoints();

    List<Point> requests = analyzeMessage();
    removeRequestedPoints(requests);

    Optional<Point> closest = findClosestPoint();

    if (!closest.isPresent()) {
      System.out.println("ohé");
      idle();
      return null;
    }

    Optional<Vehicle> vehicle = agent.getLand().getVehicleAt(closest.get());

    if (!vehicle.isPresent()) {
      agent.setNextPos(closest.get());
      return null;
    }

    //We're block so me wait
    System.out.println("bloké");
    idle();
    sendRequest(vehicle.get(), closest.get());
    return  null; // @todo [irindul-2018-12-02] : change
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

  private List<Point> analyzeMessage() {
    return commands.stream()
        .map(command -> (RequestMove) command)
        .map(RequestMove::getGoal)
        .collect(Collectors.toList());
  }

  private void removeRequestedPoints(List<Point> answers) {
    answers.forEach(point -> {
      if (!availablePoints.isEmpty()) {
        availablePoints.remove(point);
      }
    });
    availablePoints = availablePoints.stream().filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  private void sendRequest(Vehicle block, Point point) {
    List<Receiver> receivers = Collections.singletonList(block);

    RequestMove requestMove = new RequestMove(
        agent,
        receivers,
        Priority.MEDIUM,
        point
    );
    agent.invoke(requestMove);
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

  private void idle() {
    agent.setNextPos(agent.getCurrentPos());
  }

  @Override
  public void run() {
    Intent myIntent = null;
    List<Intent> othersIntent = new ArrayList<>();
    if (othersIntent.stream().anyMatch(intent -> intent.equals(myIntent))) {
      agent.setNextPos(myIntent.getTo());
      return; //@TODO get to next position
    }
    if (availablePoints.isEmpty()) {
      //@TODO warn other guy
      agent.setNextPos(myIntent.getTo());
    }

  }
}
