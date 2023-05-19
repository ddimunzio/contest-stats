package org.lw5hr.contest.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.model.Contest;
import org.lw5hr.contest.utils.ADIFReader;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class ImportController extends GenericContestController {

  @FXML
  private TextField kIndex;

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

  @FXML
  private Label errorLabel;

  @FXML
  private Button browseButton;
  private File selectedFile;

  public ImportController() {
  }

  public ImportController(Contest contest) {
    if (contest.getkIndex() != null) {
      this.kIndex.setText(contest.getkIndex().toString());
    }
    this.dateTo.setValue(LocalDate.from(Instant.ofEpochMilli(contest.getDateTo().getTime())));
    this.dateFrom.setValue(LocalDate.from(Instant.ofEpochMilli(contest.getDateFrom().getTime())));
    this.contestNameField.setText(contest.getContestName());
    this.contestCategory.setText(contest.getCategory());
    if (contest.getSfi() != null) {
      this.sfiIndex.setText(contest.getSfi().toString());
    }
    if (contest.getaIndex() != null) {
      this.aIndex.setText(contest.getaIndex().toString());
    }
    this.filePathField.setVisible(false);
    this.browseButton.setVisible(false);
  }

  private final ResourceBundle resources = ResourceBundle.getBundle("i18n/main", MainWindow.getLocale());

  @Override
  @FXML
  public void handleCancel() {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }

  /*TODO: Validate Fields*/
  @Override
  @FXML
  protected void handleSave(final ActionEvent event) {
    QueryUtil q = MainWindow.getQueryUtil();
    if (validateFields() && !q.contestExist(contestNameField.getText())) {
      errorLabel.setVisible(false);
      try {
        if (validateFields()) {
          Contest contest = new Contest();
          ADIFReader adiReader = new ADIFReader(selectedFile.getPath());
          contest.setContestName(contestNameField.getText());
          contest.setCategory(contestCategory.getText());
          contest.setDateFrom(Date.from(dateFrom.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
          contest.setDateTo(Date.from(dateTo.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
          contest.setSfi(Double.valueOf(sfiIndex.getText()));
          contest.setkIndex(Integer.parseInt(kIndex.getText()));
          contest.setaIndex(Integer.parseInt(aIndex.getText()));
          adiReader.read(contest);
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      stage.close();
    } else {
      errorLabel.setText(resources.getString("key.import.contest.exist"));
      errorLabel.setVisible(true);
    }
  }

  @Override
  @FXML
  protected void handleBrowse(final ActionEvent event) throws Exception {
    String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File(currentPath));
    fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Text Files", "*.ADI")
            , new FileChooser.ExtensionFilter("HTML Files", "*.ADIF")
    );
    selectedFile = fileChooser.showOpenDialog(contestPane.getScene().getWindow());
    if (selectedFile != null) {
      try {
        filePathField.setText(selectedFile.getAbsolutePath());
        contestNameField.setText(selectedFile.getName().substring(0, selectedFile.getName().indexOf(".")));
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

  public void setContest(Contest contest) {
    if (contest.getkIndex() != null) {
      this.kIndex.setText(contest.getkIndex().toString());
    }
    Instant toInstant = contest.getDateTo().toInstant();
    this.dateTo.setValue(toInstant.atZone(ZoneId.systemDefault()).toLocalDate());
    Instant fromInstant = contest.getDateFrom().toInstant();
    this.dateFrom.setValue(fromInstant.atZone(ZoneId.systemDefault()).toLocalDate());
    this.contestNameField.setText(contest.getContestName());
    this.contestCategory.setText(contest.getCategory());
    if (contest.getSfi() != null) {
      this.sfiIndex.setText(contest.getSfi().toString());
    }
    if (contest.getaIndex() != null) {
      this.aIndex.setText(contest.getaIndex().toString());
    }
  }
}
