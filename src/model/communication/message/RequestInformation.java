package model.communication.message;

import java.util.Collections;
import java.util.List;
import model.agents.Vehicle;
import model.communication.Invoker;
import model.communication.Receiver;

public class RequestInformation extends Message {

  public RequestInformation(Invoker author, List<Receiver> receivers, Priority priority) {
    super(author, receivers, priority);
  }

  @Override
  public void execute() {
    System.out.println("dans request informations");

    System.out.println(this);
    receivers.forEach(receiver -> {
      if (!(receiver instanceof Vehicle)) {
        return;
      }
      Vehicle vehicle = (Vehicle) receiver;
      List<Receiver> destinataires = Collections.singletonList((Receiver)author);
      vehicle.invoke(new Information(vehicle, destinataires, Priority.LOW));
    });
  }

  @Override
  public String toString() {
    return "{RequestInformation: "+ super.toString() + "}";
  }
}
