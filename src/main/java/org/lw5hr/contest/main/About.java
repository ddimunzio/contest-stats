package org.lw5hr.contest.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class About extends Application {
  @Override
  public void start(Stage aboutStage) throws Exception {
    Locale loc = MainWindow.getLocale();
    ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", loc);
    FXMLLoader fxmlLoader = new FXMLLoader(About.class.getResource("about-form.fxml"), mainResources);
    Scene scene = new Scene(fxmlLoader.load());
    aboutStage.setScene(scene);
    aboutStage.setResizable(false);
    aboutStage.setTitle(mainResources.getString("main.menu.help.about.tittle"));
    aboutStage.show();
  }
}
