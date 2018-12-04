package model.agents;

import java.util.concurrent.Callable;
import util.Intent;

public interface MotionStrategy extends Callable<Intent> {

  Intent getIntent(Vehicle agent);

}
