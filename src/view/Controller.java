package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.environment.Simulation;
import util.Logger;

public class Controller implements PropertyChangeListener {

  private static final Logger LOGGER = Logger.getLogger();

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

  public Controller() {
    System.out.println("In constructor");

    Logger.addPropertyChangeListener(this);
  }

  @FXML
  private void runSimulation(ActionEvent event) {
    System.out.println(this.vehiclesInput.getText());
    System.out.println(this.iterationsInput.getText());
    System.out.println("click on run simulation");
    Simulation simulation = new Simulation(10);
    new Thread(() -> {
      while (simulation.hasNext()) {
        simulation.next();
      }
    }).start();
  }

  @FXML
  private void stopSimulation(ActionEvent event) {
    System.out.println(this.vehiclesInput.getText());
    System.out.println(this.iterationsInput.getText());
    System.out.println("click on run simulation");
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Platform.runLater(() -> {
      String newEntry = (String) evt.getNewValue();
      logs.add(newEntry);
      logsList.setItems(logs);
    });
  }
}
