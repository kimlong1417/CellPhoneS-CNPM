package controllers;

import com.jfoenix.controls.JFXCheckBox;
import entities.Employee;
import entities.WorkShift;
import entities.WorkTable;
import holders.WorkTableHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.Session;
import repositories.EmployeeRepository;
import repositories.WorkShiftRepository;
import utils.AlertBoxHelper;
import utils.HibernateUtils;
import utils.StageHelper;
import validation.WorkTableValidation;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WorkTableUpdateController implements Initializable {
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

    public static WorkTableUpdateController instance;
    public WorkTableUpdateController() { instance = this; }

    // Get WorkTable from WorkTableCategory select(MouseEvent event)
    WorkTable chosenWorkTable = WorkTableHolder.getInstance().getWorkTable();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Employee> allEmployee = EmployeeRepository.getAll();
        List<WorkShift> allShifts = WorkShiftRepository.getAll();
        // Fill textfield autocomplete
        TextFields.bindAutoCompletion(employeeHolder, allEmployee.stream().map(Employee::getFullName).collect(Collectors.toList()));
        TextFields.bindAutoCompletion(shiftHolder, allShifts.stream().map(WorkShift::getName).collect(Collectors.toList()));

        // Set data
        employeeHolder.setText(chosenWorkTable.getEmployee().getFullName());
        shiftHolder.setText(chosenWorkTable.getWorkShift().getName());
        List<String> workDaysOfWeek = Arrays.asList(chosenWorkTable.getDayOfWeek().split("-"));
        for (String item : workDaysOfWeek) {
            switch (item) {
                case "T2" -> t2CheckBox.setSelected(true);
                case "T3" -> t3CheckBox.setSelected(true);
                case "T4" -> t4CheckBox.setSelected(true);
                case "T5" -> t5CheckBox.setSelected(true);
                case "T6" -> t6CheckBox.setSelected(true);
                case "T7" -> t7CheckBox.setSelected(true);
                case "CN" -> cnCheckBox.setSelected(true);
            }
        }
    }

    @FXML
    void close(MouseEvent event) {
        StageHelper.closeStage(event);
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(false);
    }

    @FXML
    void delete(ActionEvent event) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(chosenWorkTable);
        session.getTransaction().commit();
        session.close();

        // Show alert box
        AlertBoxHelper.showMessageBox("Xoá thành công");
        // Refresh content table
        WorkTableCategoryController.getInstance().initialize(null, null);
        // Unhide host only when orders add is not show
        MainNavigatorController.instance.getHost().setDisable(false);
        // Close stage
        StageHelper.closeStage(event);
        // Clear holder
        WorkTableHolder.getInstance().setWorkTable(null);
    }

    @FXML
    void requestFocus(MouseEvent event) {
        host.requestFocus();
    }

    @FXML
    void update(ActionEvent event) {
        Employee employee = EmployeeRepository.getByEmployeeName(employeeHolder.getText());
        WorkShift workShift = WorkShiftRepository.getByName(shiftHolder.getText());

        List<JFXCheckBox> checkBoxList = new ArrayList<>(Arrays.asList(t2CheckBox, t3CheckBox, t4CheckBox, t5CheckBox, t6CheckBox, t7CheckBox, cnCheckBox));
        String workDaysInWeek = "";
        for (JFXCheckBox jfxCheckBox : checkBoxList) {
            if (jfxCheckBox.isSelected()) {
                workDaysInWeek += jfxCheckBox.getText() + "-";
            }
        }
        workDaysInWeek = workDaysInWeek.substring(0, workDaysInWeek.length() - 1);

        chosenWorkTable.setEmployee(employee);
        chosenWorkTable.setWorkShift(workShift);
        chosenWorkTable.setDayOfWeek(workDaysInWeek);

        List<String> validateInsert = WorkTableValidation.validateUpdate(chosenWorkTable);
        if (validateInsert.size() == 0) {
            Session session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(chosenWorkTable);
            session.getTransaction().commit();
            session.close();

            // Show alert box
            AlertBoxHelper.showMessageBox("Cập nhật thành công");
            // Refresh content table
            WorkTableCategoryController.getInstance().initialize(null, null);
            // Unhide host only when orders add is not show
            MainNavigatorController.instance.getHost().setDisable(false);
            // Close stage
            StageHelper.closeStage(event);
            // Clear holder
            WorkTableHolder.getInstance().setWorkTable(chosenWorkTable);
        } else {
            errorMessage.setText(validateInsert.get(0));
        }
    }
}
