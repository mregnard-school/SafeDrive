package view;

import java.io.IOException;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

  private Stage stage;
  private static int WIDTH = 1900;
  private static int HEIGHT = 1080;

  @Override
  public void start(Stage primaryStage) {
    this.stage = primaryStage;
    stage.setTitle("SageDrive");

    initRootLayout();
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
}
