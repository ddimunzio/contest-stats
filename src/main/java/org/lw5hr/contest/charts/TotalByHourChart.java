package org.lw5hr.contest.charts;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.lw5hr.contest.main.MainWindow;

import java.util.ResourceBundle;

public class TotalByHourChart extends Application  {

    @Override
    public void start(Stage stage) throws Exception {
        ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", MainWindow.getLocale());
        FXMLLoader fxmlLoader = new FXMLLoader(TotalByHourChart.class.getResource("total-by-hour-chart.fxml"), mainResources);
        Scene scene = new Scene(fxmlLoader.load(), 700, 300);
        stage.setScene(scene);
        stage.show();
    }
}
