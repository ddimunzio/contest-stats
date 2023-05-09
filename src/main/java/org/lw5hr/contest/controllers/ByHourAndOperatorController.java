package org.lw5hr.contest.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.model.Qso;
import org.lw5hr.contest.utils.StatsUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ByHourAndOperatorController implements Initializable {
    private final ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", MainWindow.getLocale());
    @FXML
    CategoryAxis xAxis ;
    @FXML
    NumberAxis yAxis;

    @FXML
    StackedBarChart<String, Integer> byHourAndOperator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        QueryUtil q = MainWindow.getQ();
        Long selectedContest = q.getSelectedContest();
        List<Qso> qsos = q.getQsoByContest(selectedContest);
        StatsUtil st = new StatsUtil();
        ObservableList<XYChart.Series<String, Integer>> byHourAndOpData = st.getByHourAndOperator(qsos);
        String titleLabel = mainResources.getString("key.main.menu.charts.total.by.hour");
        titleLabel = titleLabel + " - " + q.getContest(selectedContest).getContestName();
        byHourAndOperator.setTitle(titleLabel);
        byHourAndOperator.setData(byHourAndOpData);
        addLabelsToChart(byHourAndOperator);
    }

    @FXML
    private void addLabelsToChart(StackedBarChart<String, Integer> chart) {
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
                }
            }
        }
    }
}
