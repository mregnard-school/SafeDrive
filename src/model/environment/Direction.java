package model.environment;

import java.awt.Point;
import javax.sound.midi.Soundbank;

public enum Direction {
  NORTH {
    public Point next(Point point) {
      return new Point(point.x, point.y -1);
    }

    public Direction opposite() {
      return SOUTH;
    }
  },
  SOUTH {
    public Point next(Point point) {
      return new Point(point.x, point.y + 1);
    }
    public Direction opposite() {
      return NORTH;
    }
  },
  EAST {
    public Point next(Point point) {
      return new Point(point.x + 1, point.y);
    }
    public Direction opposite() {
      return WEST;
    }
  },
  WEST {
    public Point next(Point point) {
      return new Point(point.x -1, point.y);
    }
    public Direction opposite() {
      return EAST;
    }
  };

  public abstract Point next(Point point);

  public abstract Direction opposite();
}
