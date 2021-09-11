package controllers;

import holders.CustomerHolder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import repositories.CustomerRepository;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CustomerUpdateControllerTest extends ApplicationTest {
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
        CustomerHolder.getInstance().setCustomer(CustomerRepository.getByName("AbCD"));

        stage = new Stage();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/CustomerUpdate.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    // TC133
    @Test
    void update() {
        Label errorMessage = lookup("#errorMessage").queryAs(Label.class);
        // TC204
        clickOn("#nameHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL).press(KeyCode.DELETE).release(KeyCode.DELETE);
        clickOn("#updateButton");
        assertEquals("Chưa điền tên", errorMessage.getText());
        // TC205
        clickOn("#nameHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("Huy");
        clickOn("#updateButton");
        assertEquals("Tên đã được sử dụng", errorMessage.getText());
        // TC206
        clickOn("#nameHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("AbCD");
        clickOn("#phoneHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL).press(KeyCode.DELETE).release(KeyCode.DELETE);
        clickOn("#updateButton");
        assertEquals("Chưa điền số điện thoại", errorMessage.getText());
        // TC207
        clickOn("#phoneHolder");
        write("A");
        clickOn("#updateButton");
        assertEquals("Số điện thoại phải là số", errorMessage.getText());
        // TC208
        clickOn("#phoneHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("123");
        clickOn("#updateButton");
        assertEquals("Số điện thoại phải có 10 chữ số", errorMessage.getText());
        // TC209
        clickOn("#phoneHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("1111111111");
        clickOn("#updateButton");
        assertEquals("Số điện thoại đã được sử dụng", errorMessage.getText());
        // TC210
        clickOn("#phoneHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("1539874069");
        clickOn("#emailHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL).press(KeyCode.DELETE).release(KeyCode.DELETE);
        clickOn("#updateButton");
        assertEquals("Chưa điền email", errorMessage.getText());
        // TC212
        clickOn("#emailHolder");
        write("abcd");
        clickOn("#updateButton");
        assertEquals("Email không hợp lệ", errorMessage.getText());
        // TC213
        clickOn("#emailHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("hazle2016@gmail.com");
        clickOn("#updateButton");
        assertEquals("Email đã được sử dụng", errorMessage.getText());
        // TC214
        clickOn("#emailHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("abcd@gmail.com");
        clickOn("#updateButton");
        assertFalse(CustomerUpdateController.instance.host.getScene().getWindow().isShowing());
        assertFalse(MainNavigatorController.instance.getHost().isDisabled());
        sleep(600);
    }
}