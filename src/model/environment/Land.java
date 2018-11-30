package model.environment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import model.agents.Agent;
import model.agents.DumbMotion;
import model.agents.Vehicle;

public class Land {

  private List<Vehicle> agents;
  private List<Road> roads;

  private void init() {
    agents = new ArrayList<>();
    roads = new ArrayList<>();
    Road road = new Road(Direction.EAST, new Point(0, 10));
    roads.add(road);
  }

  public Land() {
    init();
  }

  public List<Road> getRoads() {
    return roads;
  }
}
