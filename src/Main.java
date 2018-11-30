import java.util.ArrayList;
import java.util.List;
import model.agents.Vehicle;
import model.communication.Command;
import model.communication.Message;
import model.communication.Receiver;

public class Main {

  public static void main(String[] args) {

    Vehicle vehicle = new Vehicle();
    Vehicle vehicle1 = new Vehicle();


    List<Receiver> receivers = new ArrayList<>();
    receivers.add(vehicle1);
    Message message = new Message(receivers);

    vehicle.invoke(message);
  }
}
