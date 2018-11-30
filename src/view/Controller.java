package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.environment.Simulation;

public class Controller implements PropertyChangeListener {

  private static final ObservableList<String> logs =
      FXCollections.observableArrayList();

  @FXML
  TextField iterationsInput;

  @FXML
  ListView<String> logsList;

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
    Simulation simulation = new Simulation(10);
    while(simulation.hasNext()) {
      simulation.next();
    }
  }

  @FXML
  private void stopSimulation(ActionEvent event) {
    System.out.println(this.vehiclesInput.getText());
    System.out.println(this.iterationsInput.getText());
    System.out.println("click on run simulation");
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String newEntry = (String) evt.getNewValue();
    logs.add(newEntry);
    logsList.setItems(logs);
  }
}
