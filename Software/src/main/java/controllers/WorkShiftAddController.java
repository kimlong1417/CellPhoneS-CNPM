package controllers;

import entities.WorkShift;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.AlertBoxHelper;
import utils.HibernateUtils;
import utils.StageHelper;
import utils.UUIDHelper;
import validation.WorkShiftValidation;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WorkShiftAddController implements Initializable {
    @FXML
    AnchorPane host;
    @FXML
    private TextField nameHolder;
    @FXML
    private ComboBox<String> timeInHolder;
    @FXML
    private ComboBox<String> timeOutHolder;
    @FXML
    private Label errorMessage;

    public static WorkShiftAddController instance;
    public WorkShiftAddController() { instance = this; }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add hours to ComboBox
        List<String> hours = new ArrayList<>();
        for (int i = 0; i <= 24; i++) {
            hours.add(i + ":00");
            hours.add(i + ":30");
        }
        timeInHolder.getItems().addAll(hours);
        timeOutHolder.getItems().addAll(hours);
    }

    @FXML
    void close(MouseEvent event) {
        StageHelper.closeStage(event);
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(false);
    }

    @FXML
    void save(ActionEvent event) {
        Session session;
        WorkShift workShift = new WorkShift();
        workShift.setId(UUIDHelper.generateType4UUID().toString());
        workShift.setName(nameHolder.getText());
        workShift.setStartTime(timeInHolder.getValue());
        workShift.setEndTime(timeOutHolder.getValue());

        List<String> validateInsert = WorkShiftValidation.validateInsert(workShift);
        if (validateInsert.size() == 0) {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(workShift);
            session.getTransaction().commit();
            session.close();

            // Show alert box
            AlertBoxHelper.showMessageBox("Thêm thành công");
            // Refresh content table
            WorkShiftCategoryController.getInstance().initialize(null, null);
            // Unhide host only when orders add is not show
            MainNavigatorController.instance.getHost().setDisable(false);
            // Close stage
            StageHelper.closeStage(event);
        } else {
            errorMessage.setText(validateInsert.get(0));
        }
    }

    @FXML
    void requestFocus(MouseEvent event) {
        host.requestFocus();
    }
}
