package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import repositories.MerchandiseRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MerchandiseAddControllerTest extends ApplicationTest {
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
        MainNavigatorController.instance.openMerchandiseCategory(null);

        stage = new Stage();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/MerchandiseAdd.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    // TC1 -> TC2
    @Test
    void initialize() {
        List<String> allMerchandiseTypes = MerchandiseRepository.getAllMerchandiseTypes();
        TextField typeHolder = lookup("#typeHolder").queryAs(TextField.class);
        clickOn(typeHolder);
        if (allMerchandiseTypes == null) {
            write("d");
            sleep(700);
            press(KeyCode.ENTER).release(KeyCode.ENTER);
            assertEquals(typeHolder.getText(), "d");
        } else {
            for (String item : allMerchandiseTypes) {
                clickOn(typeHolder);
                press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
                write("" + item.charAt(0) + item.charAt(1));
                sleep(700);
                press(KeyCode.ENTER).release(KeyCode.ENTER);
                assertEquals(typeHolder.getText(), item);
            }
        }
    }

    // TC5
    @Test
    void close() {
        clickOn("#closeButton");
        Assertions.assertFalse(MerchandiseAddController.getInstance().host.getScene().getWindow().isShowing());
    }

    // TC3 -> TC4
    @Test
    void save() {
        Label errorMessage = lookup("#errorMessage").queryAs(Label.class);
        //TC256
        clickOn("#saveButton");
        assertEquals("Chưa điền tên", errorMessage.getText());
        //TC257
        clickOn("#nameHolder");
        write("Oppo A73");
        clickOn("#saveButton");
        assertEquals("Tên đã được sử dụng", errorMessage.getText());
        //TC258
        clickOn("#nameHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("abc");
        clickOn("#saveButton");
        assertEquals("Chưa điền thương hiệu", errorMessage.getText());
        //TC259
        clickOn("#branchHolder");
        write("Apple");
        clickOn("#saveButton");
        assertEquals("Chưa điền loại", errorMessage.getText());
        //TC260
        clickOn("#typeHolder");
        write("d");
        sleep(900);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        clickOn("#saveButton");
        assertEquals("Chưa điền giá mua", errorMessage.getText());
        //TC261
        clickOn("#importPriceHolder");
        write("d");
        clickOn("#saveButton");
        assertEquals("Giá mua phải là số", errorMessage.getText());
        //TC262
        clickOn("#importPriceHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("100000");
        clickOn("#saveButton");
        assertEquals("Chưa điền giá bán", errorMessage.getText());
        //TC263
        clickOn("#priceHolder");
        write("d");
        clickOn("#saveButton");
        assertEquals("Giá bán phải là số", errorMessage.getText());
    }

    // TC6
    @Test
    void requestFocus() {
        clickOn("#nameHolder");
        clickOn("#name");
        assertTrue(MerchandiseAddController.instance.host.isFocused());
    }
}