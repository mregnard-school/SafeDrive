package util;

import java.awt.Point;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import model.agents.Vehicle;

public class Intent {

  private AbstractMap.Entry<Point, Point> entry;
  private Vehicle agent;

  public Intent(Point from, Point to, Vehicle agent) {
    entry = new SimpleEntry<>(from, to);
    this.agent = agent;
  }

  public Point getTo() {
    return entry.getValue();
  }

  public int getPlateAgent() {
    return agent.getId();
  }

  public Vehicle getAgent() {
    return agent;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Intent)) {
      return false;
    }
    Intent intent = (Intent) obj;
    return intent.getTo().equals(getTo());
  }
}