package org.lw5hr.contest.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.model.Qso;
import org.lw5hr.contest.utils.StatsUtil;

import java.net.URL;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;

public class ByBandAndOperatorController extends GenericStackedBarCharController{
  private CheckBox invert;
  @FXML
  private  StackedBarChart<String, String> totalByBandAndOp;

  private final ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", MainWindow.getLocale());

  public ByBandAndOperatorController() {
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    QueryUtil q = MainWindow.getQueryUtil();
    Long selectedContest = q.getSelectedContest();
    List<Qso> qsos = q.getQsoByContest(selectedContest);
    StatsUtil st = new StatsUtil();
    ObservableList<XYChart.Series<String, Integer>> byBandAndOperator = st.getTotalsByTwoParameters(qsos, Qso::getBand, Qso::getOperator);

    String titleLabel = mainResources.getString("key.main.menu.charts.total.by.hour");
    titleLabel = titleLabel + " - " + q.getContest(selectedContest).getContestProperties().getEventName() + " - "
                + q.getContest(selectedContest).getDateFrom().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
    chart.setTitle(titleLabel);
    chart.setData(byBandAndOperator);
    addLabelsToChart(chart);
  }

  @FXML
  @Override
  protected void addLabelsToChart(StackedBarChart<String, Integer> chart) {
    for (final XYChart.Series<String, Integer> series : chart.getData()) {
      int total = 0;
      for (final XYChart.Data<String, Integer> data : series.getData()) {
        total += data.getYValue();
      }
      Text totalLabel = new Text("Total: " + total);
      totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));
      totalLabel.setFill(Color.BLACK);
      totalLabel.setTextAlignment(TextAlignment.CENTER);
      StackPane.setAlignment(totalLabel, Pos.CENTER);
      for (int i = 0; i < series.getData().size(); i++) {
        XYChart.Data<String, Integer> data = series.getData().get(i);
        StackPane stackPane = (StackPane) data.getNode();
        stackPane.getChildren().add(totalLabel);
        Bounds textBounds = totalLabel.getLayoutBounds();
        totalLabel.setTranslateX(series.getData().get(i).getXValue().length());
        totalLabel.setTranslateY(-textBounds.getHeight() / 2 - i * textBounds.getHeight());
      }
    }

    for (final XYChart.Series<String, Integer> series : chart.getData()) {
      for (final XYChart.Data<String, Integer> data : series.getData()) {
        StackPane stackPane = (StackPane) data.getNode();
        if (data.getYValue()!= 0) {
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


  public void handleInvert(final ActionEvent event) {
    QueryUtil q = MainWindow.getQueryUtil();
    Long selectedContest = q.getSelectedContest();
    List<Qso> qsos = q.getQsoByContest(selectedContest);
    ObservableList<XYChart.Series<String, Integer>> byBandAndOperator;
    StatsUtil st = new StatsUtil();
    CheckBox cb = (CheckBox) event.getSource();

    if (cb.isSelected()) {
       byBandAndOperator = st.getTotalsByTwoParameters(qsos, Qso::getOperator, Qso::getBand);
    } else {
       byBandAndOperator = st.getTotalsByTwoParameters(qsos, Qso::getBand, Qso::getOperator);
    }

    chart.setData(byBandAndOperator);
    addLabelsToChart(chart);
  }
}
