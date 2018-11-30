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
  private Map<Point, Road> joins;

  public Road(Direction axis, Point position) {
    vehicles = new HashMap<>();
    exits = new ArrayList<>();
    joins = new HashMap<>();
    this.axis = axis;
    this.position = position;
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

  public void addJoin(Road road) {
    Point join = new Point();
    if (road.getAxis().equals(Direction.WEST) || road.getAxis().equals(Direction.EAST)) {
      join.x = position.x;
      join.y = road.getPosition().y;
    } else {
      join.y = position.y;
      join.x = road.getPosition().x;
    }
    joins.put(join, road);
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

  public Map<Point, Road> getJoins() {
    return joins;
  }
}
