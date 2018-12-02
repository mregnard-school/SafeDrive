package model.communication.message;

import java.util.List;
import model.communication.Receiver;

public class Request extends Message {

  public Request(List<Receiver> receivers, Type type, Priority priority) {
    super(receivers, priority);
  }
}
