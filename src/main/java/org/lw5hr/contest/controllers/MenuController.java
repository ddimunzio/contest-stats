package org.lw5hr.contest.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.lw5hr.contest.charts.ByHourAndOperatorAreaChart;
import org.lw5hr.contest.charts.ByHourAndOperatorChart;
import org.lw5hr.contest.charts.TotalByHourChart;
import org.lw5hr.contest.charts.TotalByOperatorChart;
import org.lw5hr.contest.main.ContestManager;
import org.lw5hr.contest.main.ImportContest;
import org.lw5hr.contest.main.MainWindow;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MenuController extends BorderPane implements Initializable {
  @FXML
  private RadioMenuItem en;

  @FXML
  private RadioMenuItem es;

  @FXML
  private MenuBar menuBar;

  @FXML
  private void handleImportAdifAction(final ActionEvent event) throws Exception {
    Stage importStage = new Stage();
    ImportContest ic = new ImportContest();
    ic.start(importStage);
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
    ByHourAndOperatorChart byHourAndOperator = new ByHourAndOperatorChart();
    byHourAndOperator.start(stage);
  }

  @FXML
  private void handleQsosByHourAndOperatorArea(final ActionEvent event) throws Exception {
    Stage stage = new Stage();
    ByHourAndOperatorAreaChart byHourAndOperatorArea = new ByHourAndOperatorAreaChart();
    byHourAndOperatorArea.start(stage);
  }

  @FXML
  private void handleLanguage(final ActionEvent event) throws IOException {
    EventTarget s = event.getTarget();
    String id = ((RadioMenuItem) event.getSource()).getId();
    Locale locale = null;
    switch (id) {
      case "en":
        MainWindow.setLocale(new Locale("en", "US"));
        setSelectedLanguage(id);
        break;
      case "es":
        MainWindow.setLocale(new Locale("es", "ES"));
        setSelectedLanguage(id);
        break;
    }
  }

  @FXML
  private void handleSelectContest(final ActionEvent event) throws Exception {
    Stage stage = new Stage();
    ContestManager contestManager = new ContestManager();
    contestManager.start(stage);
  }

  @FXML
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    String lang = MainWindow.getLocale().getLanguage();
    setSelectedLanguage(lang);
  }

  private void setSelectedLanguage(String lang) {
      en.setSelected(en.getId().equalsIgnoreCase(lang));
      es.setSelected(es.getId().equalsIgnoreCase(lang));

  }
}
