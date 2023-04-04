package org.lw5hr.contest.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.model.Qso;
import org.lw5hr.contest.utils.StatsUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChartTotalByHourController implements Initializable {
    CategoryAxis xAxis ;
    @FXML
    NumberAxis yAxis;
    @FXML
    BarChart<String, Integer> totalByHour;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        QueryUtil queryUtil = new QueryUtil();
        List<Qso> qsos = queryUtil.getQsoByContest(1L);
        StatsUtil st = new StatsUtil();
        ObservableList<XYChart.Series<String, Integer>> byHourData = st.getTotalsByHour(qsos);
        totalByHour.setData(byHourData);
    }
}
