package org.lw5hr.contest.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.utils.ADIFReader;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ImportController implements Initializable {
    @FXML
    Pane importPane;
    @FXML
    Button cancelButton;

    @FXML
    Button saveButton;
    @FXML
    TextField filePathField;

    @FXML
    TextField contestNameField;

    @FXML
    Label errorLabel;

    private File adiFile;

    private final ResourceBundle importResources = ResourceBundle.getBundle("i18n/import", MainWindow.getLocale());
    @FXML
    private void handleCancel(final ActionEvent event) throws Exception {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /*TODO: Validate Fields*/
    @FXML
    private void handleSave(final ActionEvent event) throws Exception {
        QueryUtil q = MainWindow.getQ();
        if (adiFile != null && !q.contestExist(contestNameField.getText())) {
            errorLabel.setVisible(false);
            try {
                ADIFReader adiReader = new ADIFReader(adiFile.getPath());
                adiReader.read(contestNameField.getText());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.close();

        } else {
            errorLabel.setText(importResources.getString("key.import.contest.exist"));
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void handleBrowse(final ActionEvent event) throws Exception {
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(currentPath));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.ADI")
                , new FileChooser.ExtensionFilter("HTML Files", "*.ADIF")
        );
        adiFile = fileChooser.showOpenDialog(importPane.getScene().getWindow());
        if (adiFile != null) {
            try {
                filePathField.setText(adiFile.getAbsolutePath());
                contestNameField.setText(adiFile.getName().substring(0, adiFile.getName().indexOf(".")));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
