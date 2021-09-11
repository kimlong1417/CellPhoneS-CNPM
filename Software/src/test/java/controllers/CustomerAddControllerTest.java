package controllers;

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
import repositories.CustomerRepository;
import utils.HibernateUtils;
import utils.UUIDHelper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CustomerAddControllerTest extends ApplicationTest {
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

        stage = new Stage();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/CustomerAdd.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    // TC120
    @Test
    void initialize() {
        ComboBox<String> typeHolder = lookup("#typeHolder").queryAs(ComboBox.class);
        assertTrue(typeHolder.getItems().containsAll(Arrays.asList("Khách hàng", "Nhà cung cấp")));
        clickOn(typeHolder);
        sleep(600);
    }

    // TC121
    @Test
    void save() {
        Label errorMessage = lookup("#errorMessage").queryAs(Label.class);
        // TC193
        clickOn("#addButton");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        assertEquals("Chưa điền tên", errorMessage.getText());
        // TC194
        clickOn("#nameHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("Huy");
        clickOn("#addButton");
        assertEquals("Tên đã được sử dụng", errorMessage.getText());
        // TC195
        clickOn("#nameHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        String name = UUIDHelper.generateType4UUID().toString();
        write(name);
        clickOn("#addButton");
        assertEquals("Chưa điền số điện thoại", errorMessage.getText());
        // TC196
        clickOn("#phoneHolder");
        write("A");
        clickOn("#addButton");
        assertEquals("Số điện thoại phải là số", errorMessage.getText());
        // TC197
        clickOn("#phoneHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("123");
        clickOn("#addButton");
        assertEquals("Số điện thoại phải có 10 chữ số", errorMessage.getText());
        // TC198
        clickOn("#phoneHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("1111111111");
        clickOn("#addButton");
        assertEquals("Số điện thoại đã được sử dụng", errorMessage.getText());
        // TC199
        clickOn("#phoneHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        String phoneNumber = String.valueOf((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);
        write(phoneNumber);
        clickOn("#addButton");
        assertEquals("Chưa chọn loại khách hàng", errorMessage.getText());
        // TC200
        clickOn("#typeHolder");
        press(KeyCode.DOWN).release(KeyCode.DOWN).press(KeyCode.ENTER).release(KeyCode.ENTER);
        clickOn("#addButton");
        assertEquals("Chưa điền email", errorMessage.getText());
        // TC201
        clickOn("#emailHolder");
        write("abcd");
        clickOn("#addButton");
        assertEquals("Email không hợp lệ", errorMessage.getText());
        // TC202
        clickOn("#emailHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("a@gmail.com");
        clickOn("#addButton");
        assertEquals("Email đã được sử dụng", errorMessage.getText());
        // TC203
        clickOn("#emailHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write(UUIDHelper.generateType4UUID().toString() + "@gmail.com");
        clickOn("#addButton");
        assertFalse(CustomerAddController.instance.host.getScene().getWindow().isShowing());
        assertFalse(MainNavigatorController.instance.getHost().isDisabled());
        sleep(600);

        Session session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(CustomerRepository.getByName(name));
        session.getTransaction().commit();
        session.close();
    }
}