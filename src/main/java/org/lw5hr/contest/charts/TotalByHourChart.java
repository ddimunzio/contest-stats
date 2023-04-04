package org.lw5hr.contest.charts;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TotalByHourChart extends Application  {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(TotalByHourChart.class.getResource("total-by-hour-chart.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 300);
        stage.setScene(scene);
        stage.show();
    }
}
