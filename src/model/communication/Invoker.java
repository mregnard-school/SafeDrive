package model.communication;

import java.io.Serializable;
import model.communication.message.Command;

public interface Invoker extends Serializable {

  void invoke(Command command);

}
