package org.lw5hr.contest.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.lw5hr.contest.main.MainWindow;

public class ByHourAndOperatorAreaChart extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("by-hour-and-operator-area-chart.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 1000);
        stage.setScene(scene);
        stage.show();
    }
}
