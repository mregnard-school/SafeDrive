package model.communication.message;

import java.awt.Point;
import java.util.List;
import model.agents.Vehicle;
import model.communication.Invoker;
import model.communication.Receiver;

public class Information  extends Message{

  private int speed;
  private Point destination;
  private Point nextPos;

  public Information(Invoker author, List<Receiver> receivers, Priority priority) {
    super(author, receivers, priority);
    Vehicle vehicle = (Vehicle) author;
    speed = vehicle.getSpeed();
    destination = vehicle.getDestination();
    nextPos = vehicle.getNextPos();
  }

  @Override
  public void execute() {
  }

  @Override
  public String toString() {
    return "Information: "+ speed + ", destination: " + destination + ", nextPos: " + nextPos;
  }
}
