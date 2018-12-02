package util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class Logger {

  private static List<String> logs;
  private static Logger instance = new Logger();
  private static PropertyChangeSupport support;

  private Logger() {
    logs = new ArrayList<>();
    support = new PropertyChangeSupport(this);
  }

  public static void  addPropertyChangeListener(PropertyChangeListener pcl) {
    support.addPropertyChangeListener(pcl);
  }

  public static void removePropertyChangeListener(PropertyChangeListener pcl) {
    support.removePropertyChangeListener(pcl);
  }

  public static void log(String logEntry) {
    logs.add(logEntry);
    support.firePropertyChange("logs", logs, logEntry);
    //System.out.println(logEntry);

  }
}
