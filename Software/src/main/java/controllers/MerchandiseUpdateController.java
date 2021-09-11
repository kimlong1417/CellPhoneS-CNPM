package controllers;

import com.jfoenix.controls.JFXButton;
import entities.Merchandise;
import holders.MerchandiseHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import repositories.MerchandiseRepository;
import utils.AlertBoxHelper;
import utils.HibernateUtils;
import utils.NumberHelper;
import utils.StageHelper;
import validation.MerchandiseValidation;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MerchandiseUpdateController implements Initializable {
    @FXML
    private AnchorPane host;
    @FXML
    private TextField nameHolder;
    @FXML
    private TextField priceHolder;
    @FXML
    private TextField importPriceHolder;
    @FXML
    private TextField typeHolder;
    @FXML
    private TextField branchHolder;
    @FXML
    private TextField quantityHolder;
    @FXML
    private Label errorMessage;

    // Get Merchandise from MerchandiseCategoryController select(MouseEvent event)
    Merchandise merchandise = MerchandiseHolder.getInstance().getMerchandise();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set Merchandise in update window
        nameHolder.setText(merchandise.getName());
        priceHolder.setText(merchandise.getPrice());
        importPriceHolder.setText(merchandise.getImportPrice());
        typeHolder.setText(merchandise.getType());
        branchHolder.setText(merchandise.getBranch());
        quantityHolder.setText(merchandise.getQuantity().toString());
        // Set autocomplete for merchandise type
        List<String> allMerchandiseTypes = MerchandiseRepository.getAllMerchandiseTypes();
        if (allMerchandiseTypes != null) {
            AutoCompletionBinding<String> tHolder = TextFields.bindAutoCompletion(typeHolder, allMerchandiseTypes);
        }
    }

    @FXML
    void update(ActionEvent event) {
        Session session;
        Merchandise merchandise = MerchandiseHolder.getInstance().getMerchandise();
        merchandise.setName(nameHolder.getText());
        merchandise.setType(typeHolder.getText());
        merchandise.setBranch(branchHolder.getText());
        merchandise.setPrice(NumberHelper.removeComma(priceHolder.getText()));
        merchandise.setImportPrice(NumberHelper.removeComma(importPriceHolder.getText()));
        merchandise.setQuantity(!quantityHolder.getText().isEmpty() ? Integer.parseInt(quantityHolder.getText()) : 0);

        List<String> validateUpdate = MerchandiseValidation.validateUpdate(merchandise);
        if (validateUpdate.size() == 0) {
            // Update customer info
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(merchandise);
            session.getTransaction().commit();
            session.close();

            // Close stage
            StageHelper.closeStage(event);
            // Show alert box
            AlertBoxHelper.showMessageBox("Cập nhật thành công");
            // Refresh content table
            MerchandiseCategoryController.getInstance().refresh();
            // Set merchandise holder
            MerchandiseHolder.getInstance().setMerchandise(merchandise);
            // Unhide host
            MainNavigatorController.instance.getHost().setDisable(false);
        } else {
            errorMessage.setText(validateUpdate.get(0));
        }
    }

    @FXML
    void delete(ActionEvent event) {
        // Delete customer
        Merchandise merchandise = MerchandiseHolder.getInstance().getMerchandise();
        Session session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(merchandise);
        session.getTransaction().commit();
        session.close();

        // Show alert box
        AlertBoxHelper.showMessageBox("Xoá thành công");
        // Close stage
        StageHelper.closeStage(event);
        // Refresh content table
        MerchandiseCategoryController.getInstance().refresh();
        // Clear customer holder
        MerchandiseHolder.getInstance().setMerchandise(null);
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(false);
    }

    @FXML
    void close(MouseEvent event) {
        // Clear merchandise holder
        MerchandiseHolder.getInstance().setMerchandise(null);
        StageHelper.closeStage(event);
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(false);
    }

    @FXML
    void requestFocus(MouseEvent event) {
        host.requestFocus();
    }
}
