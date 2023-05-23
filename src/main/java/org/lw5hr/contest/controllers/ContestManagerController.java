package org.lw5hr.contest.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.lw5hr.contest.db.QueryUtil;
import org.lw5hr.contest.main.MainWindow;
import org.lw5hr.contest.model.Contest;
import org.lw5hr.contest.utils.CQWWUBNReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ContestManagerController implements Initializable {

  @FXML
  private TableColumn<Contest, Contest> attach_ubn;

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
    attach_ubn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
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

    attach_ubn.setCellFactory(column -> new TableCell<>() {
      private final Button button = new Button("UBN");
      {
        button.setOnAction(e -> {
          Contest contest = getItem();
          // Create a FileChooser object
          FileChooser fileChooser = new FileChooser();

          // Show the file dialog and get the selected file
          File selectedFile = fileChooser.showOpenDialog(new Stage());

          // Process the selected file (e.g., display its path)
          if (selectedFile != null) {
            CQWWUBNReader cqwwubnReader = new CQWWUBNReader();
            cqwwubnReader.readUbnFile(selectedFile, contest.getQsos());
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
          }
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
          FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("contest-edit.fxml"), mainResources);
          Parent root;
          try {
            root = fxmlLoader.load();
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
          ImportController importController = fxmlLoader.getController();
          importController.setContest(contest);
          Stage stage = new Stage();
          stage.setTitle(mainResources.getString("key.import.add.new"));
          stage.setScene(new Scene(root));
          stage.showAndWait();
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
