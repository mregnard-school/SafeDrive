package model.communication.message;

import java.io.Serializable;
import java.util.List;
import model.communication.Receiver;

public interface Command extends Serializable {

  void execute();

  List<Receiver> getReceivers();    //to know whether the command is broadcast or not
}
