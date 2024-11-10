package org.lw5hr.contest.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.model.Qso;
import org.lw5hr.contest.utils.StatsUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Author: Diego Dimunzio - LW5HR   
 */

public class ByHourAndOperatorAreaController implements Initializable {
  @FXML
  private CategoryAxis categoryAxis;

  @FXML
  private NumberAxis numberAxis;

  @FXML
  StackedAreaChart<String, Integer> byHourAndOperatorArea;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    QueryUtil q = MainWindow.getQueryUtil();
    List<Qso> qsos = q.getQsoByContest(q.getSelectedContest());
    StatsUtil st = new StatsUtil();
    ObservableList<XYChart.Series<String, Integer>> byHourAndOpAreaData = st.getByHourAndX(qsos, Qso::getOperator);
    numberAxis.setForceZeroInRange(true);
    byHourAndOperatorArea.setPrefSize(800, 600);
    byHourAndOperatorArea.getData().addAll(byHourAndOpAreaData);

  }
}
