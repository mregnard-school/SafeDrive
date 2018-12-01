package model.environment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import model.agents.Vehicle;

public class Land {

  private List<Vehicle> agents;
  private List<Road> roads;
  private List<Point> joins;
  private int width;
  private int height;

  public Land(int width, int height) {
    agents = new ArrayList<>();
    roads = new ArrayList<>();
    joins = new ArrayList<>();
    this.width = width;
    this.height = height;
    createRoads(5);
  }

  public void createRoads(int step) {
    createVerticalRoads(step);
    createHorizontalRoads(step);
    addJoinPoint(step, width, height);
  }

  public void createVerticalRoads(int step) {
    for (int i = 0; i < width; i += step) {
      roads.add(new Road(Direction.SOUTH, i));
      if (i + 1 < width) {
        roads.add(new Road(Direction.NORTH, i));
      }
    }
  }

  public void createHorizontalRoads(int step) {
    for (int i = 0; i < height; i += step) {
      roads.add(new Road(Direction.EAST, i));
      if (i + 1 < width) {
        roads.add(new Road(Direction.WEST, i));
      }
    }
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

  public Stream<Road> getRoadsForPoint(Point point) {
    return roads.stream().filter(road -> road.contains(point));
  }
}
