package org.lw5hr.contest.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import org.lw5hr.contest.main.MainWindow;

import java.util.ResourceBundle;

public abstract class GenericBarchartController {

  private final ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", MainWindow.getLocale());

  protected abstract void addLabelsToChart(BarChart<String, Integer> chart);
}
