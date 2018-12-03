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
    Vehicle vehicle = (Vehicle) receiver;
    Vehicle agent = Handler.getAgent(vehicle.getId());
    agent.setNoOption(true);
  }

  @Override
  public Receiver getReceiver() {
    return receiver;
  }
}
