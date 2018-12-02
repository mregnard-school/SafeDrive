package model.environment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import model.agents.Vehicle;
import model.communication.Receiver;
import model.communication.message.Priority;
import model.communication.message.RequestInformation;
import model.communication.message.RequestMove;
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
    Optional<Vehicle> block = getVehiculeAt(next);
    if (block.isPresent()) {
      //@TODO maybe it's not the best solution but meh
      List<Receiver> destinataires = Collections.singletonList(block.get());
      RequestInformation requestInformation = new RequestInformation(
          vehicle,
          destinataires,
          Priority.MEDIUM);
      //We send request for more information
//      vehicle.invoke(requestInformation);
      RequestMove requestMove = new RequestMove(
          vehicle,
          destinataires,
          Priority.MEDIUM);
//      vehicle.invoke(requestMove);
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

//  private boolean getVehiculeAt(Point point) {    //remove function cause the one below is better
//    return getRoadsForPoint(point)
//        .noneMatch(road -> road.getVehicleAt(point).isPresent());
//  }

  private Optional<Vehicle> getVehiculeAt(Point point) {
    return getRoadsForPoint(point)
        .filter(road -> road.getVehicleAt(point).isPresent())
        .map(road -> road.getVehicleAt(point)
            .get())          //don't need to check if it's present cause we test it above
        .findFirst();
  }

  public void updateRoadsFor(Vehicle vehicle) {
    getRoadsForPoint(vehicle.getCurrentPos())
        .forEach(road -> road.addVehicle(vehicle));
  }


//  public List<Road> getRoadAround(Road road, Point point) {
//    List<Road> roads = new ArrayList<>();
//    if (road.isHorizontal()) {
//      roads.addAll(getRoadsForPoint(Direction.NORTH.next(point)).collect(Collectors.toList()));
//      roads.addAll(getRoadsForPoint(Direction.SOUTH.next(point)).collect(Collectors.toList()));
//    } else {
//      roads.addAll(getRoadsForPoint(Direction.EAST.next(point)).collect(Collectors.toList()));
//      roads.addAll(getRoadsForPoint(Direction.WEST.next(point)).collect(Collectors.toList()));
//    }
//    return roads;
//  }

  public List<Point> roadExit(Road road, Point point) {
    List<Point> points = new ArrayList<>();
    Point first = null;
    Point second = null;
    if (road.isHorizontal()) {
      first = Direction.NORTH.next(point);
      second = Direction.SOUTH.next(point);
    } else {
      first = Direction.EAST.next(point);
      second = Direction.WEST.next(point);
    }
    if (getRoadsForPoint(first).collect(Collectors.toList()).isEmpty()) {
      points.add(first);
    }
    if (getRoadsForPoint(second).collect(Collectors.toList()).isEmpty()) {
      points.add(second);
    }
    return points;
  }


  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
}
