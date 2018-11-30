package view;

import java.awt.Point;
import java.util.List;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.environment.Road;
import util.IntentList.Intent;


public class PanView {


  // We define here the colors
  private final Color EMPTY_COLOR = Color.WHITE;
  private final Color ROAD_COLOR = Color.GRAY;
  private final Color STROKE_COLOR = Color.BLACK;
  private final Color CAR_COLOR = Color.RED;


  //
  private GridPane pane = new GridPane();
  private Rectangle[][] rectangles;
  private int nbRows;
  private int nbColumns;


  public PanView(int nbRows, int nbColumns, double width, double height) {
    this.nbRows = nbRows;
    this.nbColumns = nbColumns;
    rectangles = new Rectangle[nbRows][nbColumns];
    /*
     */
    drawView(width, height);
  }

  private void drawView(double width, double height) {
    for (int i = 0; i < nbRows; i++) {
      for (int j = 0; j < nbColumns; j++) {
        StackPane stack = new StackPane();
        rectangles[i][j] = new Rectangle(i, j, width, height);
        rectangles[i][j].setFill(Color.WHITE);
//        rectangles[i][j].setStroke(STROKE_COLOR);
        stack.getChildren().addAll(rectangles[i][j]);
        getPane().add(stack, j, i );

      }
    }
  }
  
  public void clearGrid(){
    for (int i = 0; i < nbRows; i++) {
      for (int j = 0; j < nbColumns; j++) {
        rectangles[i][j].setFill(EMPTY_COLOR);
      }
    }
  }

  /**
   * @return the pane
   */
  public GridPane getPane() {
    return pane;
  }

  public void draw(int x, int y, Color color) {
    rectangles[x][y].setFill(color);
  }

  public void draw(List<Road> roads) {
    roads.forEach(road -> {
      int nbTiles =  0;
      int position =  0;
      if(road.isHorizontal()) {
        nbTiles = nbColumns;
        position = road.getPosition().y;
      } else {
        nbTiles = nbRows;
        position = road.getPosition().x;
      }
      for (int i = 0; i < nbTiles ; i++) {
        if (!road.isHorizontal()) {
          draw(new Point(i, position), ROAD_COLOR);
        } else {
          draw(new Point(position, i), ROAD_COLOR);
        }
      }
    });
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

  public int getNbRows() {
    return nbRows;
  }

  public int getNbColumns() {
    return nbColumns;
  }
}