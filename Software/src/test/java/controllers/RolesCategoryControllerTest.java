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

import static org.junit.jupiter.api.Assertions.*;

class RolesCategoryControllerTest extends ApplicationTest {
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
        MainNavigatorController.instance.openRolesCategory(null);
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    // TC64
    @Test
    void insert() {
        clickOn("#openInsert");
        assertTrue(MainNavigatorController.instance.getHost().isDisabled());
        assertTrue(RolesAddController.instance.host.getScene().getWindow().isShowing());
        sleep(700);
    }

    @Test
    void requestFocus() {
        TextField searchBar = lookup("#searchBar").queryAs(TextField.class);
        clickOn(searchBar);
        clickOn("#rolesCategoryLabel");
        assertFalse(searchBar.isFocused());
        sleep(700);
    }
}