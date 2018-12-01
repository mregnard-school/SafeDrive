package model.environment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
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

  public Simulation(int maxIterations, int width, int height, int nbAgent) {
    land = new Land(width, height);
    this.maxIterations = maxIterations;
    this.currentStep = 0;
    vehicles = new ArrayList<>();
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
    // @todo [irindul-2018-12-01] : Change with real position
    Point startingPosition = new Point(0, 0);
    // @todo [irindul-2018-12-01] : Change with random destination
    Point destination = new Point(10, 10);
    // @todo [irindul-2018-12-01] : Change with road direction
    Direction direction = Direction.SOUTH;
    MotionStrategy movement = new DumbMotion();

    Vehicle vehicle = new Vehicle(startingPosition, destination, direction, movement);
    this.vehicles.add(vehicle);

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
