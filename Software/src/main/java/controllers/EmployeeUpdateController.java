package controllers;

import entities.Employee;
import entities.EmployeeRoles;
import entities.Roles;
import holders.EmployeeHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import repositories.EmployeeRolesRepository;
import repositories.RolesRepository;
import utils.AlertBoxHelper;
import utils.HibernateUtils;
import utils.StageHelper;
import utils.UUIDHelper;
import validation.EmployeeValidation;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeUpdateController implements Initializable {
    @FXML
    AnchorPane host;
    @FXML
    private TextField emailHolder;
    @FXML
    private TextField nameHolder;
    @FXML
    private ComboBox<String> roleHolder;
    @FXML
    private TextField phoneHolder;
    @FXML
    private DatePicker dateOfBirthHolder;
    @FXML
    private Label errorMessage;

    public static EmployeeUpdateController instance;
    public EmployeeUpdateController() { instance = this; }

    // Get Employee from EmployeeCategoryController select(MouseEvent event)
    Employee employee = EmployeeHolder.getInstance().getEmployee();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add item to Roles ComboBox
        List<Roles> rolesList = RolesRepository.getAll();
        for (Roles item : rolesList) {
            roleHolder.getItems().add(item.getName());
        }
        // Set default value
        EmployeeRoles employeeRoles = EmployeeRolesRepository.getByEmployeeId(employee.getId());
        roleHolder.setValue(rolesList.stream().filter(t -> t.getId().equals(employeeRoles.getRoles().getId())).findFirst().get().getName());
        // Set employee in update window
        nameHolder.setText(employee.getFullName());
        phoneHolder.setText(employee.getPhone());
        emailHolder.setText(employee.getEmail());
        // Change Date to LocalDate
        Date safeDate = new Date(employee.getBirthDay().getTime());
        dateOfBirthHolder.setValue(safeDate.toInstant().atZone(ZoneId.of("Etc/GMT-8")).toLocalDate());
    }

    @FXML
    void close(MouseEvent event) {
        // Clear employee holder
        EmployeeHolder.getInstance().setEmployee(null);
        StageHelper.closeStage(event);
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(false);
    }

    @FXML
    void delete(ActionEvent event) {
        // Delete employee_roles
        EmployeeRoles employeeRoles = EmployeeRolesRepository.getByEmployeeId(employee.getId());
        Session session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(employeeRoles);

        // Delete employee
        session.delete(employee);
        session.getTransaction().commit();
        session.close();
        ;

        // Close stage
        StageHelper.closeStage(event);
        // Show alert box
        AlertBoxHelper.showMessageBox("Xoá thành công");
        // Refresh content table
        EmployeeCategoryController.getInstance().refresh();
        // Clear customer holder
        EmployeeHolder.getInstance().setEmployee(null);
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(false);
    }

    @FXML
    void update(ActionEvent event) {
        // Create session
        Session session;

        // Get new employee
        employee.setFullName(nameHolder.getText());
        employee.setPhone(phoneHolder.getText());
        employee.setEmail(emailHolder.getText());
        employee.setBirthDay(Date.from(dateOfBirthHolder.getValue().atStartOfDay(ZoneId.of("Etc/GMT+8")).toInstant()));

        List<String> validateUpdate = EmployeeValidation.validateUpdate(employee);
        if (validateUpdate.size() == 0) {
            // Update employee
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(employee);
            session.getTransaction().commit();
            session.close();
            // Delete then save new employee_roles
            EmployeeRolesRepository.deleteByEmployeeId(employee.getId());

            Roles roles = RolesRepository.getByName(roleHolder.getValue());
            EmployeeRoles employeeRoles = new EmployeeRoles();
            employeeRoles.setId(UUIDHelper.generateType4UUID().toString());
            employeeRoles.setRoles(roles);
            employeeRoles.setEmployee(employee);

            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(employeeRoles);
            session.getTransaction().commit();
            session.close();

            // Refresh content table
            EmployeeCategoryController.getInstance().refresh();
            // Set employee holder
            EmployeeHolder.getInstance().setEmployee(employee);
            // Close Stage
            StageHelper.closeStage(event);
            // Show alert box
            AlertBoxHelper.showMessageBox("Cập nhật thành công");
            // Unhide host
            MainNavigatorController.instance.getHost().setDisable(false);
        } else {
            errorMessage.setText(validateUpdate.get(0));
        }
    }

    @FXML
    void requestFocus(KeyEvent event) {
        host.requestFocus();
    }
}
