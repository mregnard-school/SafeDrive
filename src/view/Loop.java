package view;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import model.environment.Simulation;

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
    while (!this.isInterrupted()) {
      while(shouldRun()) {
        simulation.next();
        support.firePropertyChange("simulation", false, simulation);
      }
    }
  }
}
