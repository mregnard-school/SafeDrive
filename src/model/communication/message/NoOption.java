package model.communication.message;

import java.util.Collections;
import java.util.List;
import model.communication.Receiver;

public class NoOption implements Command {

  private Receiver receiver;

  public NoOption(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public void execute() {

  }

  @Override
  public List<Receiver> getReceivers() {
    return Collections.singletonList(receiver);
  }
}
