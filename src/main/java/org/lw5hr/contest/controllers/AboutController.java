package org.lw5hr.contest.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;
public class AboutController implements Initializable {
  @FXML
  TextArea aboutText;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    aboutText.setText(resources.getString("main.help.about.text"));
    aboutText.setEditable(false);
  }
}
