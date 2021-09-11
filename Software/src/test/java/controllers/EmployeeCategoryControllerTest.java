package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeCategoryControllerTest extends ApplicationTest {
    Stage hostStage;
    Parent mainRoot;

    @Override
    public void init() throws Exception {
        FxToolkit.registerStage(Stage::new);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        hostStage = primaryStage;
        mainRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/MainNavigator.fxml")));
        hostStage.setScene(new Scene(this.mainRoot));
        hostStage.show();
        MainNavigatorController.instance.openEmployeeCategory(null);
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    // TC143
    @Test
    void insert() {
        clickOn("#openSave");
        assertTrue(MainNavigatorController.instance.getHost().isDisabled());
        assertTrue(EmployeeAddController.instance.host.getScene().getWindow().isShowing());
        sleep(600);
    }

    // TC145
    @Test
    void select() {
        doubleClickOn(563, 355);
        assertTrue(MainNavigatorController.instance.getHost().isDisabled());
        assertTrue(EmployeeUpdateController.instance.host.getScene().getWindow().isShowing());
        sleep(600);
    }
}