package model.communication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import model.communication.message.Command;

public class DialogInvoker implements Invoker {

  private DatagramSocket socket;
  private Receiver receiver;

  public DialogInvoker() {
    try {
      socket = new DatagramSocket();
    } catch (SocketException e) {
      e.printStackTrace();
    }
  }

  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public void invoke(Command command) {
    try {
      int plate = receiver.getId();
      InetAddress address = Router.getAddressOf(plate);
      int port = Router.getPortOf(plate);

      ByteArrayOutputStream bStream = new ByteArrayOutputStream();
      ObjectOutput oo = new ObjectOutputStream(bStream);

      if(command == null) {
        System.out.println("Swag");

      }

      oo.writeObject(command);
      oo.close();

      byte[] serializedMessage = bStream.toByteArray();

      DatagramPacket request = new DatagramPacket(serializedMessage, serializedMessage.length, address, port);
      socket.send(request);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
