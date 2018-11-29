import model.environment.Simulation;

public class Main {

  public static void main(String[] args) {
    Simulation simulation = new Simulation(10);
    while (simulation.hasNext()) {
      simulation.next();
    }
  }
}
