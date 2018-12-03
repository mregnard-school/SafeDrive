package model.environment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    //Logger.l
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
        roads.add(new Road(Direction.NORTH, i + 1));
      }
    }
  }

  public void createHorizontalRoads(int step) {
    for (int i = 0; i < height; i += step) {
      roads.add(new Road(Direction.EAST, i));
      if (i + 1 < height) {
        roads.add(new Road(Direction.WEST, i + 1));
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
    //vehicle.log("moved from " + currentPos + " to " + next);
    Intent intent = new Intent(currentPos, next, vehicle);
    getRoadsForPoint(currentPos).forEach(road -> road.removeVehicle(currentPos));
    getRoadsForPoint(next).forEach(road -> road.addVehicle(next, vehicle));
    vehicle.move();

    return intent;
  }

  private boolean isAvailable(Point point) {    //remove function cause the one below is better
    return getRoadsForPoint(point)
        .noneMatch(road -> road.getVehicleAt(point).isPresent());
  }

  public Optional<Vehicle> getVehicleAt(Point point) {
    return getRoadsForPoint(point)
        .filter(road -> road.getVehicleAt(point).isPresent())
        .map(road -> road.getVehicleAt(point).get())
        .findFirst();
  }

  public void updateRoadsFor(Vehicle vehicle) {
    getRoadsForPoint(vehicle.getCurrentPos())
        .forEach(road -> road.addVehicle(vehicle.getCurrentPos(), vehicle));
  }

  public List<Point> roadExit(Road road, Point point) {
    List<Point> points = new ArrayList<>();
    Point first;
    Point second;
    Direction direction1;
    Direction direction2;
    if (road.isHorizontal()) {
      direction1 = Direction.NORTH;
      direction2 = Direction.SOUTH;
    } else {
      direction1 = Direction.EAST;
      direction2 = Direction.WEST;
    }

    first = direction1.next(point);
    second = direction2.next(point);
    if (debg(first, direction1) && isInLand(first)) {
      points.add(first);
    }
    if (debg(second, direction2) && isInLand(second)) {
      points.add(second);
    }
    return points;
  }

  private boolean debg(Point point, Direction direction) {
    return getRoadsForPoint(point)
        .anyMatch(road -> road.getAxis() == direction);
  }


  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public boolean isInLand(Point point) {
    return point.y >= 0
        && point.x >= 0
        && point.y < getHeight()
        && point.x < getWidth();
  }
}
