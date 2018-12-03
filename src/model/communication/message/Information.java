package model.communication.message;

import java.util.Collections;
import java.util.List;
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
    System.out.println("Token should be released");
    vehicle.getSem().release(1);
  }

  @Override
  public List<Receiver> getReceivers() {
    return Collections.singletonList(receiver);
  }
}
