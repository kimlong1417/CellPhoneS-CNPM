package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkShiftCategoryControllerTest extends ApplicationTest {
    Stage stage;
    Parent sceneRoot;

    @Override
    public void init() throws Exception {
        FxToolkit.registerStage(Stage::new);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        sceneRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/MainNavigator.fxml")));
        stage.setScene(new Scene(sceneRoot));
        stage.show();
        MainNavigatorController.instance.openShiftTable();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    // TC20
    @Test
    void save() {
        clickOn("#save");
        assertTrue(WorkShiftAddController.instance.host.getScene().getWindow().isShowing());
        assertTrue(MainNavigatorController.instance.getHost().isDisabled());
    }

    // TC16
    @Test
    void requestFocus() {
        TextField searchBar = lookup("#searchBar").queryAs(TextField.class);
        clickOn(searchBar);
        clickOn("#workShift");
        assertFalse(searchBar.isFocused());
    }
}