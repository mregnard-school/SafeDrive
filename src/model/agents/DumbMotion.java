package model.agents;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import model.communication.Command;
import model.environment.Direction;
import model.environment.Land;

public class DumbMotion implements MotionStrategy {


  private Point destination;
  private Queue<Command> commands;

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
      //Yeah we got the closest point, we can move
    } else {
      //Uh oh, no points available :'(
      //We stop the car (speed = 0)
    }
  }

  private List<Point> getAvailablePoints() {
    return new ArrayList<>();
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
