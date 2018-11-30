package model.communication;

import java.io.Serializable;

public interface Invoker extends Serializable {

  void invoke(Command command);
}
