package view;

import java.awt.Point;
import java.util.List;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.environment.Land;
import model.environment.Road;
import util.IntentList.Intent;


public class Grid {

  // We define here the colors
  private final Color EMPTY_COLOR = Color.WHITE;
  private final Color ROAD_COLOR = Color.GRAY;
  private final Color STROKE_COLOR = Color.BLACK;
  private final Color CAR_COLOR = Color.RED;


  //
  private GridPane pane = new GridPane();
  private Rectangle[][] rectangles;
  private int height;
  private int width;


  public Grid(int nbRows, int nbColumns, double width, double height) {
    this.height = nbRows;
    this.width = nbColumns;
    rectangles = new Rectangle[nbRows][nbColumns];
    drawView(width, height);
  }

  private void drawView(double width, double height) {
    for (int i = 0; i < this.height; i++) {
      for (int j = 0; j < this.width; j++) {
        StackPane stack = new StackPane();
        rectangles[i][j] = new Rectangle(i, j, width, height);
        rectangles[i][j].setFill(Color.WHITE);
        stack.getChildren().addAll(rectangles[i][j]);
        getPane().add(stack, j, i );

      }
    }
  }
  
  public void clearGrid(){
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
    rectangles[x][y].setFill(color);
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
        point = new Point(position, i);
      } else {
        point = new Point(i, position);
      }
      draw(point, ROAD_COLOR);
    }
  }

  public void draw(Intent intent) {
    draw(intent.getTo(), CAR_COLOR);
  }

  public void draw(Point point, Color color) {
    draw(point.x, point.y, color);
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