package model.communication;

import java.io.Serializable;
import java.util.List;

public class Message implements Command, Serializable {

  private List<Receiver> receivers;

  public Message(List<Receiver> receivers) {
    this.receivers = receivers;
  }

  @Override
  public void execute() {

  }

  @Override
  public List<Receiver> getReceivers() {
    return receivers;
  }
}
