package util;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import model.agents.Vehicle;

public class IntentList {

  private ConcurrentHashMap<Vehicle, Intent> intents;

  public IntentList() {
    intents = new ConcurrentHashMap<>();
  }

  public void addIntent(Intent intent) {
    intents.put(intent.getAgent(), intent);
  }

  public Stream<Intent> stream() {
    return intents.entrySet().stream().map(Entry::getValue);
  }
}
