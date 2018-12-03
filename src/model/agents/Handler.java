package model.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Handler {

  private static List<Vehicle> agents = new ArrayList<>();

  public static void addAgent(Vehicle agent) {
    if (!agents.contains(agent)) {
      agents.add(agent);
    }
  }

  public static void removeAgent(Vehicle agent) {
    agents.remove(agent);
  }

  public static Vehicle getAgent(int plate) {
    return agents.stream().filter(agent -> agent.getId() == plate)
        .findFirst().orElseThrow(NoSuchElementException::new);
  }
}
