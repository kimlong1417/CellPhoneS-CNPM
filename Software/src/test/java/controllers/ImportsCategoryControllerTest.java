package controllers;

import entities.Employee;
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

class ImportsCategoryControllerTest extends ApplicationTest {
    Stage hostStage;
    Parent mainRoot;

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
        MainNavigatorController.instance.openImportsCategory(null);
        FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/Login.fxml")));
        LoginController.getInstance().curEmployee = new Employee();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    // TC173
    @Test
    void save() {
        clickOn("#openSave");
        assertTrue(MainNavigatorController.instance.getHost().isDisabled());
        assertTrue(ImportsAddController.instance.host.getScene().getWindow().isShowing());
    }
}