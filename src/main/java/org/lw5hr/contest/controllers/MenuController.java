package org.lw5hr.contest.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.lw5hr.contest.charts.ByHourAndOperatorAreaChart;
import org.lw5hr.contest.charts.TotalByHourChart;
import org.lw5hr.contest.charts.TotalByOperatorChart;
import org.lw5hr.contest.db.DatabaseConstants;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.main.About;
import org.lw5hr.contest.main.ContestManager;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.model.Contest;
import org.lw5hr.contest.model.Settings;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.lw5hr.contest.main.MainWindow.getQueryUtil;

public class MenuController extends BorderPane implements Initializable {
  @FXML
  private RadioMenuItem liveContestMenu;
  @FXML
  private Menu menuContestList;

  @FXML
  private RadioMenuItem en;

  @FXML
  private RadioMenuItem es;

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
    Locale loc = MainWindow.getLocale();
    ResourceBundle resources = ResourceBundle.getBundle("i18n/main", loc);
    ByHourAndOperatorController controller = new ByHourAndOperatorController();
    FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("generic-stacked-bar-chart.fxml"), resources);
    fxmlLoader.setController(controller);
    Parent root;
    try {
      root = fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Stage stage = new Stage();
    stage.setScene(new Scene(root, 1600, 768));
    stage.showAndWait();
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
        MainWindow.setLocale(Locale.of("en", "US"));
        setSelectedLanguage(id);
        break;
      case "es":
        MainWindow.setLocale(Locale.of("es", "ES"));
        setSelectedLanguage(id);
        break;
    }
  }

  @FXML
  private void handleManageContest(final ActionEvent event) throws Exception {
    Stage stage = new Stage();
    ContestManager contestManager = new ContestManager();
    contestManager.start(stage);
  }

  @FXML
  private void handleSelectContest(final ActionEvent event) throws Exception {
    QueryUtil q = getQueryUtil();
    final RadioMenuItem source = (RadioMenuItem) event.getSource();
    q.updateSetting(DatabaseConstants.CURRENT_CONTEST, source.getId());
  }

  @FXML
  private void handleAbout(final ActionEvent event) throws Exception {
    Stage stage = new Stage();
    About about = new About();
    about.start(stage);
  }

  private void addContestItems() {
    QueryUtil q = getQueryUtil();
    Long selectedContest = q.getSelectedContest();
    List<Contest> contestList = q.getContestList();
    contestList.forEach(c -> {
      RadioMenuItem r = new RadioMenuItem(c.getContestName());
      r.setId(c.getId().toString());
      r.setSelected(c.getId().equals(selectedContest));
      r.setOnAction(event -> {
        try {
          handleSelectContest(event);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
      menuContestList.getItems().add(r);
    });

  }

  @FXML
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    String lang = MainWindow.getLocale().getLanguage();
    addContestItems();
    setSelectedLanguage(lang);
    Optional<Settings> liveContest = getQueryUtil().getSetting(DatabaseConstants.LIVE_CONTEST_ON);
    if (liveContest.isPresent() && liveContest.get().getSettingValue().equals("true")) {
      liveContestMenu.setSelected(true);
    }
  }

  private void setSelectedLanguage(String lang) {
    en.setSelected(en.getId().equalsIgnoreCase(lang));
    es.setSelected(es.getId().equalsIgnoreCase(lang));

  }

  /**
   * This method is called when the menu is shown.  It is used to update the list of contests.
   * @param event not used but required by JavaFX
   */
  public void handleShow(Event event) {
    menuContestList.getItems().clear();
    addContestItems();
  }
  @FXML
  private void handleImportAdifAction(final ActionEvent event) throws Exception {
    Locale loc = MainWindow.getLocale();
    ResourceBundle resources = ResourceBundle.getBundle("i18n/import", loc);
    ImportController importController = new ImportController();
    FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("constes-form.fxml"), resources);
    fxmlLoader.setController(importController);
    Parent root;
    try {
      root = fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Stage stage = new Stage();
    stage.setScene(new Scene(root));
    stage.showAndWait();
  }

  public void handleLiveContest(ActionEvent actionEvent) throws Exception {
    getQueryUtil().updateSetting(DatabaseConstants.LIVE_CONTEST_ON, liveContestMenu.isSelected() ? "true" : "false");
    if (liveContestMenu.isSelected()) {
      Locale loc = MainWindow.getLocale();
      ResourceBundle resources = ResourceBundle.getBundle("i18n/import", loc);
      FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("constes-form.fxml"), resources);
      DxLogConnectionController dxLogConnectionController = new DxLogConnectionController();
      loader.setController(dxLogConnectionController);
      Parent root;
      try {
        root = loader.load();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      Stage stage = new Stage();
      stage.setScene(new Scene(root));
      stage.showAndWait();
    }
  }
}
