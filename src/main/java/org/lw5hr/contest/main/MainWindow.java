package org.lw5hr.contest.main;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.lw5hr.contest.db.HibernateUtil;

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


    @Override
    public void start(Stage primaryStage) throws Exception {
       //ResourceBundle i18nBundle = ResourceBundle.getBundle("Bundle.properties", new Locale("en", "US"));
        setPrimaryStage(primaryStage);
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("main-window-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        primaryStage.setTitle("Contest Stats by LW5HR");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory().openSession();
        launch();
    }


}
