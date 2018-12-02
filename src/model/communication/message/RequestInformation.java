package model.communication.message;

import java.util.ArrayList;
import java.util.List;
import model.agents.Vehicle;
import model.communication.Receiver;

public class RequestInformation extends Message {

  public RequestInformation(List<Receiver> receivers, Type type, Priority priority) {
    super(receivers, priority);
  }

  @Override
  public void execute() {
    receivers.forEach(receiver -> {
      if (!(receiver instanceof Vehicle)) {
        return;
      }
      Vehicle vehicle = (Vehicle) receiver;
      List<Receiver> destinataires = new ArrayList<>();
      vehicle.invoke(new Information(destinataires, Priority.LOW));
    });
  }
}
