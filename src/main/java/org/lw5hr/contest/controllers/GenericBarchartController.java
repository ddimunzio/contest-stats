package org.lw5hr.contest.controllers;

import javafx.scene.chart.BarChart;
import org.lw5hr.contest.main.MainWindow;

import java.util.ResourceBundle;

public abstract class GenericBarchartController {

  private final ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", MainWindow.getLocale());

  protected abstract void addLabelsToChart(BarChart<String, Integer> chart);
}
