package model.communication.message;

import java.io.Serializable;
import model.communication.Receiver;

public interface Command extends Serializable {

  void execute();

  Receiver getReceiver();    //to know whether the command is broadcast or not
}
