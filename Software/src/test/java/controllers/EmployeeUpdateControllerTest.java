package controllers;

import entities.Employee;
import holders.EmployeeHolder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import repositories.EmployeeRepository;
import repositories.EmployeeRolesRepository;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeUpdateControllerTest extends ApplicationTest {
    Stage hostStage;
    Stage stage;
    Parent mainRoot;
    Parent root;

    @Override
    public void init() throws Exception {
        FxToolkit.registerStage(Stage::new);
        FxToolkit.registerStage(Stage::new);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        hostStage = primaryStage;
        mainRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/MainNavigator.fxml")));
        hostStage.setScene(new Scene(this.mainRoot));
        hostStage.show();
        MainNavigatorController.instance.openCustomerCategory(null);
        MainNavigatorController.instance.getHost().setDisable(true);
        EmployeeHolder.getInstance().setEmployee(EmployeeRepository.getByName("ssss"));

        stage = new Stage();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/EmployeeUpdate.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    // TC148
    @Test
    void initialize() {
        Employee employee = new Employee(EmployeeHolder.getInstance().getEmployee());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        TextField nameHolder = lookup("#nameHolder").queryAs(TextField.class);
        TextField phoneHolder = lookup("#phoneHolder").queryAs(TextField.class);
        TextField emailHolder = lookup("#emailHolder").queryAs(TextField.class);
        DatePicker dateOfBirthHolder = lookup("#dateOfBirthHolder").queryAs(DatePicker.class);
        ComboBox<String> roleHolder = lookup("#roleHolder").queryAs(ComboBox.class);

        assertEquals(employee.getFullName(), nameHolder.getText());
        assertEquals(employee.getPhone(), phoneHolder.getText());
        assertEquals(employee.getEmail(), emailHolder.getText());
        assertEquals(employee.getBirthDay().toString(), dateOfBirthHolder.getValue().format(formatter));
        assertEquals(EmployeeRolesRepository.getByEmployeeId(employee.getId()).getRoles().getName(), roleHolder.getValue());
    }

    // TC150
    @Test
    void update() {
        Label errorMessage = lookup("#errorMessage").queryAs(Label.class);
        // TC228
        clickOn("#nameHolder");
        press(KeyCode.END).release(KeyCode.END).press(KeyCode.SHIFT).press(KeyCode.HOME).release(KeyCode.HOME).release(KeyCode.SHIFT);
        press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        clickOn("#updateButton");
        assertEquals("Chưa điền tên", errorMessage.getText());

        // TC229
        clickOn("#nameHolder");
        write("Admin");
        clickOn("#updateButton");
        assertEquals("Tên đã được sử dụng", errorMessage.getText());

        // TC230
        clickOn("#nameHolder");
        press(KeyCode.END).release(KeyCode.END).press(KeyCode.SHIFT).press(KeyCode.HOME).release(KeyCode.HOME).release(KeyCode.SHIFT);
        press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        write("ssss");
        clickOn("#phoneHolder");
        press(KeyCode.END).release(KeyCode.END).press(KeyCode.SHIFT).press(KeyCode.HOME).release(KeyCode.HOME).release(KeyCode.SHIFT);
        press(KeyCode.DELETE).release(KeyCode.DELETE);
        clickOn("#updateButton");
        assertEquals("Chưa điền số điện thoại", errorMessage.getText());

        // TC231
        clickOn("#phoneHolder");
        write("a");
        clickOn("#updateButton");
        assertEquals("Số điện thoại phải là số", errorMessage.getText());

        // TC232
        clickOn("#phoneHolder");
        press(KeyCode.END).release(KeyCode.END).press(KeyCode.SHIFT).press(KeyCode.HOME).release(KeyCode.HOME).release(KeyCode.SHIFT);
        write("123");
        clickOn("#updateButton");
        assertEquals("Số điện thoại phải có 10 chữ số", errorMessage.getText());

        // TC233
        clickOn("#phoneHolder");
        press(KeyCode.END).release(KeyCode.END).press(KeyCode.SHIFT).press(KeyCode.HOME).release(KeyCode.HOME).release(KeyCode.SHIFT);
        write("0908706541");
        clickOn("#updateButton");
        assertEquals("Số điện thoại đã được sử dụng", errorMessage.getText());

        // TC234
        clickOn("#phoneHolder");
        press(KeyCode.END).release(KeyCode.END).press(KeyCode.SHIFT).press(KeyCode.HOME).release(KeyCode.HOME).release(KeyCode.SHIFT);
        write("1111111111");
        clickOn("#emailHolder");
        press(KeyCode.END).release(KeyCode.END).press(KeyCode.SHIFT).press(KeyCode.HOME).release(KeyCode.HOME).release(KeyCode.SHIFT);
        press(KeyCode.DELETE).release(KeyCode.DELETE);
        clickOn("#updateButton");
        assertEquals("Chưa điền email", errorMessage.getText());

        // TC235
        clickOn("#emailHolder");
        press(KeyCode.END).release(KeyCode.END).press(KeyCode.SHIFT).press(KeyCode.HOME).release(KeyCode.HOME).release(KeyCode.SHIFT);
        write("abc");
        clickOn("#updateButton");
        assertEquals("Email không hợp lệ", errorMessage.getText());

        // TC236
        clickOn("#emailHolder");
        press(KeyCode.END).release(KeyCode.END).press(KeyCode.SHIFT).press(KeyCode.HOME).release(KeyCode.HOME).release(KeyCode.SHIFT);
        write("admin@gmail.com");
        clickOn("#updateButton");
        assertEquals("Email đã được sử dụng", errorMessage.getText());
    }
}