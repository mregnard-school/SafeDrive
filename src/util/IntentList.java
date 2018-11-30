package util;

import java.awt.Point;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import model.agents.Agent;

public class IntentList {

  private class Intent {
    private AbstractMap.Entry<Point, Point> entry;

    public Intent(Point from, Point to) {
      entry = new SimpleEntry<>(from, to);
    }

    public Point getFrom() {
      return entry.getKey();
    }

    public Point getTo() {
      return entry.getValue();
    }
  }

  private Map<Agent, Intent> intents;

  public IntentList() {
    intents = new HashMap<>();
  }

  public void addIntent(Agent agent, Point from, Point to) {
    Intent intent = new Intent(from, to);
    intents.put(agent, intent);
  }
}
