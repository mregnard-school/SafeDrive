package model.environment;

import model.agents.Vehicle;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Road {

    private Direction axis;
    private Point position;
    private Map<Point, Optional<Vehicle>> vehicules;
    private List<Point> exits;
    private Map<Point, Road> join;
}
