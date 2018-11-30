import model.communication.udp.UDPListener;

public class Main {

  public static void main(String[] args) {
    UDPListener server = new UDPListener();
    server.run();


  }
}
