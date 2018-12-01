package model.agents;

import java.awt.Point;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import model.communication.BroadcastInvoker;
import model.communication.CarReceiver;
import model.communication.Command;
import model.communication.DialogInvoker;
import model.communication.Invoker;
import model.communication.Receiver;
import model.communication.Router;
import model.environment.Direction;
import util.Logger;

public class Vehicle implements Agent, Runnable, Invoker, Receiver {

  private static int nbVehicles = 1;

  private MotionStrategy motionStrategy;
  transient private BroadcastInvoker broadcaster;
  transient private DialogInvoker dialoger;
  private CarReceiver receiver;
  private int plate;
  private int speed;
  private Point currentPos;
  private Point destination;
  private Point nextPos;
  private Direction direction;
  private Queue<Command> commands;

  private Vehicle() {
    plate = nbVehicles++;
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
      MotionStrategy motionStrategy) {
    this();
    this.currentPos = currentPos;
    this.destination = destination;
    this.direction = direction;
    this.motionStrategy = motionStrategy;
    this.speed = 1;
  }

  public void accelerate(int acceleration) {
    this.speed += acceleration;
  }

  public void brake(int deceleration) {
    this.speed -= deceleration;
  }

  public void move() {
    nextPos = direction.next(currentPos);
  }

  @Override
  public void invoke(Command command) {
    if (command.getReceivers().size() == 1) {
      dialoger.setReceiver(command.getReceivers().get(0));
      dialoger.invoke(command);
    } else {
      broadcaster.invoke(command);
    }
  }

  @Override
  public void receive(Command command) {
    Logger.log("Command received");
    commands.add(command);
  }

  @Override
  public int getId() {
    return plate;
  }

  @Override
  public void run() {
    motionStrategy.run(this);
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

    return this.plate == other.plate;
  }

  @Override
  public int hashCode() {
    return Objects.hash(plate);
  }

  @Override
  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public void setCurrentPos(Point currentPos) {
    this.currentPos = currentPos;
  }

  @Override
  public Queue<Command> getCommands() {
    return commands;
  }

  @Override
  public String toString() {
    return "Vehicule= {currentPos: "+ currentPos +", destination: " + "}" +destination;
  }

  public int getPlate() {
    return plate;
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

  public void log() {

  }
}
