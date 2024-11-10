package org.lw5hr.contest.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
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
import org.lw5hr.contest.model.Qso;
import org.lw5hr.contest.utils.StatsUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Author: Diego Dimunzio - LW5HR   
 */

public class TotalByHourController extends GenericBarchartController implements Initializable {
    @FXML
    BarChart<String, Integer> chart;

    @FXML
    NumberAxis numberAxis;

    @FXML
    CategoryAxis categoryAxis;

    @FXML
    private Button exportButton;

    private final ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", MainWindow.getLocale());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        QueryUtil q = MainWindow.getQueryUtil();
        /** Get all Qso's for a specific contest */
        Long selectedContest = q.getSelectedContest();
        List<Qso> qsos = q.getQsoByContest(selectedContest);
        StatsUtil st = new StatsUtil();
        ObservableList<XYChart.Series<String, Integer>> byHourData = st.getTotalsByHour(qsos);
        String titleLabel = mainResources.getString("key.main.menu.charts.total.by.hour");
        titleLabel = titleLabel + " - " + q.getContest(selectedContest).getContestProperties().getEventName() + " - "
                + q.getContest(selectedContest).getDateFrom().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
        chart.setTitle(titleLabel);
        chart.setData(byHourData);
        chart.setBarGap(2);
        addLabelsToChart(chart);

        // Set the preferred size of the chart with a 10-pixel margin on each side
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        double chartWidth = screenWidth - 20;  // Subtract 20 for left and right margin
        double chartHeight = screenHeight - 20;  // Subtract 20 for top and bottom margin
        chart.setPrefWidth(chartWidth);
        chart.setPrefHeight(chartHeight);

        // Set padding to create a margin around the chart
        Insets padding = new Insets(10);
        chart.setPadding(padding);
        exportButton.setOnAction(event -> exportChartToPNG(chart, "chart.png"));

    }

    @FXML
    @Override
    protected void addLabelsToChart(BarChart<String, Integer> chart) {
        for (final XYChart.Series<String, Integer> series : chart.getData()) {
            for (final XYChart.Data<String, Integer> data : series.getData()) {
                StackPane stackPane = (StackPane) data.getNode();
                if (data.getYValue() != 0) {
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

    public void exportChartToPNG(BarChart<String, Integer> chart, String filePath) {
        WritableImage image = chart.snapshot(new SnapshotParameters(), null);
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        PixelReader pixelReader = image.getPixelReader();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int argb = pixelReader.getArgb(x, y);
                bufferedImage.setRGB(x, y, argb);
            }
        }

        File file = new File(filePath);
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
