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
import org.lw5hr.contest.model.dxlog.HeaderInfo;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
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
 private TextField contestCategory;

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
    if (validateFields()) {
      q.initSetting(DatabaseConstants.DXLOG_DB_PATH, selectedFile.getAbsolutePath());
      Contest contest = new Contest(contestNameField.getText(), true);
      contest.setCategory(contestCategory.getText());
      contest.setDateFrom(Date.from(dateFrom.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
      contest.setDateTo(Date.from(dateTo.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
      contest.setSfi(Double.valueOf(sfiIndex.getText()));
      contest.setkIndex(Integer.parseInt(kIndex.getText()));
      contest.setaIndex(Integer.parseInt(aIndex.getText()));
      q.updateContest(contest);
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
        List<HeaderInfo> info = qs.getDxLogContest(selectedFile.getAbsolutePath());
        contestNameField.setText(info.stream()
                .filter(c -> c.getHdrname().equals(DatabaseConstants.DX_LOG_CONTEST_NAME))
                .findFirst().get().getHdrvalue());
        contestCategory.setText(info.stream()
                .filter(c -> c.getHdrname().equals(DatabaseConstants.DX_LOG_CONTEST_CATEGORY))
                .findFirst().get().getHdrvalue());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  protected boolean validateFields() {
    boolean isValid = true;

    if (selectedFile == null) {
      isValid = false;
      System.out.println("Error: No file selected.");
    }

    if (contestNameField.getText().isEmpty()) {
      isValid = false;
      System.out.println("Error: Contest name is required.");
    }

    if (contestCategory.getText().isEmpty()) {
      isValid = false;
      System.out.println("Error: Contest category is required.");
    }

    if (dateFrom.getValue() == null) {
      isValid = false;
      System.out.println("Error: Start date is required.");
    }

    if (dateTo.getValue() == null) {
      isValid = false;
      System.out.println("Error: End date is required.");
    }

    if (sfiIndex.getText().isEmpty() || !sfiIndex.getText().matches("\\d+(\\.\\d+)?")) {
      isValid = false;
      System.out.println("Error: SFI index is invalid.");
    }

    if (kIndex.getText().isEmpty() || !kIndex.getText().matches("\\d+")) {
      isValid = false;
      System.out.println("Error: K index is invalid.");
    }

    if (aIndex.getText().isEmpty() || !aIndex.getText().matches("\\d+")) {
      isValid = false;
      System.out.println("Error: A index is invalid.");
    }

    return isValid;
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
