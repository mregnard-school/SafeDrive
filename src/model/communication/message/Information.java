package model.communication.message;

import java.util.Collections;
import java.util.List;
import model.communication.Receiver;
import util.Intent;

public class Information implements Command {

  private double averageCost;
  private Receiver receiver;
  private Intent intent;

  public Information(double averageCost, Intent intent) {
    this.averageCost = averageCost;
  }

  @Override
  public void execute() {

  }

  @Override
  public List<Receiver> getReceivers() {
    return Collections.singletonList(receiver);
  }
}
