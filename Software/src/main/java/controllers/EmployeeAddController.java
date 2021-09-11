package controllers;

import entities.Employee;
import entities.EmployeeRoles;
import entities.Roles;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import repositories.RolesRepository;
import utils.*;
import validation.EmployeeValidation;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeAddController implements Initializable {
    @FXML
    AnchorPane host;
    @FXML
    private TextField emailHolder;
    @FXML
    private TextField nameHolder;
    @FXML
    private TextField passwordHolder;
    @FXML
    private TextField phoneHolder;
    @FXML
    private DatePicker dateOfBirthHolder;
    @FXML
    private ComboBox<String> roleHolder;
    @FXML
    private Label errorMessage;

    public static EmployeeAddController instance;
    public EmployeeAddController() { instance = this; }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add item to Roles ComboBox
        List<Roles> rolesList = RolesRepository.getAll();
        if (rolesList.size() > 0) {
            for (Roles item : rolesList) {
                roleHolder.getItems().add(item.getName());
            }
            roleHolder.setValue(rolesList.get(0).getName());
        }
    }

    @FXML
    void close(MouseEvent event) {
        StageHelper.closeStage(event);
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(false);
    }

    @FXML
    void save(ActionEvent event) {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session;

        Employee employee = new Employee();
        employee.setId(UUIDHelper.generateType4UUID().toString());
        employee.setFullName(nameHolder.getText());
        employee.setBirthDay(dateOfBirthHolder.getValue() != null ? Date.from(dateOfBirthHolder.getValue().atStartOfDay(ZoneId.of("Etc/GMT+8")).toInstant()) : null);
        employee.setPhone(phoneHolder.getText());
        employee.setEmail(emailHolder.getText());
        employee.setPassword(passwordHolder.getText().isEmpty() ? null : BCryptHelper.encode(passwordHolder.getText()));

        List<String> validateInsert = EmployeeValidation.validateInsert(employee);
        if (roleHolder.getItems() == null) {
            validateInsert.add("Chưa chọn chức vụ");
        }
        if (validateInsert.size() == 0) {
            // Save new employee
            session = factory.openSession();
            session.beginTransaction();
            session.save(employee);
            session.getTransaction().commit();
            session.close();
            // Save new employee_roles
            Roles roles = RolesRepository.getByName(roleHolder.getValue());
            EmployeeRoles employeeRoles = new EmployeeRoles();
            employeeRoles.setId(UUIDHelper.generateType4UUID().toString());
            employeeRoles.setRoles(roles);
            employeeRoles.setEmployee(employee);

            session = factory.openSession();
            session.beginTransaction();
            session.save(employeeRoles);
            session.getTransaction().commit();
            session.close();

            // Show alert box
            AlertBoxHelper.showMessageBox("Thêm thành công");
            // Close stage
            StageHelper.closeStage(event);
            // Refresh content table
            EmployeeCategoryController.getInstance().refresh();
            // Unhide host
            MainNavigatorController.instance.getHost().setDisable(false);
        } else {
            errorMessage.setText(validateInsert.get(0));
        }
    }

    @FXML
    void requestFocus(MouseEvent event) {
        host.requestFocus();
    }
}
