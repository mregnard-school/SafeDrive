package model.communication.message;

import java.awt.Point;
import java.util.Collections;
import java.util.List;
import model.communication.Receiver;

public class Information implements Command {

  private double averageCost;
  private Point concerningPoint;
  private Receiver receiver;

  public Information(double averageCost, Point concerningPoint) {
    this.averageCost = averageCost;
    this.concerningPoint = concerningPoint;
  }

  @Override
  public void execute() {

  }

  @Override
  public List<Receiver> getReceivers() {
    return Collections.singletonList(receiver);
  }
}
