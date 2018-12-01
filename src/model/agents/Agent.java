package model.agents;

import java.util.List;
import java.util.Queue;
import model.communication.Command;
import model.environment.Direction;

public interface Agent {

  Queue<Command> getCommands();

  void setDirection(Direction direction);

  List<Direction> getActions();
}
