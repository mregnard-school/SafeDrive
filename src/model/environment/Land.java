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
    Vehicle agent = new Vehicle(new DumbMotion());
    agent.setCurrentPos(new Point(25, 10));
    road.addVehicle(agent);
  }

  public Land() {
    init();
  }
}
