package model.communication;

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
