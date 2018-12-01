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
import model.environment.Simulation;
import util.IntentList;
import util.Logger;

public class Controller implements PropertyChangeListener {

  private static final ObservableList<String> logs = FXCollections.observableArrayList();

  private Loop loop;

  @FXML
  TextField iterationsInput;

  @FXML
  TextField vehiclesInput;

  @FXML
  TextField speedInput;

  @FXML
  Button runButton;

  @FXML
  Button stopButton;

  @FXML
  Label iterationsLabel;

  @FXML
  ListView<String> logsList;

  private Grid grid;

  public Controller() {
    Logger.addPropertyChangeListener(this);
  }

  @FXML
  public void initialize() {
    iterationsLabel.setText("-");
    runButton.setDisable(true);
    stopButton.setDisable(true);
    speedInput.setText(String.valueOf(1000));
    setUpListeners();
  }

  private void setUpListeners() {
    iterationsInput.textProperty().addListener((observable, oldValue, newValue) -> {
      if ("".equals(newValue)) {
        runButton.setDisable(true);
      } else {
        if (ensureInteger(iterationsInput, newValue)) {
          runButton.setDisable(false);
        }
      }
    });
    vehiclesInput.textProperty().addListener(
        (observable, oldValue, newValue) -> ensureInteger(vehiclesInput, newValue)
    );
    speedInput.textProperty().addListener(
        (observable, oldValue, newValue) -> {
          if(ensureInteger(speedInput, newValue)) {
            int speed = Integer.parseInt(newValue);
            if(speed < 200) {
              speed = 200;
            }
            loop.setSpeed(speed);
          }
        }
    );
  }

  private boolean ensureInteger(TextField input, String value) {
    if (value.matches("\\d*")) {
      return true;
    }

    input.setText(value.replaceAll("[^\\d]", ""));
    return false;
  }

  public void resetSimulation() {
    int interations = Integer.parseInt(iterationsInput.getText());
    int width = grid.getWidth();
    int height = grid.getHeight();
    int nbAgents = Integer.parseInt(vehiclesInput.getText());

    Simulation simulation = new Simulation(Integer.valueOf(iterationsInput.getText()), grid.getWidth(), grid
        .getHeight(), nbAgents);
    iterationsLabel.setText("0/" + iterationsInput.getText());
    loop = new Loop(simulation);
    loop.addPropertyChangeListener(this);
    new Thread(loop).start();
    stopButton.setDisable(false);
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
    deleteSimulation();
    Logger.log("Simulation was stopped");
  }

  private void deleteSimulation() {
    if (loop == null) {
      return;
    }

    loop.interrupt();
    loop = null;
    runButton.setText("Run");
    iterationsLabel.setText("-");
    stopButton.setDisable(true);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Platform.runLater(() -> {
      String property = evt.getPropertyName();
      if ("logs".equals(property)) {
        this.log(evt);
      }
      if ("simulation".equals(property)) {
        this.simulationUpdate(evt);
      }
    });
  }

  private void simulationUpdate(PropertyChangeEvent evt) {
    grid.clearGrid();

    grid.draw(loop.getSimulation().getLand());
    IntentList intents = (IntentList) evt.getNewValue();
    intents.stream().forEach(grid::draw);
    displayCurrentIteration();
    checkIfFinishedAndCleanUp();
  }

  private void displayCurrentIteration() {
    int totalStep = loop.getSimulation().getMaxIterations();
    String iter = iterationsLabel.getText().split("/")[0];
    int iterationsCount = Integer.parseInt(iter);
    String iterations = (++iterationsCount) + "/" + totalStep;
    iterationsLabel.setText(iterations);
  }

  private void checkIfFinishedAndCleanUp() {
    if(loop.shouldRun()) {
      return;
    }

    deleteSimulation();
  }

  private void log(PropertyChangeEvent evt) {
    String newEntry = (String) evt.getNewValue();
    logs.add(newEntry);
    logsList.setItems(logs);
  }

  public void setGrid(Grid grid) {
    this.grid = grid;
  }
}
