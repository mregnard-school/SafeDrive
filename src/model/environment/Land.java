package model.environment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import model.agents.Vehicle;

public class Land {

  private List<Vehicle> agents;
  private List<Road> roads;
  private List<Point> joins;

  public Land(int width, int height) {    //20 30
    agents = new ArrayList<>();
    roads = new ArrayList<>();
    joins = new ArrayList<>();
    int step = 5;

    for (int i = 0; i < width; i += step) {
      Road road = new Road(Direction.SOUTH, i);
      roads.add(road);
    }
    for (int i = 0; i < height; i+= step) {
      Road road = new Road(Direction.EAST, i);
      roads.add(road);
    }
    addJoinPoint(step, width, height);
  }


  public void addJoinPoint(int step, int width, int height) {
    for (int i = 0; i < height; i += step) {
      for (int j = 0; j < width; j += step) {
        joins.add(new Point(i, j));
      }
    }
  }

  public List<Road> getRoads() {
    return roads;
  }

  public List<Point> getJoins() {
    return joins;
  }
}
