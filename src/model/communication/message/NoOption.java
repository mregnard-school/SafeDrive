package model.communication.message;

import model.agents.Handler;
import model.agents.Vehicle;
import model.communication.Receiver;

public class NoOption implements Command {

  private Receiver receiver;

  public NoOption(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public void execute() {
    Vehicle agent = Handler.getAgent(receiver.getId());
    agent.getSem().release(1);
    agent.setNoOption(true);
  }

  @Override
  public Receiver getReceiver() {
    return receiver;
  }

  @Override
  public String toString() {
    return "{NoOption: for "+ receiver.getId() +"}";
  }
}
