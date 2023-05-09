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
import org.lw5hr.contest.utils.UDPListener;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
/**
 * Created by ddimunzio on 2023.
 */
public class MainWindow extends Application {

  private static final UDPListener udpListener;

  static {
    try {
      udpListener = new UDPListener();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static Stage primaryStage;

  public static void setPrimaryStage(Stage primaryStage) {
    MainWindow.primaryStage = primaryStage;
  }

  private static QueryUtil q = new QueryUtil();

  @FXML
  public static void setLocale(Locale loc) throws IOException {
    q.updateSetting(DatabaseConstants.DEFAULT_LANG, loc.toString());
    ResourceBundle resources = ResourceBundle.getBundle("i18n/main", loc);
    primaryStage.getScene().setRoot(FXMLLoader.load(Objects.requireNonNull(MainWindow.class.getResource("main-window-view.fxml")), resources));
  }

  @FXML
  public static Locale getLocale() {
    return q.getDefaultLocale();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", getLocale());
    FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("main-window-view.fxml"), mainResources, new JavaFXBuilderFactory());
    setPrimaryStage(primaryStage);
    Scene scene = new Scene(fxmlLoader.load());
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
    udpListener.close();
  }
  public static void main(String[] args) throws IOException {
    HibernateUtil.getSessionFactory().getCurrentSession();
    InitApp();
    launch();
  }
  public static void InitApp () throws IOException {
    if (!getQ().settingsExist(DatabaseConstants.DEFAULT_LANG)) {
      getQ().initSetting(DatabaseConstants.DEFAULT_LANG,
              DatabaseConstants.DEFAULT_LOCALE_LANGUAGE + "_" + DatabaseConstants.DEFAULT_LOCALE_COUNTRY);
    }
    if (!getQ().settingsExist(DatabaseConstants.CURRENT_CONTEST)) {
      getQ().initSetting(DatabaseConstants.CURRENT_CONTEST, "1");
    }
    if (!getQ().settingsExist(DatabaseConstants.LIVE_CONTEST_ON)) {
      getQ().initSetting(DatabaseConstants.LIVE_CONTEST_ON, "false");
    }
    QueryUtilSql qs = new QueryUtilSql();
    Optional<Settings> liveContest = getQ().getSetting(DatabaseConstants.LIVE_CONTEST_ON);

    if (liveContest.isPresent() && liveContest.get().getSettingValue().equals("true")) {
      Optional<Settings> setting = getQ().getSetting(DatabaseConstants.DXLOG_DB_PATH);
      setting.ifPresent(settings -> qs.getAllLoggedQso(settings.getSettingValue()));
   //   udpListener.listen();
    }

  }

  public static QueryUtil getQ() {
    return q;
  }

  public static void setQ(QueryUtil q) {
    MainWindow.q = q;
  }
}
