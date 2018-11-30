package model.environment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.agents.Vehicle;

public class Land {

  private List<Vehicle> agents;
  private List<Road> roads;

  public Land(int width, int height) {    //20 30
    agents = new ArrayList<>();
    roads = new ArrayList<>();
    int step = 5;

    for (int i = 0; i < width; i += step) {
      Road road = new Road(Direction.SOUTH, i);
      addJoinPoint(road, step, width);
      roads.add(road);
    }
    for (int i = 0; i < height; i+= step) {
      Road road = new Road(Direction.EAST, i);
      addJoinPoint(road, step, height);
      roads.add(road);
    }
  }

  public void addJoinPoint(Road road, int step, int length) {
    for (int i = 0; i < length; i += step) {
      Point point = road.isHorizontal() ? new Point(i, road.getPosition().x) : new Point(road.getPosition().y, i) ;
      road.addJoin(point);
    }
  }

  public List<Road> getRoads() {
    return roads;
  }
}
