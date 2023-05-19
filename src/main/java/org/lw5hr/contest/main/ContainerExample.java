package org.lw5hr.contest.main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ContainerExample extends Application {
  @Override
  public void start(Stage primaryStage) {
    // Create the main container
    VBox mainContainer = new VBox();

    // Create the first container
    HBox container1 = new HBox();
    container1.getChildren().add(new Label("Container 1"));

    // Create the second container
    VBox container2 = new VBox();
    container2.getChildren().add(new Label("Container 2"));

    // Create the third container
    HBox container3 = new HBox();
    container3.getChildren().add(new Label("Container 3"));

    // Add the containers to the main container
    mainContainer.getChildren().addAll(container1, container2, container3);

    // Create the main scene with the main container
    primaryStage.setScene(new Scene(mainContainer));
    primaryStage.setTitle("Multiple Containers Example");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
