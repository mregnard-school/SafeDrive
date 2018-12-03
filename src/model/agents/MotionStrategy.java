package model.agents;

import model.environment.Land;
import util.Intent;

public interface MotionStrategy extends Runnable{

  Intent getIntent(Vehicle agent, Land land);
}
