package model.environment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import model.agents.Vehicle;

public class Road {

  private Direction axis;
  private Point position;
  private Map<Point, Optional<Vehicle>> vehicles;
  private List<Point> exits;
  private List<Point> joins;
  private int pivot; // x or y, depend if it's horizontal or not

  public Road(Direction axis, int pivot) {
    vehicles = new HashMap<>();
    exits = new ArrayList<>();
    joins = new ArrayList<>();
    this.axis = axis;
    this.pivot = pivot;
    this.position = isHorizontal() ? new Point(0, pivot) : new Point(pivot, 0) ;
  }

  public boolean belongTo(Point point) {
    if (isHorizontal()) {
      return (point.x == pivot);
    } else {
      return (point.y == pivot);
    }
  }


  public void addExit(Point point) {
    exits.add(point);
  }

  public void addVehicle(Vehicle vehicle) {
    vehicles.put(vehicle.getCurrentPos(), Optional.of(vehicle));
  }

  public void removeVehicle(Vehicle vehicle) {
    vehicles.put(vehicle.getCurrentPos(), Optional.empty());
  }

  public Optional<Vehicle> getVehicleAt(Point point) {
    return vehicles.get(point);
  }

  public void addJoin(Point point) {
    joins.add(point);
  }

  public void setJoins(List<Point> joins) {
    this.joins = joins;
  }

  public Direction getAxis() {
    return axis;
  }

  public Point getPosition() {
    return position;
  }

  public List<Point> getExits() {
    return exits;
  }

  public List<Point> getJoins() {
    return joins;
  }

  public boolean isHorizontal() {
    return (axis.equals(Direction.WEST) || axis.equals(Direction.EAST));
  }

  public int getPivot() {
    return pivot;
  }
}
