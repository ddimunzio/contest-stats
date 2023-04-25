package org.lw5hr.contest.controllers;
import com.dlsc.formsfx.view.controls.SimpleRadioButtonControl;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.model.Contest;

import java.net.URL;
import java.util.ResourceBundle;

public class ContestManagerController implements Initializable {

  @FXML
  TableView<Contest> contestTable;

  @FXML
  TableColumn<Contest, String> contest_name;

  @FXML
  TableColumn<Contest, String> contest_description;

  @FXML
  TableColumn<Contest, Contest> contest_action;

  private QueryUtil q = new QueryUtil();
  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    initializeCols();
    QueryUtil q = new QueryUtil();
    contestTable.setItems(q.getContestList());
    contestTable.setEditable(true);
  }

  ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", MainWindow.getLocale());
  private void initializeCols() {
    contest_name.setCellValueFactory(new PropertyValueFactory<>("contestName"));
    contest_description.setCellValueFactory(new PropertyValueFactory<>("contestDescription"));
    contest_action.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
    editableCols();
  }

  private void editableCols() {
    contest_name.setCellFactory(TextFieldTableCell.forTableColumn());
    contest_name.setOnEditCommit(e -> {
      Contest test = e.getTableView().getItems().get(e.getTablePosition().getRow());
      test.setContestName(e.getNewValue());
      q.updateContest(test);
    });

    contest_description.setCellFactory(TextFieldTableCell.forTableColumn());
    contest_description.setOnEditCommit(e -> {
      Contest test = e.getTableView().getItems().get(e.getTablePosition().getRow());
      test.setContestDescription(e.getNewValue());
      q.updateContest(test);
    });

    contest_action.setCellFactory(column -> new TableCell<Contest, Contest>() {
      private final Button button = new Button(mainResources.getString("key.contest.table.delete"));
      {
        button.setOnAction(e -> {
          Contest contest = getItem();
          q.deleteContest(contest);
        });
      }
      @Override
      protected void updateItem(Contest item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setGraphic(null);
        } else {
          setGraphic(button);
        }
      }
    });

    contestTable.setEditable(true);
  }

}
