package view;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {

  private Stage stage;
  private static int WIDTH = 1000;
  private static int HEIGHT = 800;

  private static int WIDTH_GRID = 400;
  private static int HEIGHT_GRID  = 450;
  @Override
  public void start(Stage primaryStage) {
    this.stage = primaryStage;
    stage.setTitle("SafeDrive");
    this.initGrid(3, 2);
  }

  public static void main(String[] args) {
    launch(args);
  }

  public void initGrid(int height, int width) {
    double _height =  HEIGHT_GRID/ (double) height;
    double _width = WIDTH_GRID / (double) width;
    try {
      // Load root layout from fxml file.
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(App.class.getResource("MainView.fxml"));
      Pane rootLayout = loader.load();
      Controller controller = loader.getController();
      //GRID
      Grid grid = new Grid(height, width, _width, _height);
      grid.setLayout(33, 52);
      controller.setGrid(grid);

//      LayoutX="108.0" layoutY="68.0" prefHeight="816.0" prefWidth="862.0"
      ///
      rootLayout.getChildren().add(grid.getPane());
      Scene scene = new Scene(rootLayout, WIDTH, HEIGHT);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
