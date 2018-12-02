package model.communication.message;

import java.util.List;
import model.communication.Receiver;

public abstract class Message implements Command {

  protected Priority priority;
  protected List<Receiver> receivers;

  public Message(List<Receiver> receivers, Priority priority) {
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
}
