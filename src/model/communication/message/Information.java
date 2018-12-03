package model.communication.message;

import java.util.Collections;
import java.util.List;
import model.agents.Handler;
import model.agents.Vehicle;
import model.communication.Invoker;
import model.communication.Receiver;
import util.Intent;

public class Information implements Command {

  private Invoker author;
  private double averageCost;
  private Receiver receiver;
  private Intent intent;

  public Information(Invoker author, double averageCost, Intent intent) {
    this.author = author;
    this.averageCost = averageCost;
    this.intent = intent;
  }

  @Override
  public void execute() {
    Vehicle vehicle = (Vehicle) author;
    Vehicle agent = Handler.getAgent(vehicle.getId());
    agent.getSem().release(1);
    agent.addCost(averageCost);
  }

  @Override
  public List<Receiver> getReceivers() {
    return Collections.singletonList(receiver);
  }
}
