package model.environment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import model.agents.Vehicle;
import util.Intent;

public class Land {

  private List<Road> roads;
  private List<Point> joins;
  private int width;
  private int height;

  public Land(int width, int height) {
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

  public Intent move(Vehicle vehicle, Point next) {
    if (!isAvailable(next)) {
      return new Intent(vehicle.getCurrentPos(), vehicle.getCurrentPos(), vehicle);
    }

    Point currentPos = vehicle.getCurrentPos();
    vehicle.log("moved from " + currentPos + " to " + next);
    Intent intent = new Intent(currentPos, next, vehicle);
    getRoadsForPoint(currentPos).forEach(road -> road.removeVehicle(vehicle));
    getRoadsForPoint(next).forEach(road -> road.addVehicle(vehicle));
    vehicle.move();

    return intent;
  }

  private boolean isAvailable(Point point) {
    return getRoadsForPoint(point)
        .noneMatch(road -> road.getVehicleAt(point).isPresent());
  }

  public void updateRoadsFor(Vehicle vehicle) {
    getRoadsForPoint(vehicle.getCurrentPos())
        .forEach(road -> road.addVehicle(vehicle));
  }
}
