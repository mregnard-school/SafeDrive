package model.agents;

import model.environment.Land;
import util.Intent;
import util.IntentList;

public interface MotionStrategy {

  Intent getIntent(Vehicle agent, Land land);

  void run(IntentList intentList);
}
