package model.communication.message;

import java.awt.Point;
import java.util.List;
import model.agents.Vehicle;
import model.communication.Invoker;
import model.communication.Receiver;

public class RequestMove extends Message {
  private Point goal;

  public RequestMove(Invoker author, List<Receiver> receivers, Priority priority, Point point) {
    super(author, receivers, priority);
    Vehicle vehicle = (Vehicle) author;
    goal = point;
  }

  public Point getGoal() {
    return goal;
  }

  @Override
  public String toString() {
    return "Move is requested : goal= " + goal + " " + super.toString();
  }
}
