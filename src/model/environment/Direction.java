package model.environment;

import java.awt.Point;

public enum Direction {
  NORTH {
    public Point next(Point point) {
      return new Point(point.x, point.y -1);
    }
  },
  SOUTH {
    public Point next(Point point) {
      return new Point(point.x, point.y + 1);
    }
  },
  EAST {
    public Point next(Point point) {
      return new Point(point.x + 1, point.y);
    }
  },
  WEST {
    public Point next(Point point) {
      return new Point(point.x -1, point.y);
    }
  };

  public abstract Point next(Point point);
}
