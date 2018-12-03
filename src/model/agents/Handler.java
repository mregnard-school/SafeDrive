package model.agents;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Handler {

  private static List<Vehicle> agents = new ArrayList<>();
  private static ReentrantLock lock = new ReentrantLock();
  private static ConcurrentHashMap<Point, AtomicInteger> coins = new ConcurrentHashMap<>();

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

  public static void registerForTurnament(Point point) {
    if(!coins.containsKey(point)) {
      coins.put(point, new AtomicInteger(1));
    }
  }

  public static boolean flipACoinFor(Point point) {
    try {
      lock.lock();
      AtomicInteger coin = coins.get(point);
      return coin.decrementAndGet() == 0;
    } finally {
      lock.unlock();
    }
  }


  public static void resetCoins() {
    coins = new ConcurrentHashMap<>();
  }
}
