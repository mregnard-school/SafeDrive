package model.agents;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;
import model.communication.message.Command;
import model.communication.message.Message;
import model.environment.Direction;
import model.environment.Land;
import model.environment.Road;

public class DumbMotion implements MotionStrategy {


  private Point destination;
  private Queue<Command> commands;
  private Vehicle agent;

  @Override
  public void run(Vehicle agent, Land land) {

    //Set environement + Agent variable
    this.destination = agent.getDestination();
    commands = agent.getCommands();

    List<Direction> answers = analyzeMessage();
    if (!answers.isEmpty()) {
      //@todo Do something with this directions
    }

    //@todo send request

    List<Point> availablePoints = getAvailablePoints();
    Optional<Point> closest = findClosestPoint(availablePoints);
    if (closest.isPresent()) {
      agent.setNextPos(closest.get());
      //Yeah we got the closest point, we can move
    } else {
      agent.setNextPos(agent.getCurrentPos());    //@TODO tmp

      //Uh oh, no points available :'(
      //We stop the car (speed = 0)
    }
  }

  private List<Point> getAvailablePoints() {
    return agent.getLand().getRoadsForPoint(agent.getCurrentPos()).map(
        Road::getAxis).map(direction -> direction.next(agent.getCurrentPos())).collect(Collectors.toList());
  }

  private Optional<Point> findClosestPoint(List<Point> availablePoints) {
    if (availablePoints.isEmpty()) {
      return Optional.empty();
    }

    Point closest = Collections.min(
        availablePoints, Comparator.comparing(point -> getManhatanDistance(point, destination))
    );

    return Optional.of(closest);
  }

  private List<Direction> analyzeMessage() {
    List<Direction> answers = new ArrayList<>();
    List<Message> messages =  commands.stream().map(command -> {
      return (Message) command;
    }).collect(Collectors.toList());
    if (commands.isEmpty()) {
      return answers;
    } else {
      return answers;
    }
  }


  private int getManhatanDistance(Point from, Point to) {
    return Math.abs(to.x - from.x) + Math.abs(to.y - to.x);
  }

}
