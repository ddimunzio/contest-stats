package org.lw5hr.contest.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ImportController implements Initializable {

    @FXML
    Button cancel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    private void handleCancel(final ActionEvent event) throws Exception {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }
}
