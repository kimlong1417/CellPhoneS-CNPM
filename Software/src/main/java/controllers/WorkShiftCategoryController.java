package controllers;

import entities.WorkShift;
import holders.WorkShiftHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import repositories.WorkShiftRepository;
import utils.StageHelper;
import utils.TableHelper;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class WorkShiftCategoryController implements Initializable {
    @FXML
    AnchorPane host;
    @FXML
    private TextField searchBar;

    @FXML
    private TableView<WorkShift> contentTable;
    @FXML
    private TableColumn<WorkShift, String> dateCol;
    @FXML
    private TableColumn<WorkShift, String> nameCol;
    @FXML
    private TableColumn<WorkShift, String> timeInCol;
    @FXML
    private TableColumn<WorkShift, String> timeOutCol;

    // For other class to call function from this class
    public static WorkShiftCategoryController instance;

    public WorkShiftCategoryController() {
        instance = this;
    }

    public static WorkShiftCategoryController getInstance() {
        return instance;
    }
    ////

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<WorkShift> allWorkShift = WorkShiftRepository.getAll();
        TableHelper.setWorkShiftTable(allWorkShift, contentTable, dateCol, nameCol, timeInCol, timeOutCol);
    }

    @FXML
    void openWorkTable(ActionEvent event) {
        MainNavigatorController.getInstance().openTimeTable(null);
    }

    @FXML
    void save(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/WorkShiftAdd.fxml")));
        StageHelper.startStage(root);
        //Hide host
        MainNavigatorController.instance.getHost().setDisable(true);
    }

    @FXML
    void search(ActionEvent event) {
        List<WorkShift> filteredWorkShift = WorkShiftRepository.getLikeName(searchBar.getText());
        TableHelper.setWorkShiftTable(filteredWorkShift, contentTable, dateCol, nameCol, timeInCol, timeOutCol);
    }

    @FXML
    void select(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            WorkShift chosenWorkShift = contentTable.getSelectionModel().getSelectedItem();
            contentTable.getSelectionModel().clearSelection();
            // Store WorkShift to use in another class
            if (chosenWorkShift != null) {
                WorkShiftHolder.getInstance().setWorkShift(chosenWorkShift);
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/WorkShiftUpdate.fxml")));
                StageHelper.startStage(root);
                // Hide host
                MainNavigatorController.instance.getHost().setDisable(true);
            }
        }
    }

    @FXML
    void requestFocus(MouseEvent event) {
        host.requestFocus();
    }
}
