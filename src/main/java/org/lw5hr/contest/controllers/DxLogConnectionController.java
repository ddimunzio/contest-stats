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
import org.lw5hr.contest.db.DatabaseConstants;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.db.QueryUtilSql;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.model.Contest;
import org.lw5hr.contest.utils.ADIFReader;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class DxLogConnectionController implements Initializable {
  @FXML
  Pane connectPane;
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
  @FXML
  private File sqlLiteFile;

  QueryUtil q = MainWindow.getQ();
  
  @FXML
  private void handleCancel(final ActionEvent event) throws Exception {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }


  /*TODO: Validate Fields*/
  @FXML
  private void handleSave(final ActionEvent event) throws Exception {
    if (sqlLiteFile != null) {
      q.updateSetting(DatabaseConstants.DXLOG_DB_PATH, sqlLiteFile.getAbsolutePath());
      q.updateContest(new Contest(contestNameField.getText(), true));
    }
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.close();
  }

  @FXML
  private void handleBrowse(final ActionEvent event) throws Exception {
    String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(currentPath));
    fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Text Files", "*.dxn")
    );
    sqlLiteFile = fileChooser.showOpenDialog(connectPane.getScene().getWindow());
    if (sqlLiteFile != null) {
      try {
        QueryUtilSql qs = new QueryUtilSql();
        filePathField.setText(sqlLiteFile.getAbsolutePath());
        contestNameField.setText(qs.getContestName(sqlLiteFile.getAbsolutePath()));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }
}
