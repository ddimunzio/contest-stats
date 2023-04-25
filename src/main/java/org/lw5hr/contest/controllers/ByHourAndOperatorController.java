package org.lw5hr.contest.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.model.Qso;
import org.lw5hr.contest.utils.StatsUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ByHourAndOperatorController implements Initializable {

    CategoryAxis xAxis ;
    @FXML
    NumberAxis yAxis;
    @FXML
    StackedBarChart<String, Integer> byHourAndOperator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        QueryUtil q = MainWindow.getQ();
        List<Qso> qsos = q.getQsoByContest(q.getSelectedContest());
        StatsUtil st = new StatsUtil();
        ObservableList<XYChart.Series<String, Integer>> byHourAndOpData = st.getByHourAndOperator(qsos);
        byHourAndOperator.setData(byHourAndOpData);
    }
}
