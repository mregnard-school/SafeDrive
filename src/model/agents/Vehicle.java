package model.agents;

import java.awt.Point;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import model.communication.CarReceiver;
import model.communication.DialogInvoker;
import model.communication.Invoker;
import model.communication.Receiver;
import model.communication.Router;
import model.communication.message.Command;
import model.environment.Land;
import util.Intent;
import util.Logger;

public class Vehicle implements Invoker, Receiver {

  private static int nbVehicles = 1;

  private transient MotionStrategy motionStrategy;
  private transient DialogInvoker dialoger;
  private CarReceiver receiver;
  private int id;
  private Point currentPos;
  private Point destination;
  private Point nextPos;
  private Queue<Command> commands;
  private transient Land land; //Need to put that cause Optional which is in Land > Road is not serializable
  private transient Semaphore semaphore;
  private List<Double> costs;
  private boolean noOption;

  private Vehicle() {
    id = nbVehicles++;
    dialoger = new DialogInvoker();
    receiver = new CarReceiver(this);
    commands = new LinkedList<>();

    try {
      InetAddress addr = InetAddress.getByName("127.0.0.1");
      int port = receiver.getPort();
      Router.registerVehicle(this, addr, port);
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  public Vehicle(Point currentPos,
      Point destination,
      MotionStrategy motionStrategy,
      Land land) {
    this();
    this.currentPos = currentPos;
    this.destination = destination;
    this.motionStrategy = motionStrategy;
    this.land = land;
  }

  public void move() {
    this.currentPos = nextPos;
    nextPos = null;
    costs = new ArrayList<>();
    noOption = false;
  }

  @Override
  public void invoke(Command command) {
    if (dialoger == null) {
      dialoger = new DialogInvoker();
    }

    dialoger.invoke(command);
  }

  @Override
  public void receive(
      Command command) { //Get type of message (if information -> send it right away)
    command.execute();
    commands.add(command);
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (!(obj instanceof Vehicle)) {
      return false;
    }
    Vehicle other = (Vehicle) obj;
    return this.id == other.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Vehicule= {currentPos: " + currentPos + ", destination: " + "}" + destination;
  }

  public void setNextPos(Point pos) {
    nextPos = pos;
  }

  public void interrupt() {
    receiver.interrupt();
  }

  public void log(String logEntry) {
    String prefix = "Agent-" + getId() + " : ";
    Logger.log(prefix + logEntry);
  }

  public boolean isArrived() {
    if (this.currentPos.equals(destination)) {
      log(" is arrived !");
      return true;
    }
    return false;
  }

  public static void resetId() {
    nbVehicles = 1;
  }

  public void setSem(Semaphore sem) {
    this.semaphore = sem;
  }

  public void addCost(double cost) {
    this.costs.add(cost);
  }

  public void setNoOption(boolean noOption) {
    this.noOption = noOption;
  }

  public boolean hasNoOption() {
    return noOption;
  }

  public Land getLand() {
    return land;
  }

  public Point getCurrentPos() {
    return currentPos;
  }

  public Point getNextPos() {
    return nextPos;
  }

  public Point getDestination() {
    return this.destination;
  }

  public Intent getIntent() {
    return motionStrategy.getIntent(this);
  }

  public MotionStrategy getMotionStrategy() {
    return motionStrategy;
  }

  public Semaphore getSem() {
    return this.semaphore;
  }

  public List<Double> getCosts() {
    return this.costs;
  }
}
