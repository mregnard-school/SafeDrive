package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.environment.Simulation;
import util.Logger;

public class Controller implements PropertyChangeListener {

  private static final ObservableList<String> logs = FXCollections.observableArrayList();

  private Loop loop;

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
  Button runButton;

  @FXML
  Label iterationsLabel;

  public Controller() {
    Logger.addPropertyChangeListener(this);
  }

  private boolean checkIfInteger(TextField input, String value) {

    if (value.matches("\\d*")) {
      return true;
    }

    input.setText(value.replaceAll("[^\\d]", ""));
    return false;
  }

  @FXML
  public void initialize() {
    iterationsLabel.setText("-");
    runButton.setDisable(true);
    iterationsInput.textProperty().addListener(((observable, oldValue, newValue) -> {
      if ("".equals(newValue)) {
        runButton.setDisable(true);
      } else {

        if(checkIfInteger(iterationsInput, newValue)) {
          runButton.setDisable(false);
        }
      }
    }));
    vehiclesInput.textProperty().addListener((
        (observable, oldValue, newValue) -> checkIfInteger(vehiclesInput, newValue)
    ));
  }

  private PanView panView;

  public void resetSimulation() {
    Simulation simulation = new Simulation(Integer.valueOf(iterationsInput.getText()));
    loop = new Loop(simulation);
    loop.addPropertyChangeListener(this);
    new Thread(loop).start();
  }

  @FXML
  private void runSimulation(ActionEvent event) {
    if (loop == null) {
      resetSimulation();
    }

    String text = runButton.getText();

    if ("Run".equals(text)) {
      runButton.setText("Pause");
    } else {
      runButton.setText("Run");
    }

    loop.startPause();
  }

  @FXML
  private void stopSimulation(ActionEvent event) {
    loop.interrupt();
    loop = null;
    runButton.setText("Run");
    iterationsLabel.setText("-");
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String property = evt.getPropertyName();
    if ("logs".equals(property)) {
      this.log(evt);
    }
    if ("simulation".equals(property)) {
      this.simulationUpdate(evt);
    }
  }

  private void simulationUpdate(PropertyChangeEvent evt) {
    Simulation updatedSimulation = (Simulation) evt.getNewValue();
    Platform.runLater(() -> {
      displayCurrentIteration(updatedSimulation);
      //Other stuff => Display map
    });
  }

  private void displayCurrentIteration(Simulation simulation) {
    int step = simulation.getCurrentStep();
    int totalStep = simulation.getMaxIterations();
    String iterations = step + "/" + totalStep;
    iterationsLabel.setText(iterations);
  }

  private void log(PropertyChangeEvent evt) {
    Platform.runLater(() -> {
      String newEntry = (String) evt.getNewValue();
      logs.add(newEntry);
      logsList.setItems(logs);
    });
  }

  public void setPanView(PanView panView) {
    this.panView = panView;
  }
}
