package org.lw5hr.contest.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.lw5hr.contest.db.DatabaseConstants;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.db.QueryUtilSql;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.model.Contest;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DxLogConnectionController extends GenericContestController {
  @FXML
  private  TextField kIndex;

  @FXML
  private DatePicker dateTo;

  @FXML
  private DatePicker dateFrom;

  @FXML
  private Pane contestPane;

  @FXML
  private Button cancelButton;

  @FXML
  Button saveButton;

  @FXML
  private TextField filePathField;

  @FXML
  private TextField contestNameField;

  @FXML
  private TextField sfiIndex;

  @FXML
  private TextField aIndex;

  private File selectedFile;
  QueryUtil q = MainWindow.getQueryUtil();

  /*TODO: Validate Fields*/

  @Override
  @FXML
  protected void handleSave(final ActionEvent event) throws Exception {
    if (selectedFile != null) {
      q.updateSetting(DatabaseConstants.DXLOG_DB_PATH, selectedFile.getAbsolutePath());
      q.updateContest(new Contest(contestNameField.getText(), true));
    }
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.close();
  }

  @Override
  public void handleCancel() {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }

  @Override
  @FXML
  protected void handleBrowse(final ActionEvent event) throws Exception {
    String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(currentPath));
    fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Text Files", "*.dxn")
    );
    selectedFile = fileChooser.showOpenDialog(contestPane.getScene().getWindow());
    if (selectedFile != null) {
      try {
        QueryUtilSql qs = new QueryUtilSql();
        filePathField.setText(selectedFile.getAbsolutePath());
        contestNameField.setText(qs.getContestName(selectedFile.getAbsolutePath()));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  protected boolean validateFields() {
    return false;
  }

  @Override
  protected void processFormData() {
    filePathField.setText("saldada");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    dateFrom.setValue(LocalDate.now());
    dateTo.setValue(LocalDate.now());
  }

  @Override
  public Node getStyleableNode() {
    return super.getStyleableNode();
  }
}
