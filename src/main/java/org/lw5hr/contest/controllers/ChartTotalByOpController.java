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

public class ChartTotalByOpController implements Initializable {
    private final ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", MainWindow.getLocale());

    CategoryAxis xAxis ;
    @FXML
    NumberAxis yAxis;
    @FXML
    BarChart<String, Integer> totalByOp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        QueryUtil q = MainWindow.getQ();
        Long selectedContest = q.getSelectedContest();
        List<Qso> qsos = q.getQsoByContest(selectedContest);
        StatsUtil st = new StatsUtil();
        ObservableList<XYChart.Series<String, Integer>> byOpData = st.getTotalsByOp(qsos);
        String titleLabel = mainResources.getString("key.main.menu.charts.total.by.op");
        titleLabel = titleLabel + " - " + q.getContest(selectedContest).getContestName();
        totalByOp.setTitle(titleLabel);
        totalByOp.setAnimated(false);
        totalByOp.setData(byOpData);
    }
}
