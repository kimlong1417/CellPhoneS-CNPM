package controllers;

import entities.Employee;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import repositories.EmployeeRepository;
import repositories.EmployeeRolesRepository;
import repositories.RolesRepository;
import utils.HibernateUtils;
import utils.UUIDHelper;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeAddControllerTest extends ApplicationTest {
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
        MainNavigatorController.instance.openEmployeeCategory(null);
        MainNavigatorController.instance.getHost().setDisable(true);

        stage = new Stage();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/EmployeeAdd.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    // TC138
    @Test
    void save() {
        Label errorMessage = lookup("#errorMessage").queryAs(Label.class);
        // TC216
        clickOn("#saveButton");
        assertEquals("Chưa điền tên", errorMessage.getText());

        // TC217
        clickOn("#nameHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("Admin");
        clickOn("#saveButton");
        assertEquals("Tên đã được sử dụng", errorMessage.getText());

        // TC218
        clickOn("#nameHolder");
        String name = UUIDHelper.generateType4UUID().toString();
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write(name);
        clickOn("#saveButton");
        assertEquals("Chưa điền số điện thoại", errorMessage.getText());

        // TC219
        clickOn("#phoneHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("d");
        clickOn("#saveButton");
        assertEquals("Số điện thoại phải là số", errorMessage.getText());

        // TC220
        clickOn("#phoneHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("123");
        clickOn("#saveButton");
        assertEquals("Số điện thoại phải có 10 chữ số", errorMessage.getText());

        // TC221
        clickOn("#phoneHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("1111111111");
        clickOn("#saveButton");
        assertEquals("Số điện thoại đã được sử dụng", errorMessage.getText());

        // TC222
        clickOn("#phoneHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write(String.valueOf((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L));
        clickOn("#saveButton");
        assertEquals("Chưa điền email", errorMessage.getText());

        // TC223
        clickOn("#emailHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("abc");
        clickOn("#saveButton");
        assertEquals("Email không hợp lệ", errorMessage.getText());

        // TC224
        clickOn("#emailHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("admin@gmail.com");
        clickOn("#saveButton");
        assertEquals("Email đã được sử dụng", errorMessage.getText());

        // TC225
        clickOn("#emailHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write(UUIDHelper.generateType4UUID().toString() + "@gmail.com");
        clickOn("#saveButton");
        assertEquals("Chưa điền mật khẩu", errorMessage.getText());

        //TC226
        clickOn("#passwordHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("123");
        clickOn("#saveButton");
        assertEquals("Chưa chọn ngày sinh", errorMessage.getText());

        // TC227
        clickOn(1185, 403);
        press(KeyCode.DOWN).release(KeyCode.DOWN).press(KeyCode.ENTER).release(KeyCode.ENTER);
        clickOn("#saveButton");
        assertFalse(EmployeeAddController.instance.host.getScene().getWindow().isShowing());
        assertFalse(MainNavigatorController.instance.getHost().isDisabled());

        // Delete saved test employee
        Employee employee = EmployeeRepository.getByName(name);
        Session session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(EmployeeRolesRepository.getByEmployeeId(employee.getId()));
        session.getTransaction().commit();
        session.close();

        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(employee);
        session.getTransaction().commit();
        session.close();
    }
}