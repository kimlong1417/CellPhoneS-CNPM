package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class WorkShiftAddControllerTest extends ApplicationTest {
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
        MainNavigatorController.instance.openShiftTable();

        stage = new Stage();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/WorkShiftAdd.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    // TC10
    @Test
    void close() {
        clickOn("#closeButton");
        assertFalse(WorkShiftAddController.instance.host.getScene().getWindow().isShowing());
    }

    // TC9
    @Test
    void save() {
        Label errorMessage = lookup("#errorMessage").queryAs(Label.class);
        // Ktra đã điền tên chưa
        clickOn("#saveShift");
        assertEquals("Chưa điền tên ca làm", errorMessage.getText());
        // Ktra trùng tên
        clickOn("#nameHolder");
        write("Trưa");
        clickOn("#saveShift");
        assertEquals("Tên ca làm đã được sử dụng", errorMessage.getText());
        assertFalse(MainNavigatorController.instance.getHost().isDisabled());
    }

    // TC11
    @Test
    void requestFocus() {
        clickOn("#nameHolder");
        clickOn("#name");
        assertTrue(WorkShiftAddController.instance.host.isFocused());
    }
}