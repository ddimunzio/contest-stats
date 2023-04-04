package org.lw5hr.contest.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChangeScenes {
  public static void moveToCenter(URL ui, AnchorPane panel){
    try {
      Parent root = FXMLLoader.load(ui);
      panel.autosize();
      panel.setBottomAnchor(root, 5d);
    } catch (IOException ex) {
      Logger.getLogger(ChangeScenes.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
