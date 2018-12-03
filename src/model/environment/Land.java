package model.environment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import model.agents.Vehicle;
import util.Intent;

public class Land {

  private List<Road> roads;
  private int width;
  private int height;

  public Land(int width, int height) {
    roads = new ArrayList<>();
    this.width = width;
    this.height = height;
    createRoads(5);
  }

  private void createRoads(int step) {
    createVerticalRoads(step);
    createHorizontalRoads(step);
  }

  private void createVerticalRoads(int step) {
    for (int i = 0; i < width; i += step) {
      roads.add(new Road(Direction.SOUTH, i));
      if (i + 1 < width) {
        roads.add(new Road(Direction.NORTH, i + 1));
      }
    }
  }

  private void createHorizontalRoads(int step) {
    for (int i = 0; i < height; i += step) {
      roads.add(new Road(Direction.EAST, i));
      if (i + 1 < height) {
        roads.add(new Road(Direction.WEST, i + 1));
      }
    }
  }

  public List<Road> getRoads() {
    return roads;
  }

  public Stream<Road> getRoadsForPoint(Point point) {
    return roads.stream().filter(road -> road.contains(point));
  }

  public Intent move(Vehicle vehicle, Point next) {
    if (!isAvailable(next)) {
      return new Intent(vehicle.getCurrentPos(), vehicle.getCurrentPos(), vehicle);
    }

    Point currentPos = vehicle.getCurrentPos();
    Intent intent = new Intent(currentPos, next, vehicle);
    getRoadsForPoint(currentPos).forEach(road -> road.removeVehicle(currentPos));
    getRoadsForPoint(next).forEach(road -> road.addVehicle(next, vehicle));
    vehicle.move();

    return intent;
  }

  private boolean isAvailable(Point point) {
    return getRoadsForPoint(point)
        .noneMatch(road -> road.vehicleAt(point).isPresent());
  }

  public void updateRoadsFor(Vehicle vehicle) {
    getRoadsForPoint(vehicle.getCurrentPos())
        .forEach(road -> road.addVehicle(vehicle.getCurrentPos(), vehicle));
  }

  public List<Point> roadExit(Road road, Point point) {
    List<Point> points = new ArrayList<>();
    Direction direction1;
    Direction direction2;
    if (road.isHorizontal()) {
      direction1 = Direction.NORTH;
      direction2 = Direction.SOUTH;
    } else {
      direction1 = Direction.EAST;
      direction2 = Direction.WEST;
    }

    Point first = direction1.next(point);
    Point second = direction2.next(point);
    if (isNextPointCorrect(first, direction1) && isInLand(first)) {
      points.add(first);
    }
    if (isNextPointCorrect(second, direction2) && isInLand(second)) {
      points.add(second);
    }
    return points;
  }

  private boolean isNextPointCorrect(Point point, Direction direction) {
    if (!getRoadsForPoint(point).findAny().isPresent()) {
      return false;
    }
    return getRoadsForPoint(point)
        .noneMatch(road -> road.getAxis().equals(direction.opposite()));
  }

  public boolean isInLand(Point point) {
    return point.y >= 0
        && point.x >= 0
        && point.y < height
        && point.x < width;
  }
}
