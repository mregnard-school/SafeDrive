package model.communication.message;

import java.util.List;
import model.agents.Vehicle;
import model.communication.Invoker;
import model.communication.Receiver;

public abstract class Message implements Command {

  protected Priority priority;
  protected List<Receiver> receivers;
  protected Invoker author;

  public Message(Invoker author, List<Receiver> receivers, Priority priority) {
    this.author = author;
    this.receivers = receivers;
    this.priority = priority;
  }

  @Override
  public void execute() {
  }

  @Override
  public List<Receiver> getReceivers() {
    return receivers;
  }

  @Override
  public String toString() {
    Vehicle vehicle = (Vehicle) author;
    return "from: " + vehicle.getId() + ", priority: " + priority + "\n" + receivers;
  }
}
