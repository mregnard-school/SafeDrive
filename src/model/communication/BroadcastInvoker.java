package model.communication;

import java.util.List;

public class BroadcastInvoker implements Invoker {

  private List<Receiver> receivers;
  private DialogInvoker dialogInvoker;

  public BroadcastInvoker(DialogInvoker dialogInvoker) {
    this.dialogInvoker = dialogInvoker;
  }

  public void setReceivers(List<Receiver> receivers) {
    this.receivers = receivers;
  }

  @Override
  public void invoke(Command command) {
    receivers.forEach(receiver -> {
      dialogInvoker.setReceiver(receiver);
      dialogInvoker.invoke(command);
    });
  }
}
