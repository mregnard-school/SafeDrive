package util;

import java.awt.Point;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import model.agents.Agent;

public class Intent {

  private AbstractMap.Entry<Point, Point> entry;
  private Agent agent;

  public Intent(Point from, Point to, Agent agent) {
    entry = new SimpleEntry<>(from, to);
    this.agent = agent;
  }

  public Point getFrom() {
    return entry.getKey();
  }

  public Point getTo() {
    return entry.getValue();
  }

  public int getPlateAgent() {
    return agent.getId();
  }

  public Agent getAgent() {
    return agent;
  }
}