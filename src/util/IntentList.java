package util;

import java.awt.Point;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import model.agents.Agent;

public class IntentList {

  private ConcurrentHashMap<Agent, Intent> intents;

  public IntentList() {
    intents = new ConcurrentHashMap<>();
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
