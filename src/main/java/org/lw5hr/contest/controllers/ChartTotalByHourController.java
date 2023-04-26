package org.lw5hr.contest.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.model.Qso;
import org.lw5hr.contest.utils.StatsUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChartTotalByHourController implements Initializable {
    @FXML
    BarChart<String, Integer> totalByHour;

    @FXML
    NumberAxis numberAxis;

    @FXML
    CategoryAxis categoryAxis;

    private final ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", MainWindow.getLocale());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        QueryUtil q = MainWindow.getQ();

        /** Get all Qso's for a specific contest */
        Long selectedContest = q.getSelectedContest();
        List<Qso> qsos = q.getQsoByContest(selectedContest);
        StatsUtil st = new StatsUtil();
        ObservableList<XYChart.Series<String, Integer>> byHourData = st.getTotalsByHour(qsos);
        String titleLabel = mainResources.getString("key.main.menu.charts.total.by.hour");
        titleLabel = titleLabel + " - " + q.getContest(selectedContest).getContestName();
        totalByHour.setTitle(titleLabel);
        totalByHour.setData(byHourData);
    }
}
