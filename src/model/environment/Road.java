package model.environment;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import model.agents.Vehicle;

public class Road {

  private Direction axis;
  private Point position;
  private Map<Point, Optional<Vehicle>> vehicules;
  private List<Point> exits;
  private Map<Point, Road> join;
}
