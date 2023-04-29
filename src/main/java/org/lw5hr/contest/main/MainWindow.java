package org.lw5hr.contest.main;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.lw5hr.contest.db.DatabaseConstants;
import org.lw5hr.contest.db.HibernateSqlUtil;
import org.lw5hr.contest.db.HibernateUtil;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.db.QueryUtilSql;
import org.lw5hr.contest.model.dxlog.QsoData;
import org.lw5hr.contest.utils.UDPListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

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

    public MainWindow() throws IOException {
    }

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
    /**
     * This method is called when the application should stop, and provides a convenient place to prepare for application exit and destroy resources.
     * The implementation of this method provided by the Application class does nothing.
     * An application may override this method to perform any shutdown operations.
     * The stop method will be called on the JavaFX Application Thread.
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        HibernateUtil.getSessionFactory().close();
        udpListener.close();
    }

    public static void main(String[] args) throws Exception {
        HibernateUtil.getSessionFactory().getCurrentSession();
        QueryUtilSql qs = new QueryUtilSql();
        qs.getAllLoggedQso();
        launch();
        udpListener.listen();
}
    public static QueryUtil getQ() {
        return q;
    }
    public static void setQ(QueryUtil q) {
        MainWindow.q = q;
    }
}
