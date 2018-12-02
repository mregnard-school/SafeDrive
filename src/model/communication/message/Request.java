package model.communication.message;

import java.util.List;
import model.communication.Invoker;
import model.communication.Receiver;

public class Request extends Message {

  public Request(Invoker author, List<Receiver> receivers, Type type, Priority priority) {
    super(author, receivers, priority);
  }
}
