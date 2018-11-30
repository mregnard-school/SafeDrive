package model.environment;

import util.Logger;

public class Simulation {

  private int maxIterations;
  private int currentStep;

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

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    Logger.getLogger().log("Finished stepped " + currentStep);
  }

  public boolean hasNext() {
    return currentStep < maxIterations;
  }
}
