package model.communication.message;

import java.util.List;
import model.communication.Invoker;
import model.communication.Receiver;

public class Information  extends Message{

  public Information(Invoker author, List<Receiver> receivers, Priority priority) {
    super(author, receivers, priority);
  }

  @Override
  public void execute() {

  }
}
