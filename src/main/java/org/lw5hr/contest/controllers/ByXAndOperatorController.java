package org.lw5hr.contest.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
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
import java.util.function.Function;

public class ByXAndOperatorController extends GenericStackedBarCharController implements Initializable {
  private final Function<Qso, String> BAND_FUNCTION = Qso::getBand;
  private final Function<Qso, String> function;

  public ByXAndOperatorController(final String field) {
    Function<Qso, String> OPEARATOR_FUNCTION = Qso::getOperator;
    switch (field) {
      case "band" -> function = BAND_FUNCTION;
      case "operator" -> function = OPEARATOR_FUNCTION;
      default -> throw new IllegalArgumentException("Unknown field: " + field);
    }
  }

  private final ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", MainWindow.getLocale());
  @FXML
  CategoryAxis xAxis;

  @FXML
  NumberAxis yAxis;

  @FXML
  StackedBarChart<String, Integer> chart;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    QueryUtil q = MainWindow.getQueryUtil();
    Long selectedContest = q.getSelectedContest();
    List<Qso> qsos = q.getQsoByContest(selectedContest);
    StatsUtil st = new StatsUtil();
    ObservableList<XYChart.Series<String, Integer>> byHourAndOpData = st.getByHourAndX(qsos, function);

    String titleLabel = mainResources.getString("key.main.menu.charts.total.by.hour");
    titleLabel = titleLabel + " - " + q.getContest(selectedContest).getContestProperties().getEventName() + " - "
                + q.getContest(selectedContest).getDateFrom().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
    chart.setTitle(titleLabel);
    chart.setData(byHourAndOpData);
    addLabelsToChart(chart);
  }

  @FXML
  @Override
  protected void addLabelsToChart(StackedBarChart<String, Integer> chart) {
    for (final XYChart.Series<String, Integer> series : chart.getData()) {
      for (final XYChart.Data<String, Integer> data : series.getData()) {
        StackPane stackPane = (StackPane) data.getNode();
        if (data.getYValue()!= 0) {
          Text label = new Text(data.getYValue().toString());
          label.setFont(Font.font("Arial", FontWeight.BOLD, 10));
          label.setFill(Color.WHITE);
          label.setTextAlignment(TextAlignment.CENTER);
          stackPane.getChildren().add(label);
          StackPane.setAlignment(label, Pos.BOTTOM_CENTER); // adjust alignment
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
