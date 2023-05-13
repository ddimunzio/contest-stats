package org.lw5hr.contest.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.model.Contest;

import java.net.URL;
import java.util.ResourceBundle;

public class ContestManagerController implements Initializable {

  @FXML
  private TableColumn<Contest, Contest> edit_contest;

  @FXML
  TableView<Contest> contestTable;

  @FXML
  TableColumn<Contest, String> contest_name;

  @FXML
  TableColumn<Contest, String> contest_description;

  @FXML
  TableColumn<Contest, Contest> delete_contest;
  private final QueryUtil q = MainWindow.getQueryUtil();
  ResourceBundle mainResources = ResourceBundle.getBundle("i18n/main", MainWindow.getLocale());

  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    initializeCols();
    populateTable();
  }
  private void populateTable() {
    QueryUtil q = new QueryUtil();
    contestTable.setItems(q.getContestList());
    contestTable.setEditable(true);
  }

  private void initializeCols() {
    contest_name.setCellValueFactory(new PropertyValueFactory<>("contestName"));
    contest_description.setCellValueFactory(new PropertyValueFactory<>("contestDescription"));
    delete_contest.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
    edit_contest.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
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
    delete_contest.setCellFactory(column -> new TableCell<>() {
      private final Button button = new Button(mainResources.getString("key.contest.table.delete"));
      {
        button.setOnAction(e -> {
          Contest contest = getItem();
          q.deleteContest(contest);
          /*clean all items from the table and re-populate it*/
          contestTable.getItems().clear();
          populateTable();
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

    edit_contest.setCellFactory(column -> new TableCell<>(){
      private final Button button = new Button(mainResources.getString("key.contest.table.edit"));
      {
        button.setOnAction(e -> {
          Contest contest = getItem();
          q.updateContest(contest);
          /*clean all items from the table and re-populate it*/
          contestTable.getItems().clear();
          populateTable();
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
