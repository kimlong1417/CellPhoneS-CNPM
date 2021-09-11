package controllers;

import dataModel.WorkTableModel;
import entities.WorkTable;
import holders.WorkTableHolder;
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
import org.hibernate.SessionFactory;
import repositories.WorkTableRepository;
import utils.HibernateUtils;
import utils.StageHelper;
import utils.TableHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class WorkTableCategoryController implements Initializable {
    @FXML
    AnchorPane host;
    @FXML
    private TextField searchBar;

    @FXML
    private TableView<WorkTableModel> contentTable;
    @FXML
    private TableColumn<WorkTableModel, String> nameCol;
    @FXML
    private TableColumn<WorkTableModel, String> shiftCol;
    @FXML
    private TableColumn<WorkTableModel, String> timeInCol;
    @FXML
    private TableColumn<WorkTableModel, String> timeOutCol;
    @FXML
    private TableColumn<WorkTableModel, String> workDaysCol;

    // For other class to call function from this class
    public static WorkTableCategoryController instance;

    public WorkTableCategoryController() {
        instance = this;
    }

    public static WorkTableCategoryController getInstance() {
        return instance;
    }
    ///

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<WorkTable> allWorkTable = WorkTableRepository.getAll();
        List<WorkTableModel> allWorkTableModel = new ArrayList<>();
        for (WorkTable item : allWorkTable) {
            WorkTableModel model = new WorkTableModel(item);
            allWorkTableModel.add(model);
        }
        TableHelper.setWorkTableModelTable(allWorkTableModel, contentTable, nameCol, shiftCol, timeInCol, timeOutCol, workDaysCol);
    }

    @FXML
    void openShift(ActionEvent event) {
        MainNavigatorController.getInstance().openShiftTable();
    }

    @FXML
    void save(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/WorkTableAdd.fxml")));
        StageHelper.startStage(root);
        // Hide host
        MainNavigatorController.instance.getHost().setDisable(true);
    }

    @FXML
    void search(ActionEvent event) {
        List<WorkTable> filteredWorkTable = WorkTableRepository.getByEmployeeOrShift(searchBar.getText());
        List<WorkTableModel> filteredWorkTableModel = new ArrayList<>();
        for (WorkTable item : filteredWorkTable) {
            WorkTableModel model = new WorkTableModel(item);
            filteredWorkTableModel.add(model);
        }
        TableHelper.setWorkTableModelTable(filteredWorkTableModel, contentTable, nameCol, shiftCol, timeInCol, timeOutCol, workDaysCol);
    }

    @FXML
    void select(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            WorkTable chosenWorkTable = contentTable.getSelectionModel().getSelectedItem().getWorkTable();
            contentTable.getSelectionModel().clearSelection();
            // Store WorkTable to use in another class
            if (chosenWorkTable != null) {
                WorkTableHolder.getInstance().setWorkTable(chosenWorkTable);
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/WorkTableUpdate.fxml")));
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
