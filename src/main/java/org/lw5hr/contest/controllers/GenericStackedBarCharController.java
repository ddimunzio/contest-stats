package org.lw5hr.contest.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;

import org.lw5hr.contest.main.MainWindow;
import java.util.ResourceBundle;

public abstract class GenericStackedBarCharController implements Initializable {
  private final ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", MainWindow.getLocale());
  @FXML
  CategoryAxis xAxis ;
  @FXML
  NumberAxis yAxis;
  @FXML
  StackedBarChart<String, Integer> chart;


  protected abstract void addLabelsToChart(StackedBarChart<String, Integer> chart);
}
