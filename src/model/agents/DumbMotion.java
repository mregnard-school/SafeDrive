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
import util.Logger;

public class DumbMotion implements MotionStrategy {

  private Queue<Command> commands;
  private Vehicle agent;
  private List<Point> availablePoints;

  @Override
  public void run(Vehicle agent, Land land) {
    this.agent = agent;
    commands = agent.getCommands();

    processAvailablePoints();

    List<Point> requests = analyzeMessage();
    removeRequestedPoints(requests);

    Optional<Point> closest = findClosestPoint();

    if (!closest.isPresent()) {
      idle();
      return;
    }

    Optional<Vehicle> vehicle = agent.getLand().getVehicleAt(closest.get());

    if (!vehicle.isPresent()) {
      agent.setNextPos(closest.get());
      return;
    }

    // @todo [irindul-2018-12-02] : test
    sendRequest(vehicle.get());

    //We're block so me wait
    idle();
  }

  private void processAvailablePoints() {
    availablePoints = new ArrayList<>();
    processInRangeMovement();
    if (agent.getId() == 2) {
      agent.log("====Start===");
      agent.log("pos = " + pointToString(agent.getCurrentPos()));
      availablePoints.forEach(point -> {
        Logger.log(pointToString(point));
      });
      Logger.log("adding extra");
    }


    addExtraPoints();
    if (agent.getId() == 2) {
      availablePoints.forEach(point -> {
        Logger.log(pointToString(point));
      });
      agent.log("====End===");
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
      System.out.println("dans le foreach points");
      System.out.println(points);
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

  private void sendRequest(Vehicle block) {
    List<Receiver> receivers = Collections.singletonList(block);

    RequestMove requestMove = new RequestMove(
        agent,
        receivers,
        Priority.MEDIUM
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
}
