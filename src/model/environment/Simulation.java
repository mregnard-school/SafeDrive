package model.environment;


import java.util.Observable;
import sun.rmi.runtime.Log;
import util.Logger;

public class Simulation {

  private final Logger LOGGER =  Logger.getLogger();
  private int nbIterations;
  private int currentStep;


  public Simulation(int nbIterations) {
    this.nbIterations = nbIterations;
    this.currentStep = 0;
  }

  public boolean hasNext() {
    return currentStep < nbIterations;
  }

  public void next() {
    if (!this.hasNext()) { //IF THERE IS NOTHING AFTER
      Logger.log("Swag");
      return;
    }
    this.currentStep ++;
  }
}
