package model.communication;

import java.util.List;

public interface Command {

  void execute();

  List<Integer> getRecipients();    //to know whether the command is broadcast or not
}
