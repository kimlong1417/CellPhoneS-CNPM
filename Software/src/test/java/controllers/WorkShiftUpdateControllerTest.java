package controllers;

import entities.WorkShift;
import holders.WorkShiftHolder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class WorkShiftUpdateControllerTest extends ApplicationTest {
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

        WorkShiftHolder.getInstance().setWorkShift(new WorkShift("2f6653c1-8806-49d4-8a7b-2083485f061c", "Trưa", "13:00", "17:30", null, null));

        stage = new Stage();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/WorkShiftUpdate.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    // TC12
    @Test
    void initialize() {
        List<String> hours = new ArrayList<>();
        for (int i = 0; i <= 24; i++) {
            hours.add(i + ":00");
            hours.add(i + ":30");
        }

        List timeInValue = lookup("#timeInHolder").queryAs(ComboBox.class).getItems();
        List timeOutValue = lookup("#timeOutHolder").queryAs(ComboBox.class).getItems();
        assertEquals(hours, timeInValue);
        assertEquals(hours, timeOutValue);
    }

    @Test
    void close() {
        clickOn("#closeButton");
        assertFalse(WorkShiftUpdateController.instance.host.getScene().getWindow().isShowing());
    }

    // TC16
    @Test
    void delete() {
        // Ktra không cho xoá ca làm có đang được sử dụng
        Label errorMessage = lookup("#errorMessage").queryAs(Label.class);
        clickOn("#delete");
        assertEquals("Không được xoá ca làm đang được sử dụng", errorMessage.getText());
    }

    // TC14
    @Test
    void update() {
        Label errorMessage = lookup("#errorMessage").queryAs(Label.class);
        // Ktra đã điền tên chưa
        clickOn("#nameHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        press(KeyCode.DELETE).release(KeyCode.DELETE);
        clickOn("#update");
        assertEquals("Chưa điền tên ca làm", errorMessage.getText());
        // Ktra trùng tên
        clickOn("#nameHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("Sáng");
        clickOn("#update");
        assertEquals("Tên đã được sử dụng", errorMessage.getText());
        assertFalse(MainNavigatorController.instance.getHost().isDisabled());
    }

    // TC17
    @Test
    void requestFocus() {
        clickOn("#nameHolder");
        clickOn("#name");
        assertTrue(WorkShiftUpdateController.instance.host.isFocused());
    }
}