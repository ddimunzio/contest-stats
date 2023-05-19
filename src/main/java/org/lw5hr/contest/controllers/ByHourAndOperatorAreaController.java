package org.lw5hr.contest.controllers;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.model.Qso;
import org.lw5hr.contest.utils.StatsUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ByHourAndOperatorAreaController extends Application {

  @Override
  public void start(Stage primaryStage) {
    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();
    webEngine.load(getClass().getResource("echarts_area_chart.html").toString());

    webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue == Worker.State.SUCCEEDED) {
        JSObject window = (JSObject) webEngine.executeScript("window");
        window.setMember("JavaApp", new JavaApp());

        // You can pass data from Java to JavaScript here
        QueryUtil q = MainWindow.getQueryUtil();
        List<Qso> qsos = q.getQsoByContest(q.getSelectedContest());
        StatsUtil st = new StatsUtil();
        List<DataItem> data = new ArrayList<>();
        ObservableList<XYChart.Series<String, Integer>> seriesData = st.getByHourAndX(qsos, Qso::getOperator);
        List<String> xValues = new ArrayList<>();
        List<String> seriesNames = new ArrayList<>();

        for (XYChart.Series<String, Integer> series : seriesData) {
          List<Integer> seriesDataValues = new ArrayList<>();
          for (XYChart.Data<String, Integer> seriesDatum : series.getData()) {
            xValues.add(seriesDatum.getXValue());
            seriesDataValues.add(seriesDatum.getYValue());
          }
          data.addAll(seriesDataValues.stream().map(y -> new DataItem("", y)).collect(Collectors.toList()));
          seriesNames.add(series.getName());
        }

        webEngine.executeScript("var xValues = " + convertLabelsToJavaScriptArray(xValues) + ";");
        webEngine.executeScript("var seriesNames = " + convertLabelsToJavaScriptArray(seriesNames) + ";");
        webEngine.executeScript("var data = " + convertDataToJavaScriptArray(data) + ";");
        webEngine.executeScript("createAreaChart(xValues, seriesNames, data);");
      }
    });

    Scene scene = new Scene(webView, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private String convertDataToJavaScriptArray(List<DataItem> data) {
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < data.size(); i++) {
      DataItem item = data.get(i);
      sb.append(item.getYValue());
      if (i < data.size() - 1) {
        sb.append(", ");
      }
    }
    sb.append("]");
    return sb.toString();
  }

  private String convertLabelsToJavaScriptArray(List<String> labels) {
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < labels.size(); i++) {
      String label = labels.get(i);
      sb.append("'").append(label).append("'");
      if (i < labels.size() - 1) {
        sb.append(", ");
      }
    }
    sb.append("]");
    return sb.toString();
  }




  public static void main(String[] args) {
    launch(args);
  }

  public class JavaApp {
    // Method that can be called from JavaScript
    public void handleChartClick(String category, int value) {
      System.out.println("Clicked on category: " + category + ", value: " + value);
    }
  }

  public class DataItem {
    private String xValue;
    private int yValue;

    public DataItem(String xValue, int yValue) {
      this.xValue = xValue;
      this.yValue = yValue;
    }

    public String getXValue() {
      return xValue;
    }

    public int getYValue() {
      return yValue;
    }
  }
}
