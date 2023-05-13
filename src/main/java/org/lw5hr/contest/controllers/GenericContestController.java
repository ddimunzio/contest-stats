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
  protected TextField kIndex;
  @FXML
  protected DatePicker dateTo;
  @FXML
  protected DatePicker dateFrom;
  @FXML
  protected TextField filePathField;
  @FXML
  protected TextField contestNameField;
  @FXML
  protected TextField sfiIndex;
  @FXML
  protected TextField aIndex;
  @FXML
  protected Label errorLabel;
  @FXML
  protected Button cancelButton;
  @FXML
  protected Button saveButton;

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
