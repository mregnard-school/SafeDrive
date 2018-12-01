package view;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.Queue;
import model.environment.Simulation;
import util.IntentList;

public class Loop implements Runnable {

  private Simulation simulation;
  private boolean run;
  private PropertyChangeSupport support;
  private Queue<IntentList> buffer;
  private int speed = 1000;

  public Loop(Simulation simulation) {
    this.simulation = simulation;
    this.run = false;
    support = new PropertyChangeSupport(this);
    buffer = new LinkedList<>();
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  public void interrupt() {
    Thread.currentThread().interrupt();
    this.run = false;
    this.buffer = new LinkedList<>();
    simulation.interrupt();
  }

  public void startPause() {
    this.run = !this.run;
  }

  private boolean isInterrupted() {
    return Thread.currentThread().isInterrupted();
  }

  public boolean shoudInterrupt() {
    return !simulation.hasNext() && buffer.isEmpty();
  }

  public boolean shouldRun() {
    return !shoudInterrupt() && run;
  }

  @Override
  public void run() {
    long currentTime = System.currentTimeMillis();
    while (!this.isInterrupted()) {
      while (shouldRun()) {
        if (simulation.hasNext()) {
          simulation.next();
          buffer.add(simulation.getIntents());
        }

        long loopTime = System.currentTimeMillis();

        if (loopTime - currentTime > speed) {
          draw();
          currentTime = loopTime;
        }
      }

      if (shoudInterrupt()) {
        this.interrupt();
      }
    }
  }

  private void draw() {
    IntentList intents = buffer.poll();

    if (intents != null) {
      support.firePropertyChange("simulation", this.simulation, intents);
    }
  }

  public Simulation getSimulation() {
    return this.simulation;
  }

  public void setSpeed(int speed) {
    this.speed = speed;
  }
}
