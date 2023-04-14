package org.lw5hr.contest.main;

import com.almasb.fxgl.app.PrimaryStageWindow;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.lw5hr.contest.controllers.MenuController;
import org.lw5hr.contest.db.HibernateUtil;
import org.lw5hr.contest.main.ImportContest;

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
    BorderPane borderPane;

    @FXML
    public static void setLocale(Locale loc) throws IOException {
        ResourceBundle resources = ResourceBundle.getBundle("i18n/main", loc);
        primaryStage.getScene().setRoot(FXMLLoader.load(MainWindow.class.getResource("main-window-view.fxml"),resources));
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale loc = new Locale("es_ES");
        ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", loc);
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("main-window-view.fxml"), mainResources, new JavaFXBuilderFactory());
        setPrimaryStage(primaryStage);

        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        primaryStage.setTitle(mainResources.getString("key.main.general.title"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory().openSession();
        launch();
    }


}
