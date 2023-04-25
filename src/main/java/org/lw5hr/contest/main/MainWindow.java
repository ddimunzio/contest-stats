package org.lw5hr.contest.main;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.lw5hr.contest.db.DatabaseConstants;
import org.lw5hr.contest.db.HibernateUtil;
import org.lw5hr.contest.db.QueryUtil;


import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainWindow extends Application {

    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        MainWindow.primaryStage = primaryStage;
    }

    @FXML
    MenuBar menuBar;

    @FXML
    public static void setLocale(Locale loc) throws IOException {
        QueryUtil q = new QueryUtil();
        q.updateSetting(DatabaseConstants.DEFAULT_LANG, loc.toString() );
        ResourceBundle resources = ResourceBundle.getBundle("i18n/main", loc);
        primaryStage.getScene().setRoot(FXMLLoader.load(MainWindow.class.getResource("main-window-view.fxml"),resources));
    }

    @FXML
    public static Locale getLocale() {
        QueryUtil q = new QueryUtil();
        Locale loc = q.getDefaultLocale();
        return loc;
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

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory().openSession();
        launch();
    }


}
