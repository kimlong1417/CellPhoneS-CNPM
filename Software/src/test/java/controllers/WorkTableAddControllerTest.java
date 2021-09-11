package controllers;

import entities.Employee;
import entities.WorkShift;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import repositories.EmployeeRepository;
import repositories.WorkShiftRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkTableAddControllerTest extends ApplicationTest {
    Stage stage;
    Stage stage2;
    Parent sceneRoot;
    Parent root;

    @Override
    public void init() throws Exception {
        FxToolkit.registerStage(Stage::new);
        FxToolkit.registerStage(Stage::new);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        sceneRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/MainNavigator.fxml")));
        stage.setScene(new Scene(sceneRoot));
        stage.show();
        MainNavigatorController.instance.openTimeTable(null);

        stage2 = new Stage();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/WorkTableAdd.fxml")));
        stage2.setScene(new Scene(root));
        stage2.show();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    // TC17
    @Test
    void initialize() {
        List<String> allEmployees = null;
        List<String> allWorkShifts = null;
        if (EmployeeRepository.getAll() != null) {
           allEmployees = EmployeeRepository.getAll().stream().map(Employee::getFullName).collect(Collectors.toList());
        }
        if (WorkShiftRepository.getAll() != null) {
            allWorkShifts = WorkShiftRepository.getAll().stream().map(WorkShift::getName).collect(Collectors.toList());
        }
        TextField employeeHolder = lookup("#employeeHolder").queryAs(TextField.class);
        if (allEmployees != null && allEmployees.size() > 0) {
            for (String item : allEmployees) {
                clickOn(employeeHolder);
                press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
                write("" + item.charAt(0) + item.charAt(1));
                sleep(700);
                press(KeyCode.ENTER).release(KeyCode.ENTER);
                assertEquals(item, employeeHolder.getText());
            }
        } else {
            write("d");
            sleep(700);
            press(KeyCode.ENTER).release(KeyCode.ENTER);
            assertEquals("d", employeeHolder.getText());
        }
        TextField shiftHolder = lookup("#shiftHolder").queryAs(TextField.class);
        if (allWorkShifts != null && allWorkShifts.size() > 0) {
            for (String item : allWorkShifts) {
                clickOn(shiftHolder);
                press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
                write("" + item.charAt(0) + item.charAt(1));
                sleep(700);
                press(KeyCode.ENTER).release(KeyCode.ENTER);
                assertEquals(item, shiftHolder.getText());
            }
        } else {
            write("d");
            sleep(700);
            press(KeyCode.ENTER).release(KeyCode.ENTER);
            assertEquals("d", shiftHolder.getText());
        }
    }

    // TC30
    @Test
    void close() {
        clickOn("#closeButton");
        Assertions.assertFalse(WorkTableAddController.instance.host.getScene().getWindow().isShowing());
    }

    // TC31
    @Test
    void requestFocus() {
        clickOn("#employeeHolder");
        clickOn("#employee");
        assertTrue(WorkTableAddController.instance.host.isFocused());
    }
}