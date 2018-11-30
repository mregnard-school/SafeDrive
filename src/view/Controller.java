package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class Controller {

  @FXML
  TextField iterationsInput;

  @FXML
  TextField vehiclesInput;

  @FXML
  Pane simulationPane;

  @FXML
  AnchorPane logPane;

  @FXML
  private void runSimulation(ActionEvent event) {
    System.out.println(this.vehiclesInput.getText());
    System.out.println(this.iterationsInput.getText());
    System.out.println("click on run simulation");
  }

  @FXML
  private void stopSimulation(ActionEvent event) {
    System.out.println(this.vehiclesInput.getText());
    System.out.println(this.iterationsInput.getText());
    System.out.println("click on run simulation");
  }

}
