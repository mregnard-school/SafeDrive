package model.agents;

import java.awt.Point;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;
import model.communication.message.Command;
import model.communication.message.RequestMove;
import model.environment.Land;
import model.environment.Road;

public class DumbMotion implements MotionStrategy {

  private Queue<Command> commands;
  private Vehicle agent;

  @Override
  public void run(Vehicle agent, Land land) {

    //Set environement + Agent variable
    this.agent = agent;
    commands = agent.getCommands();

    //@todo send request

    List<Point> availablePoints = getAvailablePoints();

    //@TODO REMOVE DEBUG    THIS FUCKING CAR NEED TO TURN AROUND
    if (agent.getId() == 19) {
      //System.out.println("Fucking car");
      //System.out.println(availablePoints);
    }

    Optional<List<Point>> answers = analyzeMessage();
    if (answers.isPresent()) {      //if there is an answer, we remove the points available
      availablePoints = removeCommonPoints(availablePoints, answers.get());
    }
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

  private List<Point> removeCommonPoints(List<Point> availablePoints,
      List<Point> answers) {

    List<Point> tmp = availablePoints;
//    List<Point> newPoints = availablePoints
//        .stream()
//        .filter(point -> answers
//            .stream()
//            .noneMatch(answer -> answer.equals(point)))
//        .collect(Collectors.toList());

    answers.forEach(point -> {
      if (!tmp.isEmpty()) {
        tmp.remove(point);
      }
    });
    return tmp.stream().filter(Objects::nonNull).collect(Collectors.toList());
  }

  private List<Point> getAvailablePoints() {
    List<Point> points = agent
        .getLand()
        .getRoadsForPoint(agent.getCurrentPos())
        .map(Road::getAxis)
        .map(direction -> direction.next(agent.getCurrentPos()))
        .filter(point -> point.y > -1 && point.x > 0 && point.y < agent.getLand().getHeight()
            && point.x < agent.getLand().getWidth())
        .collect(Collectors.toList());
    //Now we get oghter points around cause sometime u have to turn around
    List<Road> roads = agent
        .getLand()
        .getRoadsForPoint(agent.getCurrentPos())
        .collect(Collectors.toList());

    //We take the first one because there should @TODO here to pass someonex
    agent.getLand().roadExit(roads.get(0), agent.getCurrentPos());

    return points;
  }

  private Optional<Point> findClosestPoint(List<Point> availablePoints) {
    if (availablePoints.isEmpty()) {
      return Optional.empty();
    }

    Point closest = Collections.min(
        availablePoints,
        Comparator.comparing(point -> getManhatanDistance(point, agent.getDestination()))
    );

    return Optional.of(closest);
  }

  private Optional<List<Point>> analyzeMessage() {
//    List<Message> messages =  commands.stream().map(command -> (Message) command).collect(Collectors.toList());
//    System.out.println(commands);
    if (commands.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(commands.stream().map(command -> {
      RequestMove message = (RequestMove) command;
      return message.getGoal();
    }).collect(Collectors.toList()));
//    messages
//    List<Direction> answers = new ArrayList<>();
  }


  private int getManhatanDistance(Point from, Point to) {
    return Math.abs(to.x - from.x) + Math.abs(to.y - to.x);
  }

}
