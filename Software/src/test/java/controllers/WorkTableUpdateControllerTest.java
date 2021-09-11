package controllers;

import entities.Employee;
import entities.WorkShift;
import entities.WorkTable;
import holders.WorkTableHolder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import utils.HibernateUtils;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkTableUpdateControllerTest extends ApplicationTest {
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
        hostStage.setScene(new Scene(mainRoot));
        hostStage.show();

        MainNavigatorController.instance.openTimeTable(null);
        Session session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        WorkTableHolder.getInstance().setWorkTable(new WorkTable("03d06401-05ec-4ccb-a0de-fa6f13c08852", session.get(Employee.class, "94485f3a-976b-4359-a41a-e4bea52ec5f5"), session.get(WorkShift.class, "2f6653c1-8806-49d4-8a7b-2083485f061c"), "T3-T7"));
        session.getTransaction().commit();
        session.close();

        stage = new Stage();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/WorkTableUpdate.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    // TC42
    @Test
    void close() {
        clickOn("#closeButton");
        assertFalse(WorkTableUpdateController.instance.host.getScene().getWindow().isShowing());
        assertFalse(MainNavigatorController.instance.getHost().isDisabled());
    }

    // TC43
    @Test
    void requestFocus() {
        clickOn("#employeeHolder");
        clickOn("#name");
        assertTrue(WorkTableUpdateController.instance.host.isFocused());
        clickOn("#shiftHolder");
        clickOn("#name");
        assertTrue(WorkTableUpdateController.instance.host.isFocused());
    }
}