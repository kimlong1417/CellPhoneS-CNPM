package controllers;

import entities.Customer;
import holders.CustomerHolder;
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
import validation.CustomerValidation;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerUpdateController implements Initializable {
    @FXML
    AnchorPane host;
    @FXML
    private TextField emailHolder;
    @FXML
    private TextField nameHolder;
    @FXML
    private TextField phoneHolder;
    @FXML
    private ComboBox<String> typeHolder;
    @FXML
    private TextField addressHolder;
    @FXML
    private Label errorMessage;

    public static CustomerUpdateController instance;
    public CustomerUpdateController() { instance = this; }

    // Get Customer from CustomerCategoryController select(MouseEvent event)
    Customer customer = CustomerHolder.getInstance().getCustomer();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add value to type ComboBox
        typeHolder.getItems().addAll("Khách hàng", "Nhà cung cấp");

        // Set Customer in update window
        nameHolder.setText(customer.getFullName());
        emailHolder.setText(customer.getEmail());
        phoneHolder.setText(customer.getPhone());
        addressHolder.setText(customer.getAddress());
        if (customer.getType() != null) {
            typeHolder.setValue((customer.getType().equals("Khách hàng")) ? "Khách hàng" : "Nhà cung cấp");
        }
    }

    @FXML
    void close(MouseEvent event) {
        // Clear customer holder
        CustomerHolder.getInstance().setCustomer(null);
        StageHelper.closeStage(event);
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(false);
    }

    @FXML
    void update(ActionEvent event) {
        // Create session
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session;

        customer.setFullName(nameHolder.getText());
        customer.setEmail(emailHolder.getText());
        customer.setPhone(phoneHolder.getText());
        customer.setAddress(addressHolder.getText());
        customer.setType(typeHolder.getValue());

        List<String> validateUpdate = CustomerValidation.validateUpdate(customer);
        if (validateUpdate.size() == 0) {
            // Update customer info
            session = factory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(customer);
            session.getTransaction().commit();
            session.close();

            // Show alert box
            AlertBoxHelper.showMessageBox("Cập nhật thành công");
            // Close stage
            StageHelper.closeStage(event);
            // Refresh content table
            CustomerCategoryController.getInstance().refresh();
            // Set customer holder
            CustomerHolder.getInstance().setCustomer(customer);
            // Unhide host
            MainNavigatorController.instance.getHost().setDisable(false);
            // Refresh ReceiptCategory
            if (ReceiptCategoryController.getInstance() != null) {
                ReceiptCategoryController.getInstance().refresh();
            }
            // Refresh ImportsCategory
            if (ImportsCategoryController.getInstance() != null) {
                ImportsCategoryController.getInstance().refresh();
            }
            // Refresh OrdersCategory
            if (OrderCategoryController.getInstance() != null) {
                OrderCategoryController.getInstance().refresh();
            }
        } else {
            errorMessage.setText(validateUpdate.get(0));
        }
    }

    @FXML
    void delete(ActionEvent event) {
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session;

        List<String> validateDelete = CustomerValidation.validateDelete(customer);
        if (validateDelete.size() == 0) {
            StageHelper.closeStage(event);
            // Show alert box
            AlertBoxHelper.showMessageBox("Xoá thành công");
            // Delete customer
            session = factory.openSession();
            session.beginTransaction();
            session.delete(customer);
            session.getTransaction().commit();
            // Refresh content table
            CustomerCategoryController.getInstance().refresh();
            // Clear customer holder
            CustomerHolder.getInstance().setCustomer(null);
            // Unhide host
            AnchorPane host = MainNavigatorController.instance.getHost();
            host.setDisable(false);
        } else {
            errorMessage.setText(validateDelete.get(0));
        }
    }

    @FXML
    void requestFocus(MouseEvent event) {
        host.requestFocus();
    }
}
