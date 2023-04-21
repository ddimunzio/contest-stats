package org.lw5hr.contest.controllers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.model.Contest;

import java.net.URL;
import java.util.ResourceBundle;

public class ContestManagerController implements Initializable {

  @FXML
  TableView<Contest> contestTable;

  @FXML
  TableColumn<CheckBox, String> selectRow;

  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    QueryUtil q = new QueryUtil();
    contestTable.setItems(q.getContestList());
    contestTable.setEditable(true);
  }



}
