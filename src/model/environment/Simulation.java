package model.environment;

import java.awt.Point;
import java.util.List;
import model.agents.Agent;
import model.communication.Command;
import util.IntentList;
import util.IntentList.Intent;
import util.Logger;

public class Simulation {

  private int maxIterations;
  private int currentStep;
  private IntentList intents; 

  public Simulation(int maxIterations) {
    this.maxIterations = maxIterations;
    this.currentStep = 0;
  }

  public int getMaxIterations() {
    return maxIterations;
  }

  public int getCurrentStep() {
    return currentStep;
  }

  public void next() {
    if (!this.hasNext()) {
      return;
    }
    this.currentStep++;
    intents = new IntentList();

    Agent a  = new Agent() {
      @Override
      public List<Command> getCommands() {
        return null;
      }

      @Override
      public void setDirection(Direction direction) {

      }
    };

    intents.addIntent(a, new Point(currentStep-1, 0), new Point(currentStep, 0));

    Logger.getLogger().log("Finished stepped " + currentStep);
  }

  public boolean hasNext() {
    return currentStep < maxIterations;
  }

  public IntentList getIntents() {
    return this.intents;
  }
}
