package org.lw5hr.contest.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.util.Duration;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.model.Contest;
import org.lw5hr.contest.utils.StatsUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class CompareContestController extends GenericBarchartController implements Initializable {
  @FXML
  private BarChart<String, Integer> chart;

  @FXML
  private ComboBox<Contest> contestOne;

  @FXML
  private ComboBox<Contest> contestTwo;

  ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", MainWindow.getLocale());
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    QueryUtil qu = MainWindow.getQueryUtil();
    ObservableList<Contest> cl = qu.getContestList();
    cl.forEach(c -> {
      contestOne.getItems().add(c);
      contestTwo.getItems().add(c);
    });
    contestOne.setPlaceholder(new Label(resourceBundle.getString("key.main.charts.compare.select")));
    contestTwo.setPlaceholder(new Label(resourceBundle.getString("key.main.charts.compare.select")));
    // Set the preferred size of the chart with a 10-pixel margin on each side
    double screenWidth = Screen.getPrimary().getBounds().getWidth();
    double screenHeight = Screen.getPrimary().getBounds().getHeight();
    double chartWidth = screenWidth - 20;  // Subtract 20 for left and right margin
    double chartHeight = screenHeight - 200;  // Subtract 200 for top and bottom margin
    chart.setPrefWidth(chartWidth);
    chart.setPrefHeight(chartHeight);

    // Set padding to create a margin around the chart
    Insets padding = new Insets(10);
    chart.setPadding(padding);
  }

  public void handleContestOneSelected(ActionEvent event) {
    StatsUtil st = new StatsUtil();
    final ComboBox<Contest> cb = (ComboBox<Contest>) event.getSource();
    st.getTotalsByHour(cb.getSelectionModel().getSelectedItem().getQsos());
    ObservableList<XYChart.Series<String, Integer>> data =  FXCollections.observableArrayList();
    data.addAll(st.getTotalsByHour(cb.getSelectionModel().getSelectedItem().getQsos()));
    contestTwo.getItems().remove(cb.getSelectionModel().getSelectedItem());
    chart.setData(data);
    addLabelsToChart(chart);
  }

  public void handleContestTwoSelected(ActionEvent event) {
    StatsUtil st = new StatsUtil();
    final ComboBox<Contest> cb = (ComboBox<Contest>) event.getSource();
    ObservableList<XYChart.Series<String, Integer>> data =  FXCollections.observableArrayList();
    data.addAll(st.getTotalsByHour(cb.getSelectionModel().getSelectedItem().getQsos()));
    chart.getData().addAll(data);
    contestOne.getItems().remove(cb.getSelectionModel().getSelectedItem());
    addLabelsToChart(chart);
  }

  @Override
  protected void addLabelsToChart(BarChart<String, Integer> chart) {
    for (final XYChart.Series<String, Integer> series : chart.getData()) {
      for (final XYChart.Data<String, Integer> data : series.getData()) {
        StackPane stackPane = (StackPane) data.getNode();
        if (data.getYValue() != 0D) {
          Text label = new Text(data.getYValue().toString());
          label.setFont(Font.font("Arial", FontWeight.BOLD, 10));
          label.setFill(Color.WHITE);
          label.setTextAlignment(TextAlignment.CENTER);
          stackPane.getChildren().add(label);
          StackPane.setAlignment(label, Pos.CENTER);
          // create and set tooltip
          String seriesName = null;
          for (XYChart.Series<String, Integer> s : chart.getData()) {
            if (s.getData().contains(data)) {
              seriesName = s.getName();
              break;
            }
          }
          String tooltipText = seriesName;
          Tooltip tooltip = new Tooltip(tooltipText);
          Tooltip.install(stackPane, tooltip);

          // delay showing tooltip and hide after a delay on mouse exit
          final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), event -> {
            Point2D p = stackPane.localToScreen(stackPane.getBoundsInLocal().getMaxX(), stackPane.getBoundsInLocal().getMaxY());
            tooltip.show(stackPane, p.getX(), p.getY());
          }));
          timeline.setCycleCount(1);

          stackPane.setOnMouseEntered(event -> {
            timeline.playFromStart();
          });

          stackPane.setOnMouseExited(event -> {
            timeline.stop();
            timeline.setOnFinished(finishEvent -> {
              tooltip.hide();
            });
            timeline.play();
          });
        }
      }
    }
  }
}
