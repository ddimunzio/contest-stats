package org.lw5hr.contest.main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.util.ResourceBundle;

/**
 * Author: Diego Dimunzio - LW5HR   
 */

public class ContestManager extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", MainWindow.getLocale());
    FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("contest-table.fxml"), mainResources, new JavaFXBuilderFactory());
    Scene scene = new Scene(fxmlLoader.load());
    stage.setScene(scene);
    stage.setResizable(false);
    stage.setTitle(mainResources.getString("key.contest.windows.tittle"));
    stage.show();
  }
}
