package view;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.function.Consumer;
import model.agents.Agent;
import model.environment.Simulation;
import util.IntentList;
import util.IntentList.Intent;

public class Loop implements Runnable {

  private Simulation simulation;
  private boolean run;
  private PropertyChangeSupport support;
  private Queue<IntentList> buffer;
  //private int displayedStep = 0;

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
  }

  public void startPause() {
    this.run = !this.run;
  }

  private boolean isInterrupted() {
    return Thread.currentThread().isInterrupted();
  }

  private boolean shouldRun() {
    return (simulation.hasNext() || !buffer.isEmpty()) && run;
  }

  @Override
  public void run() {
    long currentTime = System.currentTimeMillis();
    while (!this.isInterrupted()) {
      while (shouldRun()) {
        simulation.next();
        long loopTime = System.currentTimeMillis();
        buffer.add(simulation.getIntents());

        System.out.println("Loop time " + loopTime);
        System.out.println("Current time " + currentTime);
        System.out.println("diff " + (loopTime - currentTime));

        if (loopTime - currentTime > 1000) {
          System.out.println("What");

          draw();
          currentTime = loopTime;
        }
        //support.firePropertyChange("simulation", false, simulation);
      }
    }
  }

  private void draw() {
    IntentList intents = buffer.poll();
    if (intents != null) {
      System.out.println("Firering");

      support.firePropertyChange("simulation", this.simulation, intents);
    }
  }
}
