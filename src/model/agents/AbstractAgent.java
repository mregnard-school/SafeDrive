package model.agents;

public abstract class AbstractAgent implements Agent {

  private MotionStrategy motionStrategy;

  public void setMotionStrategy(MotionStrategy motionStrategy) {
    this.motionStrategy = motionStrategy;
  }
}
