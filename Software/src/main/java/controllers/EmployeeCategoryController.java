package controllers;

import entities.Employee;
import holders.EmployeeHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import lombok.SneakyThrows;
import repositories.EmployeeRepository;
import utils.StageHelper;
import utils.TableHelper;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class EmployeeCategoryController implements Initializable {
    @FXML
    private TextField searchBar;
    @FXML
    private TableView<Employee> contentTable;
    @FXML
    private TableColumn<Employee, String> nameCol;
    @FXML
    private TableColumn<Employee, String> phoneCol;
    @FXML
    private TableColumn<Employee, String> emailCol;
    @FXML
    private TableColumn<Employee, Date> dateOfBirthCol;

    // For other class call function from this class
    public static EmployeeCategoryController instance;

    public EmployeeCategoryController() {
        instance = this;
    }

    public static EmployeeCategoryController getInstance() {
        return instance;
    }
    ///

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Employee> employeeList = EmployeeRepository.getAll();
        TableHelper.setEmployeeTable(employeeList, contentTable, nameCol, phoneCol, emailCol, dateOfBirthCol);
    }

    @FXML
    void insert(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/EmployeeAdd.fxml")));
        StageHelper.startStage(root);
        // Hide host
        MainNavigatorController.instance.getHost().setDisable(true);
    }

    @FXML
    void search(ActionEvent event) {
        try {
            List<Employee> employeeList = EmployeeRepository.getByNamePhoneEmail(searchBar.getText());
            TableHelper.setEmployeeTable(employeeList, contentTable, nameCol, phoneCol, emailCol, dateOfBirthCol);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
    }

    @FXML
    void select(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            Employee employee = contentTable.getSelectionModel().getSelectedItem();
            contentTable.getSelectionModel().clearSelection();
            // Store Employee to use in another class
            if (employee != null) {
                EmployeeHolder.getInstance().setEmployee(employee);
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/EmployeeUpdate.fxml")));
                StageHelper.startStage(root);
                // Hide host
                MainNavigatorController.instance.getHost().setDisable(true);
            }
        }
    }

    // Refresh table
    public void refresh() {
        this.initialize(null, null);
    }
}
