package view;

import java.util.Arrays;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class PanView {


  // We define here the colors
  private final Color EMPTY_COLOR = Color.WHITE;
  private final Color ROAD_COLOR = Color.GRAY;
  private final Color STROKE_COLOR = Color.BLACK;
  private final Color CAR_COLOR = Color.RED;


  //
  private GridPane pane = new GridPane();
  private Rectangle[][] rectangles;
  private int rows;
  private int columns;


  public PanView(int rows, int columns, double width, double height) {
    this.rows = rows;
    this.columns = columns;
    rectangles = new Rectangle[rows][columns];
    /*
     */
    drawView(width, height);
  }

  private void drawView(double width, double height) {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        StackPane stack = new StackPane();
        rectangles[i][j] = new Rectangle(i, j, width, height);
        rectangles[i][j].setFill(Color.WHITE);
//        rectangles[i][j].setStroke(STROKE_COLOR);
        stack.getChildren().addAll(rectangles[i][j]);
        getPane().add(stack, j, i );

      }
    }
  }
  
  public void init(){
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
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

  public void setLayout(double x, double y) {
    pane.setLayoutX(x);
    pane.setLayoutY(y);
  }


  public Color getCarColor() {
    return CAR_COLOR;
  }
}