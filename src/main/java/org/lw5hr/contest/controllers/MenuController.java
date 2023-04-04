package org.lw5hr.contest.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.lw5hr.contest.charts.ByHourAndOperatorAreaChart;
import org.lw5hr.contest.charts.ByHourAndOperatorChart;
import org.lw5hr.contest.charts.TotalByHourChart;
import org.lw5hr.contest.charts.TotalByOperatorChart;
import org.lw5hr.contest.main.ChangeScenes;
import org.lw5hr.contest.main.ImportContest;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.model.Qso;
import org.lw5hr.contest.utils.ADIFReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class MenuController extends BorderPane implements Initializable {
  @FXML
  private MenuBar menuBar;

  @FXML
  private AnchorPane mainPanel;

  @FXML
  private void handleImportAdifAction(final ActionEvent event) throws Exception {
    Stage importStage = new Stage();
    ImportContest ic = new ImportContest();
    ic.start(importStage);

    /*String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(currentPath));
    fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Text Files", "*.ADI")
            , new FileChooser.ExtensionFilter("HTML Files", "*.ADIF")
    );
    File adiFile = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
    if (adiFile != null) {
      try {
        ADIFReader adiReader = new ADIFReader(adiFile.getPath());
        List<Qso> qsos = adiReader.read();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }*/

  }

  @FXML
  private void handleTotalByOpAction(final ActionEvent event) throws Exception {
    Stage byOpStage = new Stage();
    TotalByOperatorChart to = new TotalByOperatorChart();
    to.start(byOpStage);
  }

  @FXML
  private void handleTotalByHourAction(final ActionEvent event) throws Exception {
    Stage byHourStage = new Stage();
    TotalByHourChart th = new TotalByHourChart();
    th.start(byHourStage);
  }

  @FXML
  private void handleQsosByHourAndOperator(final ActionEvent event) throws Exception {
    Stage stage = new Stage();
    ByHourAndOperatorChart  byHourAndOperator = new ByHourAndOperatorChart();
    byHourAndOperator.start(stage);
  }

  @FXML
  private void handleQsosByHourAndOperatorArea(final  ActionEvent event) throws Exception {
    Stage stage = new Stage();
    ByHourAndOperatorAreaChart byHourAndOperatorArea = new ByHourAndOperatorAreaChart();
    byHourAndOperatorArea.start(stage);
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

  }

}
