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
import org.lw5hr.contest.utils.UDPListener;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainWindow extends Application {

    private static Stage primaryStage;
    public static void setPrimaryStage(Stage primaryStage) {
        MainWindow.primaryStage = primaryStage;
    }

    private static QueryUtil q = new QueryUtil();
    @FXML
    public static void setLocale(Locale loc) throws IOException {
        q.updateSetting(DatabaseConstants.DEFAULT_LANG, loc.toString() );
        ResourceBundle resources = ResourceBundle.getBundle("i18n/main", loc);
        primaryStage.getScene().setRoot(FXMLLoader.load(Objects.requireNonNull(MainWindow.class.getResource("main-window-view.fxml")),resources));
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
    public static void main(String[] args) throws Exception {
        HibernateUtil.getSessionFactory().getCurrentSession();
        UDPListener udp = new UDPListener();
        udp.listen();

        launch();
    }
    public static QueryUtil getQ() {
        return q;
    }
    public static void setQ(QueryUtil q) {
        MainWindow.q = q;
    }
}
