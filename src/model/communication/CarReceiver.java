package model.communication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import model.communication.message.Command;

public class CarReceiver implements Runnable, Receiver {

  transient private DatagramSocket socket;
  transient private byte[] buf = new byte[2048];
  transient private Receiver receiver;
  transient private int port;
  transient private boolean shouldRun = true;

  public CarReceiver(Receiver receiver) {
    try {
      this.port = this.getAvailablePort();
      this.socket = new DatagramSocket(port);
      this.receiver = receiver;
      new Thread(this).start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    while (shouldRun) {
      DatagramPacket packet = new DatagramPacket(buf, buf.length);
      try {
        socket.receive(packet);
        InetAddress clientAddr = packet.getAddress();
        byte[] bytesReceived = packet.getData();

        ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(bytesReceived));
        Command command = (Command) iStream.readObject();
        iStream.close();

        this.receive(command);

      } catch (IOException e) {
        //e.printStackTrace();
        break;
      } catch (ClassNotFoundException e) {
        //Do nothing
      }

    }
  }

  public int getAvailablePort() throws IOException {
    for (int i = 1024; i < 65535; i++) {
      try {
        DatagramSocket socket = new DatagramSocket(i);
        socket.close();
        return i;
      } catch (IOException e) { /*Do nothing*/}
    }

    throw new IOException("No available port !!");
  }

  @Override
  public void receive(Command command) {
    receiver.receive(command);
  }

  @Override
  public int getId() {
    return 0;
  }

  public int getPort() {
    return this.port;
  }

  public void interrupt() {
    if (socket != null) {
      socket.close();
      socket = null;
    }
    shouldRun = false;
  }
}
