package controllers;

import entities.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import repositories.EmployeeRepository;
import utils.BCryptHelper;
import utils.HibernateUtils;
import utils.StageHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LoginController {

    @FXML
    private Label status;
    @FXML
    private TextField userEmail;
    @FXML
    private PasswordField userPassword;

    public Employee curEmployee;

    // For other class to access this class
    public static LoginController instance;
    public LoginController() {
        instance = this;
    }
    public static LoginController getInstance() {
        return instance;
    }
    ///

    @FXML
    void close(MouseEvent mouseEvent) {
        StageHelper.closeStage(mouseEvent);
    }

    @FXML
    void minimizeWindow(MouseEvent event) {
        StageHelper.minimizeStage(event);
    }

    @FXML
    void login(ActionEvent actionEvent) {
        try {
            Employee employee = EmployeeRepository.getByEmail(userEmail.getText());
            List<String> validateLogin = validateLogin(employee);
            if (validateLogin.size() == 0) {
                // Set logged in employee
                curEmployee = employee;
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/MainNavigator.fxml")));
                StageHelper.closeStage(actionEvent);
                StageHelper.startStage(root);
            } else {
                status.setText(validateLogin.get(0));
            }
        } catch (Exception ex) {
            status.setText("Lỗi đăng nhập");
        }
    }

    @FXML
    void enterLogin(ActionEvent actionEvent) {
        this.login(actionEvent);
    }

    List<String> validateLogin(Employee employee) {
        List<String> msg = new ArrayList<>();

        if (employee == null) {
            msg.add("Sai email");
        } else if (!BCryptHelper.check(userPassword.getText(), employee.getPassword())) {
            msg.add("Sai mật khẩu");
        }

        return msg;
    }
}
