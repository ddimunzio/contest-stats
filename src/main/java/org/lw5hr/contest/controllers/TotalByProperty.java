package org.lw5hr.contest.controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.model.Qso;
import org.lw5hr.contest.utils.StatsUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

public class TotalByProperty extends GenericBarchartController implements Initializable {
  private final ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", MainWindow.getLocale());
  @FXML
  private VBox container;

  @FXML
  CategoryAxis xAxis;

  @FXML
  NumberAxis yAxis;
  @FXML
  BarChart<String, Integer> chart;

  private final Function<Qso, String> function;

  private final String translationKey;

  public TotalByProperty(final String property) {
    Function<Qso, String> OPEARATOR_FUNCTION = Qso::getOperator;
    Function<Qso, String> BAND_FUNCTION = Qso::getBand;
    switch (property) {
      case "totalByBand" -> {
        function = BAND_FUNCTION;
        this.translationKey = "key.main.menu.charts.total.by.band";
      }
      case "totalByOperator" -> {
        function = OPEARATOR_FUNCTION;
        this.translationKey = "key.main.menu.charts.total.by.op";
      }
      default -> throw new IllegalArgumentException("Unknown field: " + property);
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    QueryUtil q = MainWindow.getQueryUtil();
    Long selectedContest = q.getSelectedContest();
    List<Qso> qsos = q.getQsoByContest(selectedContest);
    StatsUtil st = new StatsUtil();
    ObservableList<XYChart.Series<String, Integer>> chartData = st.getTotalsByParameter(qsos, function);
    String titleLabel = mainResources.getString(translationKey);
    titleLabel = titleLabel + " - " + q.getContest(selectedContest).getContestName();
    chart.setTitle(titleLabel);
    chart.setData(chartData);
    chart.setAnimated(false);
    xAxis.setTickLabelsVisible(false);
    addLabelsToChart(chart);

    Platform.runLater(() -> {
      container.setFillWidth(true);
      chart.prefWidthProperty().bind(container.widthProperty());
      chart.prefHeightProperty().bind(container.heightProperty());
    });

    // Set the width of the bars
    double barWidth = 1;

// Loop through the series and set the width of the bars
    for (XYChart.Series<String, Integer> series : chartData) {
      for (XYChart.Data<String, Integer> data : series.getData()) {
        Node bar = data.getNode();
        if (bar != null) {
          bar.setStyle("-fx-bar-width: " + barWidth + ";");
        }
      }
    }

    // Adjust the space between the bars using CSS
    String chartStyle = "-fx-category-gap: " + (barWidth - 1) + ";";
    container.setStyle(chartStyle);

  }


  @Override
  protected void addLabelsToChart(BarChart<String, Integer> chart) {

    for (XYChart.Series<String, Integer> series : chart.getData()) {
      for (XYChart.Data<String, Integer> data : series.getData()) {
        // add label above bar
        Label label = new Label(data.getYValue().toString());
        label.setTextFill(Color.BLACK);
        StackPane stackPane = (StackPane) data.getNode();
        stackPane.getChildren().add(label);
        StackPane.setAlignment(label, Pos.TOP_CENTER);
        StackPane.setMargin(label, new Insets(-20, 0, 0, 0));

        // add rotated text inside bar if number of contacts is greater than 100
        if (data.getYValue() > 100) {
          Text text = new Text(data.getXValue());
          text.setFill(Color.WHITE);
          text.setFont(Font.font("Arial", FontWeight.BOLD, 9));
          StackPane rotatedStackPane = new StackPane();
          rotatedStackPane.setRotate(270);
          rotatedStackPane.getChildren().add(text);
          StackPane.setAlignment(rotatedStackPane, Pos.CENTER);
          double width = stackPane.getBoundsInLocal().getWidth();
          double height = stackPane.getBoundsInLocal().getHeight();
          StackPane.setMargin(rotatedStackPane, new Insets((width - height) / 2, 0, 0, 0));
          stackPane.getChildren().add(rotatedStackPane);
        }
      }
    }
  }

  public String getTranslationKey() {
    return translationKey;
  }
}
