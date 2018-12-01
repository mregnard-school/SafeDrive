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
import util.Logger;

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

  public IntentList getInitialIntents() {
    IntentList intentList = new IntentList();
    this.vehicles.forEach(
        vehicle -> intentList.addIntent(vehicle, vehicle.getCurrentPos(), vehicle.getCurrentPos())
    );
    return intentList;
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

    Vehicle vehicle = new Vehicle(currentPosition, destination, direction, movement);
    agentInitialPositions.put(currentPosition, vehicle);
    this.vehicles.add(vehicle);

  }

  private Point getValidPoint() {
    Point point;

    boolean isInvalidPoint;
    do {
      int x = random.nextInt(width);
      int y = random.nextInt(height);

      point = new Point(x, y);
      Stream<Road> roadStream = land.getRoadsForPoint(point);
      boolean belongsToRoad = roadStream.findAny().isPresent();

      Agent agent = agentInitialPositions.get(point);
      isInvalidPoint = !belongsToRoad || (agent != null);
    } while (isInvalidPoint);

    return point;
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
    Logger.log("Finished stepped " + currentStep);
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


}
