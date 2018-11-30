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

  private static int WIDTH_GRID = 1000;
  private static int HEIGHT_GRID  = 800;
  @Override
  public void start(Stage primaryStage) {
    this.stage = primaryStage;
    stage.setTitle("SafeDrive");

    initRootLayout();
    this.initGrid(100, 100);
  }

  public static void main(String[] args) {
    launch(args);
  }

  public void initRootLayout() {
    try {
      // Load root layout from fxml file.
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(App.class.getResource("MainView.fxml"));
      Pane rootLayout = loader.load();
      Scene scene = new Scene(rootLayout, WIDTH, HEIGHT);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void initGrid(int y, int x) {
    double height =  HEIGHT_GRID/ (double) y;
    double width = WIDTH_GRID / (double) x;
    try {
      // Load root layout from fxml file.
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(App.class.getResource("MainView.fxml"));
      Pane rootLayout = loader.load();
      Controller controller = loader.getController();
      //GRID
      PanView panView = new PanView(y, x, width, height);
      panView.setLayout(108, 68);
      controller.setPanView(panView);

//      LayoutX="108.0" layoutY="68.0" prefHeight="816.0" prefWidth="862.0"
      ///
      rootLayout.getChildren().add(panView.getPane());
      Scene scene = new Scene(rootLayout, WIDTH, HEIGHT);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
