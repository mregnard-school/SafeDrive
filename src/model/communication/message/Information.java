package model.communication.message;

import java.util.List;
import model.communication.Receiver;

public class Information  extends Message{

  public Information(List<Receiver> receivers, Priority priority) {
    super(receivers, priority);
  }

  @Override
  public void execute() {

  }
}
