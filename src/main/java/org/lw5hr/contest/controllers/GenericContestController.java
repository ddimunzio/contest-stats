package org.lw5hr.contest.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class GenericContestController extends Pane {
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
  public GenericContestController() {}

  @FXML
  protected abstract void handleSave(final ActionEvent event) throws Exception;

  @FXML
  public abstract void handleCancel();

  @FXML
  protected abstract void handleBrowse(final ActionEvent event) throws Exception;

  protected abstract boolean validateFields();

  protected abstract void processFormData();

  @FXML
  public abstract void initialize(URL location, ResourceBundle resources);
}
