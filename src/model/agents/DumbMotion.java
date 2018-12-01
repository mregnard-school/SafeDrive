package model.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import model.communication.Command;
import model.environment.Direction;

public class DumbMotion implements MotionStrategy {

  @Override
  public void run(Agent agent) {
    List<Command> commands = agent.getCommands();
    List<Direction>  answers = analyzeMessage(commands);
    if (!answers.isEmpty()) {
      //@todo Do something with this directions
    }

//    agent.getActions().stream().min(direction -> {
//
//    });
    //@todo send request

    //@Todo set direction
    Direction bestDirection = null;

    //if agent has a best direction
    if (bestDirection != null) {
      agent.setDirection(bestDirection);
    }
  }

  private List<Direction> analyzeMessage(List<Command> commands) {
    List<Direction> answers = new ArrayList<>();
    return answers;
  }


}
