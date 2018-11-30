package model.agents;

import java.awt.Point;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import model.communication.BroadcastInvoker;
import model.communication.Command;
import model.communication.CarReceiver;
import model.communication.DialogInvoker;
import model.communication.Invoker;
import model.communication.Receiver;
import model.communication.Router;
import model.environment.Direction;
import util.Logger;

public class Vehicle implements Agent, Runnable, Invoker, Receiver {

  private static int nbVehicles = 1;

  private MotionStrategy motionStrategy;
  private BroadcastInvoker broadCast;
  private DialogInvoker dialog;
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
    dialog = new DialogInvoker();
    broadCast = new BroadcastInvoker(new DialogInvoker());
    receiver = new CarReceiver(this);
    messages = new ArrayList<>();

    try {
      InetAddress addr = InetAddress.getByName("127.0.0.1");
      int port = ((CarReceiver) receiver).getPort();
      Router.registerVehicle(this, addr, port );
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }

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
    if (command.getReceivers().size() == 1) {
      dialog.setReceiver(command.getReceivers().get(0));
      dialog.invoke(command);
    } else {
      broadCast.invoke(command);
    }
  }

  @Override
  public void receive(Command command) {
    Logger.getLogger().log("Command received");
    messages.add(command);
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
