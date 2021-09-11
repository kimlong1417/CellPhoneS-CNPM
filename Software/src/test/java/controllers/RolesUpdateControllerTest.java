package controllers;

import entities.Roles;
import holders.RolesHolder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import repositories.RolesDetailRepository;
import repositories.RolesRepository;
import utils.HibernateUtils;
import utils.UUIDHelper;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class RolesUpdateControllerTest extends ApplicationTest {
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
        MainNavigatorController.instance.openRolesCategory(null);
        MainNavigatorController.instance.getHost().setDisable(true);

        RolesHolder.getInstance().setRoles(new Roles("0bd8f140-bcf4-492a-a18e-4868bbe8abd1", "Test_Name_abaf4ca0-9f24-4546-b12a-be8ee563db6c", null, null));

        stage = new Stage();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/RolesUpdate.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    // TC71
    @Test
    void close() {
        clickOn("#closeButton");
        assertFalse(RolesUpdateController.instance.host.getScene().getWindow().isShowing());
        assertFalse(MainNavigatorController.instance.getHost().isDisabled());
        sleep(600);
    }

    // TC80
    @Test
    void update() {
        Label errorMessage = lookup("#errorMessage").queryAs(Label.class);
        // TC85
        clickOn("#nameHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        press(KeyCode.DELETE).release(KeyCode.DELETE);
        clickOn("#updateButton");
        assertEquals("Chưa điền tên", errorMessage.getText());
        // TC86
        clickOn("#nameHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        write("Admin");
        clickOn("#updateButton");
        assertEquals("Tên đã được sử dụng", errorMessage.getText());
        // TC87
        clickOn("#nameHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        String name = "Test_Update_Name" + UUIDHelper.generateType4UUID().toString();
        write(name);
        clickOn("#updateButton");
        assertEquals("Tên đã được sử dụng", errorMessage.getText());
        sleep(600);

        Roles roles = RolesRepository.getByName(name);
        RolesDetailRepository.deleteByRoleId(roles.getId());

        Session session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(roles);
        session.getTransaction().commit();
        session.close();
    }

    @Test
    void requestFocus() {
        clickOn("#rolesUpdateLabel");
        clickOn("#nameHolder");
        clickOn("#rolesUpdateLabel");
        assertTrue(RolesUpdateController.instance.host.isFocused());
        sleep(600);
    }
}