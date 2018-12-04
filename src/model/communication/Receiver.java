package model.communication;

import java.io.Serializable;
import model.communication.message.Command;

public interface Receiver extends Serializable {

  void receive(Command command);
  int getId();
}
