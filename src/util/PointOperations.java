package util;

import java.awt.Point;

public class PointOperations {

  public static int getManhatanDistance(Point from, Point to) {
    return Math.abs(to.x - from.x) + Math.abs(to.y - from.y);
  }

  public static double getEuclidianDistance(Point from, Point to) {
    return Math.sqrt(Math.pow((from.x - to.x), 2) + Math.pow((from.y - to.y), 2));
  }

  public static String pointToString(Point point) {
    return "[x=" + point.x + ", y=" + point.y + "]";
  }
}
