package org.lw5hr.contest.main;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.lw5hr.contest.db.DatabaseConstants;
import org.lw5hr.contest.db.HibernateUtil;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.db.QueryUtilSql;
import org.lw5hr.contest.model.Settings;
import org.lw5hr.contest.utils.StatsUtil;
import org.lw5hr.contest.utils.UDPListener;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Author: Diego Dimunzio - LW5HR   
 */

public class MainWindow extends Application {

  private static Stage primaryStage;

  public MainWindow() throws IOException {
  }

  public static void setPrimaryStage(Stage primaryStage) {
    MainWindow.primaryStage = primaryStage;
  }

  private static QueryUtil queryUtil = new QueryUtil();

  static UDPListener udpListener;

  static {
    try {
      udpListener = new UDPListener();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @FXML
  public static void setLocale(Locale loc) throws IOException {
    queryUtil.updateSetting(DatabaseConstants.DEFAULT_LANG, loc.toString());
    ResourceBundle resources = ResourceBundle.getBundle("i18n/main", loc);
    primaryStage.getScene().setRoot(FXMLLoader.load(Objects.requireNonNull(MainWindow.class.getResource("main-window-view.fxml")), resources));
  }

  @FXML
  public static Locale getLocale() {
    return queryUtil.getDefaultLocale();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", getLocale());
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-window-view.fxml"), mainResources, new JavaFXBuilderFactory());
    setPrimaryStage(primaryStage);
    Scene scene = new Scene(fxmlLoader.load(), 1280, 700);
    primaryStage.setTitle(mainResources.getString("key.main.general.title"));
    primaryStage.setScene(scene);
    primaryStage.show();

  }

  /**
   * This method is called when the application should stop, and provides a convenient place to prepare for application exit and destroy resources.
   * The implementation of this method provided by the Application class does nothing.
   * An application may override this method to perform any shutdown operations.
   * The stop method will be called on the JavaFX Application Thread.
   *
   * @throws Exception
   */
  @Override
  public void stop() throws Exception {
    super.stop();
    HibernateUtil.getSessionFactory().close();
    udpListener.stop();
  }
  public static void main(String[] args) throws IOException {
    HibernateUtil.getSessionFactory().getCurrentSession();
    InitApp();
    launch();
  }

  public static void InitApp () throws IOException {
    if (!getQueryUtil().settingsExist(DatabaseConstants.DEFAULT_LANG)) {
      getQueryUtil().initSetting(DatabaseConstants.DEFAULT_LANG,
              DatabaseConstants.DEFAULT_LOCALE_LANGUAGE + "_" + DatabaseConstants.DEFAULT_LOCALE_COUNTRY);
    }
    if (!getQueryUtil().settingsExist(DatabaseConstants.CURRENT_CONTEST)) {
      getQueryUtil().initSetting(DatabaseConstants.CURRENT_CONTEST, "1");
    }
    if (!getQueryUtil().settingsExist(DatabaseConstants.LIVE_CONTEST_ON)) {
      getQueryUtil().initSetting(DatabaseConstants.LIVE_CONTEST_ON, "false");
    }
    QueryUtilSql qs = new QueryUtilSql();
    Optional<Settings> liveContest = getQueryUtil().getSetting(DatabaseConstants.LIVE_CONTEST_ON);

    if (liveContest.isPresent() && liveContest.get().getSettingValue().equals("true")) {
      Optional<Settings> setting = getQueryUtil().getSetting(DatabaseConstants.DXLOG_DB_PATH);
      setting.ifPresent(settings -> qs.getAllLoggedQso(settings.getSettingValue()));
    }
    //udpListener.listen();
    StatsUtil st = new StatsUtil();
    st.teamTopRates();
  }

  public static QueryUtil getQueryUtil() {
    return queryUtil;
  }

  public static void setQueryUtil(QueryUtil queryUtil) {
    MainWindow.queryUtil = queryUtil;
  }
}
