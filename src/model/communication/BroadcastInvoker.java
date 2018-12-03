package model.communication;

import model.communication.message.Command;

public class BroadcastInvoker implements Invoker {

  private DialogInvoker dialogInvoker;

  public BroadcastInvoker(DialogInvoker dialogInvoker) {
    this.dialogInvoker = dialogInvoker;
  }

  @Override
  public void invoke(Command command) {
    command.getReceivers().forEach(receiver -> {
      dialogInvoker.setReceiver(receiver);
      dialogInvoker.invoke(command);
    });
  }
}
