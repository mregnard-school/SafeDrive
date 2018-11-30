package util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class Logger {

  private List<String> logs;
  private static Logger instance;
  private  PropertyChangeSupport  support;

  private Logger() {
    logs = new ArrayList<>();
    support = new PropertyChangeSupport(this);
  }

  public static void  addPropertyChangeListener(PropertyChangeListener pcl) {
    Logger.getLogger().support.addPropertyChangeListener(pcl);
  }

  public static void removePropertyChangeListener(PropertyChangeListener pcl) {
    Logger.getLogger().support.removePropertyChangeListener(pcl);
  }

  public static Logger getLogger() {
    if(Logger.instance == null) {
      Logger.instance =  new Logger();
    }
    return Logger.instance;
  }

  public void log(String logEntry) {
    this.logs.add(logEntry);
    support.firePropertyChange("logs", this.logs, logEntry);
  }
}
