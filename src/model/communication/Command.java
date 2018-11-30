package model.communication;

import java.io.Serializable;
import java.util.List;

public interface Command extends Serializable {

  void execute();

  List<Integer> getRecipients();    //to know whether the command is broadcast or not
}
