package model.agents;

import static util.PointOperations.pointToString;

import java.awt.Point;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;
import model.communication.BroadcastInvoker;
import model.communication.CarReceiver;
import model.communication.DialogInvoker;
import model.communication.Invoker;
import model.communication.Receiver;
import model.communication.Router;
import model.communication.message.Command;
import model.communication.message.RequestInformation;
import model.environment.Direction;
import model.environment.Land;
import model.environment.Road;
import util.Logger;

public class Vehicle implements Agent, Runnable, Invoker, Receiver {

  private static int nbVehicles = 1;

  private transient MotionStrategy motionStrategy;
  transient private BroadcastInvoker broadcaster;
  private transient DialogInvoker dialoger;
  private CarReceiver receiver;
  private int id;
  private int speed;
  private Point currentPos;
  private Point destination;
  private Point nextPos;
  private Direction direction;
  private Queue<Command> commands;
  private transient Land land; //Need to put that cause Optional which is in Land > Road is not serializable

  private Vehicle() {
    id = nbVehicles++;
    dialoger = new DialogInvoker();
    broadcaster = new BroadcastInvoker(dialoger);
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
      Direction direction,
      MotionStrategy motionStrategy,
      Land land) {
    this();
    this.currentPos = currentPos;
    this.destination = destination;
    this.direction = direction;
    this.motionStrategy = motionStrategy;
    this.speed = 1;
    this.land = land;
    this.log(pointToString(destination));
  }

  public void accelerate(int acceleration) {
    this.speed += acceleration;
  }

  public void brake(int deceleration) {
    this.speed -= deceleration;
  }

  public void move() {
    this.currentPos = nextPos;
    nextPos = null;
  }

  @Override
  public void invoke(Command command) {
    log(command.toString());
    if (command.getReceivers().size() > 1) {
      broadcaster.invoke(command);
    } else if (command.getReceivers().size() == 1) {
      if (dialoger == null) {
        dialoger = new DialogInvoker();
      }
      dialoger.setReceiver(command.getReceivers().get(0));
      dialoger.invoke(command);
    }
  }

  @Override
  public List<Direction> getActions() {
    return land.getRoadsForPoint(currentPos).map(Road::getAxis).collect(Collectors.toList());
  }

  @Override
  public void receive(Command command) { //Get type of message (if information -> send it right away)
    Logger.log("Command received:" + command.toString());
    command.execute();
    if (command instanceof RequestInformation) {    //we don't need to store
      return;
    }
    commands.add(command);
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void run() {
    motionStrategy.getIntent(this, land);
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
  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public void setCurrentPos(Point currentPos) {
    this.currentPos = currentPos;
  }

  public Land getLand() {
    return land;
  }

  @Override
  public Queue<Command> getCommands() {
    return commands;
  }

  @Override
  public String toString() {
    return "Vehicule= {currentPos: "+ currentPos +", destination: " + "}" +destination;
  }

  public void setNextPos(Point pos) {
    nextPos = pos;
  }

  public int getSpeed() {
    return speed;
  }
  public Point getCurrentPos() {
    return currentPos;
  }

  public Point getNextPos() {
    return nextPos;
  }

  public void interrupt() {
    receiver.interrupt();
  }

  public void log(String logEntry) {
    String prefix = "Agent-" + getId() + " : ";
    Logger.log(prefix + logEntry);
  }

  public Point getDestination() {
    return this.destination;
  }

  public boolean isArrived() {
    if (this.currentPos.equals(destination)) {
      log("is arrived !");
      return true;
    }
    return false;
  }

  public static void resetId() {
    nbVehicles = 1;
  }
}
