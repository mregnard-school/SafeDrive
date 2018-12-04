package model.environment;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import model.agents.Vehicle;

public class Road {

  private Direction axis;
  private Map<Point, Optional<Vehicle>> vehicles;
  private int pivot; // x or y, depend if it's horizontal or not

  public Road(Direction axis, int pivot) {
    vehicles = new HashMap<>();
    this.axis = axis;
    this.pivot = pivot;
  }

  public boolean contains(Point point) {
    if (isHorizontal()) {
      return (point.y == pivot);
    } else {
      return (point.x == pivot);
    }
  }

  public void addVehicle(Point point, Vehicle vehicle) {
    vehicles.put(point, Optional.of(vehicle));
  }

  public void removeVehicle(Point point) {
    vehicles.put(point, Optional.empty());
  }

  public Optional<Vehicle> vehicleAt(Point point) {
    if (!vehicles.containsKey(point)) {
      return Optional.empty();
    }
    return vehicles.get(point);
  }

  @Override
  public String toString() {
    return "road: " + pivot + " " + getAxis();
  }

  public boolean isHorizontal() {
    return (axis.equals(Direction.WEST) || axis.equals(Direction.EAST));
  }

  public int getPivot() {
    return pivot;
  }

  public Direction getAxis() {
    return axis;
  }

}
