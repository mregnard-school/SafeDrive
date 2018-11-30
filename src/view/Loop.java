package view;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import model.environment.Simulation;
import util.IntentList;

public class Loop implements Runnable {

  private Simulation simulation;
  private boolean run;
  private PropertyChangeSupport support;

  public Loop(Simulation simulation) {
    this.simulation = simulation;
    this.run = false;
    support = new PropertyChangeSupport(this);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  public void interrupt() {
    Thread.currentThread().interrupt();
    this.run = false;
  }

  public void startPause() {
    this.run = !this.run;
  }

  private boolean isInterrupted() {
    return Thread.currentThread().isInterrupted();
  }

  private boolean shouldRun() {
    return simulation.hasNext() && run;
  }

  @Override
  public void run() {
    long currentTime = System.currentTimeMillis();
    while (!this.isInterrupted()) {
      while (shouldRun()) {
        simulation.next();
        long loopTime = System.currentTimeMillis();

        if (loopTime - currentTime > 1000) {
          //Draw
          currentTime = loopTime;
        }
        //support.firePropertyChange("simulation", false, simulation);
      }
    }
  }
}
