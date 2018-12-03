package util;

import java.awt.Point;

public class PointOperations {

  public static int getManhatanDistance(Point from, Point to) {
    return Math.abs(to.x - from.x) + Math.abs(to.y - from.y);
  }

  /*public static int getEuclidianDistance(Point from, Point to) {

  }*/

  public static String pointToString(Point point) {
    return "[x=" + point.x + ", y=" + point.y + "]";
  }
}
