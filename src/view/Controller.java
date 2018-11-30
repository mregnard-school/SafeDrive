package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

  public Controller() {
    Logger.addPropertyChangeListener(this);
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
    System.out.println("Stopping");
    
    loop.interrupt();
    loop = null;
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
    //Simulation updated yeah
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
