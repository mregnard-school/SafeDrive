package model.environment;


import java.beans.PropertyChangeSupport;
import util.Logger;

public class Simulation {

  private int nbIterations;
  private int currentStep;
  private PropertyChangeSupport support;


  public Simulation(int nbIterations) {
    this.nbIterations = nbIterations;
    this.currentStep = 0;
  }

  public boolean hasNext() {
    return currentStep < nbIterations;
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

    Logger.getLogger().log("Finished running yeah");
  }
}
