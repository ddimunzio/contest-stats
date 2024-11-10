package org.lw5hr.contest.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.lw5hr.contest.db.DatabaseConstants;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.main.About;
import org.lw5hr.contest.main.ContestManager;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.model.Contest;
import org.lw5hr.contest.model.Settings;
import org.lw5hr.contest.utils.StatsUtil;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.lw5hr.contest.main.MainWindow.getQueryUtil;

/**
 * Author: Diego Dimunzio - LW5HR   
 */

public class MenuController extends BorderPane implements Initializable {
  @FXML
  private BarChart<String, Integer> personalTopRates;

  @FXML
  private BarChart<String, Integer> teamTopRates;

  @FXML
  private MenuItem operator;

  @FXML
  private MenuItem band;

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
    Locale loc = MainWindow.getLocale();
    ResourceBundle resources = ResourceBundle.getBundle("i18n/main", loc);
    MenuItem item = (MenuItem) event.getSource();
    TotalByProperty controller = new TotalByProperty(item.getId());
    FXMLLoader fxmlLoader;
    fxmlLoader = new FXMLLoader(MainWindow.class.getResource("generic-bar-chart.fxml"), resources);
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
  private void handleTotalByHourAction(final ActionEvent ignoredEvent) throws Exception {
    Locale loc = MainWindow.getLocale();
    ResourceBundle resources = ResourceBundle.getBundle("i18n/main", loc);
    TotalByHourController controller = new TotalByHourController();
    FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("generic-bar-chart.fxml"), resources);
    fxmlLoader.setController(controller);
    Parent root;
    try {
      root = fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Stage stage = new Stage();
    stage.setScene(new Scene(root, 1600, 768));
    stage.show();
  }

  @FXML
  private void handleQsosXAndOperator(final ActionEvent event) throws Exception {
    Locale loc = MainWindow.getLocale();
    ResourceBundle resources = ResourceBundle.getBundle("i18n/main", loc);
    MenuItem item = (MenuItem) event.getSource();
    ByXAndOperatorController controller = new ByXAndOperatorController(item.getId());
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
  private void handleQsosByHourAndOperatorArea(final ActionEvent ignoredEvent) throws Exception {
    Stage stage = new Stage();
    ByHourAndOperatorAreaChart byHourAndOperatorArea = new ByHourAndOperatorAreaChart();
    byHourAndOperatorArea.start(stage);
  }

  @FXML
  private void handleLanguage(final ActionEvent event) throws IOException {
    String id = ((RadioMenuItem) event.getSource()).getId();
    switch (id) {
      case "en" -> {
        MainWindow.setLocale(Locale.of("en", "US"));
        setSelectedLanguage(id);
      }
      case "es" -> {
        MainWindow.setLocale(Locale.of("es", "ES"));
        setSelectedLanguage(id);
      }
    }
  }

  @FXML
  private void handleManageContest(final ActionEvent ignoredEvent) throws Exception {
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
  private void handleAbout(final ActionEvent ignoredEvent) throws Exception {
    Stage stage = new Stage();
    About about = new About();
    about.start(stage);
  }

  private void addContestItems() {
    QueryUtil q = getQueryUtil();
    Long selectedContest = q.getSelectedContest();
    List<Contest> contestList = q.getContestList();
    contestList.forEach(c -> {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
      LocalDate dateFrom = c.getDateFrom().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      LocalDate dateTo = c.getDateTo().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

      String formattedDateFrom = dateFrom.format(formatter);
      String formattedDateTo = dateTo.format(formatter);

      RadioMenuItem r = new RadioMenuItem(c.getContestName() + " [" + formattedDateFrom + " - " + formattedDateTo + "]");
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
    if (getQueryUtil().getSetting(DatabaseConstants.DXLOG_DB_PATH).isEmpty()) {
      liveContestMenu.setDisable(true);
    }
    initializeManinCharts();
  }

  private void setSelectedLanguage(String lang) {
    en.setSelected(en.getId().equalsIgnoreCase(lang));
    es.setSelected(es.getId().equalsIgnoreCase(lang));

  }

  /**
   * This method is called when the menu is shown.  It is used to update the list of contests.
   *
   * @param ignoredEvent not used but required by JavaFX
   */
  public void handleShow(Event ignoredEvent) {
    menuContestList.getItems().clear();
    addContestItems();
  }

  @FXML
  private void handleImportAdifAction(final ActionEvent ignoredEvent) {
    Locale loc = MainWindow.getLocale();
    ResourceBundle resources = ResourceBundle.getBundle("i18n/main", loc);
    FXMLLoader fxmlLoader = new FXMLLoader(MenuController.class.getResource("contest-import.fxml"), resources);
    Parent root;
    try {
      root = fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Stage stage = new Stage();
    stage.setTitle(resources.getString("key.import.add.new"));
    stage.setScene(new Scene(root));
    stage.showAndWait();
  }

  public void handleDxLogConnection(ActionEvent ignoredActionEvent) throws Exception {
    Locale loc = MainWindow.getLocale();
    ResourceBundle resources = ResourceBundle.getBundle("i18n/main", loc);
    FXMLLoader loader = new FXMLLoader(MenuController.class.getResource("contest-dxlog-connection.fxml"), resources);
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

  public void handleEnableDxLog(ActionEvent ignoredActionEvent) throws Exception {
    getQueryUtil().updateSetting(DatabaseConstants.LIVE_CONTEST_ON, liveContestMenu.isSelected() ? "true" : "false");
  }

  public void handleCompare(ActionEvent ignoredActionEvent) {
    Locale loc = MainWindow.getLocale();
    ResourceBundle resources = ResourceBundle.getBundle("i18n/main", loc);
    FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("compare-contest.fxml"), resources);
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

  public void handleBandAndOperator(ActionEvent ignoredActionEvent) throws Exception {
    Locale loc = MainWindow.getLocale();
    ResourceBundle resources = ResourceBundle.getBundle("i18n/main", loc);
    FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("by-band-and-operator.fxml"), resources);
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

  private void initializeManinCharts() {
    StatsUtil st = new StatsUtil();
    ObservableList<XYChart.Series<String, Integer>> topTenTeam = st.teamTopRates();
    ObservableList<XYChart.Series<String, Integer>> topTenPersonal = st.personalTopRates();
    teamTopRates.getData().addAll(topTenTeam);
    personalTopRates.getData().addAll(topTenPersonal);

  }
}
