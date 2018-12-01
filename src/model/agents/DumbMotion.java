package model.agents;

import java.util.List;
import java.util.Queue;
import model.communication.Command;
import model.environment.Direction;

public class DumbMotion implements MotionStrategy {

  @Override
  public void run(Agent agent) {
    Queue<Command> commands = agent.getCommands();
    //@todo analyze command

    //@todo send request

    //@Todo set direction
    Direction bestDirection = null;

    //if agent has a best direction
    if (bestDirection != null) {
      agent.setDirection(bestDirection);
    }
  }


}
