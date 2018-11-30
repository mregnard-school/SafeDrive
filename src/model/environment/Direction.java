package model.environment;

import java.awt.Point;

public enum Direction {
  NORTH {
    public Point position(Point point) {
      return new Point(point.x, point.y -1);
    }
  },
  SOUTH {
    public Point position(Point point) {
      return new Point(point.x, point.y + 1);
    }
  },
  EAST {
    public Point position(Point point) {
      return new Point(point.x + 1, point.y);
    }
  },
  WEST {
    public Point position(Point point) {
      return new Point(point.x -1, point.y);
    }
  };

  public abstract Point position(Point point);
}
