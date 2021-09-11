package controllers;

import entities.Merchandise;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.Session;
import repositories.MerchandiseRepository;
import utils.AlertBoxHelper;
import utils.HibernateUtils;
import utils.StageHelper;
import utils.UUIDHelper;
import validation.MerchandiseValidation;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MerchandiseAddController implements Initializable {
    @FXML
    AnchorPane host;
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

    // For other class call function from this class
    public static MerchandiseAddController instance;
    public MerchandiseAddController() {
        instance = this;
    }
    public static MerchandiseAddController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set autocomplete for merchandise type
        List<String> allMerchandiseTypes = MerchandiseRepository.getAllMerchandiseTypes();
        if (allMerchandiseTypes != null) {
            AutoCompletionBinding<String> tHolder = TextFields.bindAutoCompletion(typeHolder, allMerchandiseTypes);
        }
    }

    @FXML
    void close(MouseEvent event) {
        StageHelper.closeStage(event);
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(OrderCategoryController.getInstance().ordersAddUpdateIsShow);
    }

    @FXML
    void save(ActionEvent event) {
        Session session;

        Merchandise merchandise = new Merchandise();
        merchandise.setId(UUIDHelper.generateType4UUID().toString());
        merchandise.setName(nameHolder.getText());
        merchandise.setBranch(branchHolder.getText());
        merchandise.setType(typeHolder.getText());
        merchandise.setImportPrice(importPriceHolder.getText());
        merchandise.setPrice(priceHolder.getText());
        merchandise.setQuantity(!quantityHolder.getText().isEmpty() ? Integer.parseInt(quantityHolder.getText()) : 0);

        List<String> validateInsert = MerchandiseValidation.validateInsert(merchandise);
        if (validateInsert.size() == 0) {
            // Save new merchandise
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(merchandise);
            session.getTransaction().commit();
            session.close();

            // Close stage
            StageHelper.closeStage(event);
            // Show alert box
            AlertBoxHelper.showMessageBox("Thêm thành công");
            // Refresh content table
            MerchandiseCategoryController.getInstance().refresh();
            // Unhide host only when orders add is not show
            MainNavigatorController.instance.getHost().setDisable(OrderCategoryController.getInstance().ordersAddUpdateIsShow);
            // Refresh Orders merchandise list
            if (OrderAddController.getInstance() != null) {
                OrderAddController.getInstance().initialize(null, null);
            }
        } else {
            errorMessage.setText(validateInsert.get(0));
        }
    }

    @FXML
    void requestFocus(MouseEvent event) {
        host.requestFocus();
    }
}

