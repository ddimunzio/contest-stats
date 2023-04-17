package org.lw5hr.contest.main;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class ImportContest extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Locale loc = MainWindow.getLocale();
        ResourceBundle importResources = ResourceBundle.getBundle("i18n/import", loc);
        FXMLLoader fxmlLoader = new FXMLLoader(ImportContest.class.getResource("import-adif.fxml"), importResources);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle(importResources.getString("key.import.add.new"));
        stage.show();
    }
}