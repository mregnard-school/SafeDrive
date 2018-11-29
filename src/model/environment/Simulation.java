package model.environment;

public class Simulation {

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
      return;
    }
    this.currentStep ++;
  }
}
