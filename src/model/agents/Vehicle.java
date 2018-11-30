package model.agents;

import java.awt.Point;
import java.util.List;
import java.util.Objects;
import model.communication.BroadcastInvoker;
import model.communication.Command;
import model.communication.CarReceiver;
import model.communication.DialogInvoker;
import model.communication.Invoker;
import model.communication.Receiver;
import model.communication.udp.UDPListener;
import model.environment.Direction;

public class Vehicle implements Agent, Runnable, Invoker, Receiver {

  private static int nbVehicles = 1;

  private MotionStrategy motionStrategy;
  private Invoker broadCast;
  private Invoker dialog;
  private Receiver receiver;
  private int plate;
  private int vitesse;
  private Point currentPos;
  private Point destination;
  private Point nexPos;
  private Direction direction;
  private List<Command> messages;

  public Vehicle() {
    plate = nbVehicles;
    nbVehicles++;
    broadCast = new BroadcastInvoker();
    dialog = new DialogInvoker();
    receiver = new UDPListener(this);
  }

  public Vehicle(MotionStrategy motionStrategy) {
    this();
    setMotionStrategy(motionStrategy);
  }

  public void setMotionStrategy(MotionStrategy motionStrategy) {
    this.motionStrategy = motionStrategy;
  }

  public void accelerate(int vitesse) {
    this.vitesse += vitesse;
  }

  public void brake(int vitesse) {
    this.vitesse -= vitesse;
  }

  public void move() {
    nexPos = direction.position(currentPos);
  }

  @Override
  public void invoke(Command command) {
    if (command.getRecipients().size() == 1) {
      dialog.invoke(command);
    } else {
      broadCast.invoke(command);
    }
  }

  @Override
  public void receive(Command command) {
    messages.add(command);
  }

  @Override
  public void run() {
    motionStrategy.run(this);
  }

  @Override
  public boolean equals(Object obj) {
    return ((Vehicle) obj).plate == plate;
  }

  @Override
  public int hashCode() {
    return Objects.hash(plate);
  }

  @Override
  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  @Override
  public List<Command> getCommands() {
    return messages;
  }

  public int getPlate() {
    return plate;
  }

  public int getVitesse() {
    return vitesse;
  }

  public Point getCurrentPos() {
    return currentPos;
  }

  public Point getNexPos() {
    return nexPos;
  }
}
