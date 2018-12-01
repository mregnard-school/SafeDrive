package model.communication;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import model.agents.Vehicle;

public class Router {

  private static Map<Integer, InetAddress> ips = new HashMap<>();
  private static Map<Integer, Integer> ports = new HashMap<>();

  public static void registerVehicle(Vehicle vehicle, InetAddress address, int port) {
    ips.put(vehicle.getId(), address);
    ports.put(vehicle.getId(), port);
  }

  public static InetAddress getAddressOf(Integer plate) {
    return ips.get(plate);
  }

  public static InetAddress getAddressOf(Vehicle vehicle) {
    return getAddressOf(vehicle.getId());
  }

  public static int getPortOf(Integer plate) {
    return ports.get(plate);
  }

  public static int getPortOf(Vehicle vehicle) {
    return getPortOf(vehicle.getId());
  }


}
