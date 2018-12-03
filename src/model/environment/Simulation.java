package model.environment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.stream.Stream;
import model.agents.CooperativeMotion;
import model.agents.Handler;
import model.agents.MotionStrategy;
import model.agents.Vehicle;
import util.Intent;
import util.IntentList;
import util.Logger;

public class Simulation {

  private int maxIterations;
  private int currentStep;
  private IntentList intents;
  private IntentList intentsToSend;
  private Land land;
  private List<Vehicle> vehicles;
  private int width;
  private int height;
  private Random random;
  Map<Point, Vehicle> agentInitialPositions;
  private final ExecutorService executorService = Executors
      .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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
    MotionStrategy movement = new CooperativeMotion();

    Vehicle vehicle = new Vehicle(currentPosition, destination, movement, land);
    agentInitialPositions.put(currentPosition, vehicle);
    this.vehicles.add(vehicle);
    land.updateRoadsFor(vehicle);
    Handler.addAgent(vehicle);
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
    Vehicle agent = agentInitialPositions.get(point);
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
    intentsToSend = new IntentList();
  }

  public void next() {
    if (!this.hasNext()) {
      return;
    }
    step();

    List<Vehicle> remaining = new ArrayList<>();

    vehicles.parallelStream().forEach(vehicle -> {
      Intent intent = vehicle.getIntent();
      intents.addIntent(intent);

      vehicle.setSem(new Semaphore(0));
    });

    List<Callable<Intent>> callables = new ArrayList<>();

    vehicles.forEach(vehicle -> {
      CooperativeMotion motion = (CooperativeMotion) vehicle.getMotionStrategy();
      motion.setIntents(intents);
      callables.add(motion);
    });

    try {
      List<Future<Intent>> results = executorService.invokeAll(callables);
      for (Future result : results) {
        Intent intent = (Intent) result.get();
        Vehicle vehicle = intent.getAgent();

        intentsToSend.addIntent(land.move(vehicle, vehicle.getNextPos()));
        if (vehicle.isArrived()) {
          land.getRoadsForPoint(vehicle.getCurrentPos())
              .forEach(road -> road.removeVehicle(vehicle.getCurrentPos()));
          vehicle.interrupt();
          Handler.removeAgent(vehicle);
        } else {
          remaining.add(vehicle);
        }
      }
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    Handler.resetCoins();
    Logger.log("Finished step: " + currentStep);
    vehicles = remaining;
  }

  public boolean hasNext() {
    return getCurrentStep() < getMaxIterations() && vehicles.size() > 0;
  }

  public IntentList getIntents() {
    return this.intentsToSend;
  }

  public Land getLand() {
    return land;
  }

  public void interrupt() {
    vehicles.forEach(vehicle -> {
      vehicle.interrupt();
      Handler.removeAgent(vehicle);
    });
  }

  public IntentList getInitialIntents() {
    IntentList intentList = new IntentList();
    this.vehicles.forEach(
        vehicle -> intentList.addIntent(new Intent(vehicle.getCurrentPos(), vehicle.getCurrentPos(), vehicle))
    );
    return intentList;
  }

  public boolean isEnded() {
    return vehicles.isEmpty();
  }
}
