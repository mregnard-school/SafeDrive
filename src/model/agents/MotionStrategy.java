package model.agents;

import model.environment.Land;

public interface MotionStrategy {

  void run(Vehicle agent, Land land);
}
