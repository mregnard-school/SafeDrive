package model.environment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;
import model.agents.Agent;
import model.agents.DumbMotion;
import model.agents.MotionStrategy;
import model.agents.Vehicle;
import util.IntentList;

public class Simulation {

  private int maxIterations;
  private int currentStep;
  private IntentList intents;
  private Land land;
  private List<Vehicle> vehicles;
  private int width;
  private int height;
  private Random random;
  Map<Point, Agent> agentInitialPositions;

  public Simulation(int maxIterations, int width, int height, int nbAgent) {
    this.maxIterations = maxIterations;
    this.currentStep = 0;
    this.width = width;
    this.height = height;
    vehicles = new ArrayList<>();
    agentInitialPositions = new HashMap<>();
    random = new Random(12);
    land = new Land(width, height);
    createAgents(nbAgent);
  }

  private void createAgents(int nbAgent) {
    for (int i = 0; i < nbAgent; i++) {
      createAgent();
    }
  }

  private void createAgent() {
    Point currentPosition = getValidPoint();
    Point destination;
    do {
      destination = getValidPoint();
    } while (destination.equals(currentPosition));

    Road road = land.getRoadsForPoint(currentPosition).findFirst()
        .orElseThrow(IllegalStateException::new);

    Direction direction = road.getAxis();
    MotionStrategy movement = new DumbMotion();

    Vehicle vehicle = new Vehicle(currentPosition, destination, direction, movement, land);
    agentInitialPositions.put(currentPosition, vehicle);
    this.vehicles.add(vehicle);
    land.updateRoadsFor(vehicle);
  }

  private Point getValidPoint() {
    Point point;
    boolean isInvalidPoint;
    do {
      point = getRandomPoint();
      isInvalidPoint = checkPointValidity(point);
    } while (isInvalidPoint);

    return point;
  }

  private Point getRandomPoint() {
    return new Point(random.nextInt(width), random.nextInt(height));
  }

  private boolean checkPointValidity(Point point) {
    Stream<Road> roadStream = land.getRoadsForPoint(point);
    boolean belongsToRoad = roadStream.findAny().isPresent();
    Agent agent = agentInitialPositions.get(point);
    boolean isNullAgent = (agent == null);
    return !(belongsToRoad && isNullAgent);
  }

  public int getMaxIterations() {
    return maxIterations;
  }

  public int getCurrentStep() {
    return currentStep;
  }

  private void step() {
    this.currentStep++;
    intents = new IntentList();
  }

  public void next() {
    if (!this.hasNext()) {
      return;
    }
    step();

    List<Vehicle> remaining = new ArrayList<>();

    // @todo [irindul-2018-12-02] : Handle problem with blocked car (no available points for it)
    // @todo [irindul-2018-12-02] : Handle problem with available tile marked as unavalaible

    vehicles.forEach(vehicle -> {
      vehicle.run();
      Point next = vehicle.getNextPos();
      // @todo [irindul-2018-12-02] : Send multiple intents for same agent
      intents.addIntent(land.move(vehicle, next));

      if (vehicle.isArrived()) {
        land.getRoadsForPoint(vehicle.getCurrentPos())
            .forEach(road -> road.removeVehicle(vehicle));
        vehicle.interrupt();
      } else {
        remaining.add(vehicle);
      }
    });

    vehicles = remaining;
  }

  public boolean hasNext() {
    return getCurrentStep() < getMaxIterations();
  }

  public IntentList getIntents() {
    return this.intents;
  }

  public Land getLand() {
    return land;
  }

  public void interrupt() {
    vehicles.forEach(Vehicle::interrupt);
  }

  public IntentList getInitialIntents() {
    IntentList intentList = new IntentList();
    this.vehicles.forEach(
        vehicle -> intentList.addIntent(vehicle, vehicle.getCurrentPos(), vehicle.getCurrentPos())
    );
    return intentList;
  }

}
