package org.lw5hr.contest.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
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

public class ChartTotalByOpController implements Initializable {
    private final ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", MainWindow.getLocale());

    @FXML
    CategoryAxis xAxis ;
    @FXML
    NumberAxis yAxis;
    @FXML
    BarChart<String, Integer> totalByOp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        QueryUtil q = MainWindow.getQueryUtil();
        Long selectedContest = q.getSelectedContest();
        List<Qso> qsos = q.getQsoByContest(selectedContest);
        StatsUtil st = new StatsUtil();
        ObservableList<XYChart.Series<String, Integer>> byOpData = st.getTotalsByOp(qsos);
        String titleLabel = mainResources.getString("key.main.menu.charts.total.by.op");
        titleLabel = titleLabel + " - " + q.getContest(selectedContest).getContestName();
        totalByOp.setTitle(titleLabel);
        totalByOp.setData(byOpData);
        totalByOp.setBarGap(0);
        totalByOp.setAnimated(false);
        totalByOp.setCategoryGap(0);
        xAxis.setTickLabelsVisible(false);
        addLabelsToBars();
    }

    public void addLabelsToBars() {
        for (XYChart.Series<String, Integer> series : totalByOp.getData()) {
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
                    text.setFont(Font.font("Arial", FontWeight.BOLD, 10));
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

}
