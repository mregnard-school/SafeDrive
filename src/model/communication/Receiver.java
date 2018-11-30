package model.communication;

import java.io.Serializable;

public interface Receiver extends Serializable {

  void receive(Command command);
  int getId();
}
