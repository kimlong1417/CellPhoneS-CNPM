package controllers;

import entities.Roles;
import entities.RolesDetail;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import repositories.RolesDetailRepository;
import repositories.RolesRepository;
import utils.HibernateUtils;
import utils.UUIDHelper;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class RolesAddControllerTest extends ApplicationTest {
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

        stage = new Stage();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/RolesAdd.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    // TC54
    @Test
    void close() {
        clickOn("#closeButton");
        Assertions.assertFalse(RolesAddController.instance.host.getScene().getWindow().isShowing());
        Assertions.assertFalse(MainNavigatorController.getInstance().getHost().isDisabled());
        sleep(500);
    }

    // TC56
    @Test
    void save() {
        Label errorMessage = lookup("#errorMessage").queryAs(Label.class);
        // TC82
        clickOn("#saveRoles");
        assertEquals("Chưa điền tên chức vụ", errorMessage.getText());
        // TC83
        clickOn("#nameHolder");
        write("Admin");
        clickOn("#saveRoles");
        assertEquals("Tên đã được sử dụng", errorMessage.getText());
        // TC84
        clickOn("#nameHolder");
        press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        String name = "Test_Name_" + UUIDHelper.generateType4UUID().toString();
        write(name);
        doubleClickOn(839 ,427);
        clickOn("#saveRoles");
        assertEquals("Tên đã được sử dụng", errorMessage.getText());
        assertFalse(RolesAddController.instance.host.getScene().getWindow().isShowing());
        sleep(500);

        Roles roles = RolesRepository.getByName(name);
        RolesDetailRepository.deleteByRoleId(roles.getId());

        Session session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(roles);
        session.getTransaction().commit();
        session.close();
    }

    // TC62
    @Test
    void requestFocus() {
        clickOn("#rolesLabel");
        clickOn("#nameHolder");
        clickOn("#rolesLabel");
        assertTrue(RolesAddController.instance.host.isFocused());
        sleep(500);
    }
}