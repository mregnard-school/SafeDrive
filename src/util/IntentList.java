package util;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import model.agents.Agent;

public class IntentList {

  private Map<Agent, Intent> intents;

  public IntentList() {
    intents = new HashMap<>();
  }

  public void addIntent(Agent agent, Point from, Point to) {
    Intent intent = new Intent(from, to, agent);
    intents.put(agent, intent);
  }

  public void addIntent(Intent intent) {
    intents.put(intent.getAgent(), intent);
  }

  public Stream<Intent> stream() {
    return intents.entrySet().stream().map(Entry::getValue);
  }
}
