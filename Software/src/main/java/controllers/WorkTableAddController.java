package controllers;

import com.jfoenix.controls.JFXCheckBox;
import entities.Employee;
import entities.WorkShift;
import entities.WorkTable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import repositories.EmployeeRepository;
import repositories.WorkShiftRepository;
import utils.AlertBoxHelper;
import utils.HibernateUtils;
import utils.StageHelper;
import utils.UUIDHelper;
import validation.WorkTableValidation;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WorkTableAddController implements Initializable {
    @FXML
    AnchorPane host;
    @FXML
    private TextField employeeHolder;
    @FXML
    private TextField shiftHolder;
    @FXML
    private Label errorMessage;
    @FXML
    private JFXCheckBox t2CheckBox;
    @FXML
    private JFXCheckBox t6CheckBox;
    @FXML
    private JFXCheckBox t7CheckBox;
    @FXML
    private JFXCheckBox t5CheckBox;
    @FXML
    private JFXCheckBox t4CheckBox;
    @FXML
    private JFXCheckBox t3CheckBox;
    @FXML
    private JFXCheckBox cnCheckBox;

    public static WorkTableAddController instance;
    public WorkTableAddController() { instance = this; }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Employee> allEmployee = EmployeeRepository.getAll();
        List<WorkShift> allShifts = WorkShiftRepository.getAll();
        // Fill textfield autocomplete
        TextFields.bindAutoCompletion(employeeHolder, allEmployee.stream().map(Employee::getFullName).collect(Collectors.toList()));
        TextFields.bindAutoCompletion(shiftHolder, allShifts.stream().map(WorkShift::getName).collect(Collectors.toList()));
    }

    @FXML
    void close(MouseEvent event) {
        StageHelper.closeStage(event);
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(false);
    }

    @FXML
    void save(ActionEvent event) {
        Session session;
        Employee employee = EmployeeRepository.getByEmployeeName(employeeHolder.getText());
        WorkShift workShift = WorkShiftRepository.getByName(shiftHolder.getText());

        List<JFXCheckBox> checkBoxList = new ArrayList<>(Arrays.asList(t2CheckBox, t3CheckBox, t4CheckBox, t5CheckBox, t6CheckBox, t7CheckBox, cnCheckBox));
        String workDaysInWeek = "";
        for (int i = 0; i < checkBoxList.size(); i++) {
            if (checkBoxList.get(i).isSelected()) {
                workDaysInWeek += checkBoxList.get(i).getText() + "-";
            }
        }
        workDaysInWeek = workDaysInWeek.substring(0, workDaysInWeek.length() - 1);

        WorkTable workTable = new WorkTable();
        workTable.setId(UUIDHelper.generateType4UUID().toString());
        workTable.setEmployee(employee);
        workTable.setWorkShift(workShift);
        workTable.setDayOfWeek(workDaysInWeek);

        List<String> validateInsert = WorkTableValidation.validateInsert(workTable);
        if (validateInsert.size() == 0) {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(workTable);
            session.getTransaction().commit();
            session.close();

            // Show alert box
            AlertBoxHelper.showMessageBox("Thêm thành công");
            // Refresh content table
            WorkTableCategoryController.getInstance().initialize(null, null);
            // Unhide host only when orders add is not show
            MainNavigatorController.instance.getHost().setDisable(false);
            // Close stage
            StageHelper.closeStage(event);
        } else {
            errorMessage.setText(validateInsert.get(0));
        }
    }

    @FXML
    void requestFocus(MouseEvent event) {
        host.requestFocus();
    }
}
