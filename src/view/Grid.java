package view;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.environment.Land;
import model.environment.Road;
import util.Intent;


public class Grid {

  // We define here the colors
  private final Color EMPTY_COLOR = Color.WHITE;
  private final Color ROAD_COLOR = Color.GRAY;
  private final Color STROKE_COLOR = Color.BLACK;
  private final Color CAR_COLOR = Color.RED;
  private final Color DIRECTION_COLOR = Color.WHITE;


  //
  private GridPane pane = new GridPane();
  private Rectangle[][] rectangles;
  Map<Rectangle, Text> rectangleStringMap;
  private int height;
  private int width;


  public Grid(int nbRows, int nbColumns, double width, double height) {
    this.height = nbRows;
    this.width = nbColumns;
    rectangles = new Rectangle[nbRows][nbColumns];
    rectangleStringMap = new HashMap<>();
    drawView(width, height);
  }

  private void drawView(double width, double height) {
    for (int i = 0; i < this.height; i++) {
      for (int j = 0; j < this.width; j++) {
        StackPane stack = new StackPane();
        rectangles[i][j] = new Rectangle(i, j, width, height);
        rectangles[i][j].setFill(Color.WHITE);
        Text text = new Text("");
        text.setFill(DIRECTION_COLOR);
        rectangleStringMap.put(rectangles[i][j], text);
        stack.getChildren().addAll(rectangles[i][j], text);
        getPane().add(stack, j, i );

      }
    }
  }
  
  public void clearGrid(){
    rectangleStringMap.forEach((rectangle, text) -> {
      rectangle.setFill(EMPTY_COLOR);
      text.setText("");
    });
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        rectangles[i][j].setFill(EMPTY_COLOR);
      }
    }
  }


  public GridPane getPane() {
    return pane;
  }

  public void draw(int x, int y, Color color) {
    rectangles[y][x].setFill(color);
  }

  public void draw(Land land) {
    draw(land.getRoads());
//    land.getJoins().forEach(point -> {
//      draw(point, Color.BLUE);
//    });
  }

  public void draw(List<Road> roads) {
    roads.forEach(this::draw);
  }

  public void draw(Road road) {
    int nbTiles;
    int position;
    if (road.isHorizontal()) {
      nbTiles = width;
      position = road.getPivot();
    } else {
      nbTiles = height;
      position = road.getPivot();
    }
    for (int i = 0; i < nbTiles; i++) {
      Point point;
      if (road.isHorizontal()) {
        point = new Point(i, position);
      } else {
        point = new Point(position, i);
      }
      drawDirection(road, i, point);
      draw(point, ROAD_COLOR);
    }
  }

  private void drawDirection(Road road, int i, Point point) {
    String first = "";
    String second = "";
    switch (road.getAxis()) {
      case NORTH:
        first = "^";
        second = "|";
        break;
      case SOUTH:
        first = "|";
        second = "v";
        break;
      case EAST:
        first = "-";
        second = ">";
        break;
      case WEST:
        first = "<";
        second = "-";
        break;
    }
    if (i == 1) {
      draw(point, ROAD_COLOR, first);
    } else if (i == 2) {
      draw(point, ROAD_COLOR, second);
    }
  }

  public void draw(Intent intent) {
    draw(intent.getTo(), CAR_COLOR, String.valueOf(intent.getPlateAgent()));
  }

  public void draw(Point point, Color color) {
    draw(point.x, point.y, color);
  }

  public void draw(Point point, Color color, String text) {
    draw(point.x, point.y, color);
    rectangleStringMap.get(rectangles[point.y][point.x]).setText(text);
  }

  public void setLayout(double x, double y) {
    pane.setLayoutX(x);
    pane.setLayoutY(y);
  }

  public Color getCarColor() {
    return CAR_COLOR;
  }

  public Color getRoadColor() {
    return ROAD_COLOR;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }
}