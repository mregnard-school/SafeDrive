package model.agents;

import java.util.List;
import model.communication.Command;
import model.environment.Direction;

public interface Agent {
  List<Command> getCommands();

  void setDirection(Direction direction);
}
