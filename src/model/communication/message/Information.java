package model.communication.message;

import model.agents.Handler;
import model.agents.Vehicle;
import model.communication.Invoker;
import model.communication.Receiver;

public class Information implements Command {

  private Invoker author;
  private double averageCost;
  private Receiver receiver;

  public Information(Invoker author, double averageCost, Receiver receiver) {
    this.author = author;
    this.averageCost = averageCost;
    this.receiver = receiver;
  }

  @Override
  public void execute() {
    Vehicle vehicle = (Vehicle) receiver;
    Vehicle agent = Handler.getAgent(vehicle.getId());
    agent.getSem().release(1);
    agent.addCost(averageCost);
  }

  @Override
  public Receiver getReceiver() {
    return receiver;
  }

  @Override
  public String toString() {
    Vehicle vehicle = (Vehicle) author;
    return "{Information: from"+ vehicle.getId()+", cost: " + averageCost + "}";
  }
}
