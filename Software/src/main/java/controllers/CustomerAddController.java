package controllers;

import entities.Customer;
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
import validation.CustomerValidation;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerAddController implements Initializable {
    @FXML
    AnchorPane host;
    @FXML
    private TextField nameHolder;
    @FXML
    private TextField emailHolder;
    @FXML
    private TextField phoneHolder;
    @FXML
    private TextField addressHolder;
    @FXML
    private ComboBox<String> typeHolder;
    @FXML
    private Label errorMessage;

    public static CustomerAddController instance;
    public CustomerAddController() { instance = this; }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeHolder.getItems().addAll("Khách hàng", "Nhà cung cấp");
    }

    @FXML
    void save(ActionEvent event) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        Session session;

        Customer customer = new Customer();
        customer.setId(UUIDHelper.generateType4UUID().toString());
        customer.setFullName(nameHolder.getText());
        customer.setEmail(emailHolder.getText());
        customer.setPhone(phoneHolder.getText());
        customer.setAddress(addressHolder.getText());
        customer.setType(typeHolder.getValue());

        List<String> validateInsert = CustomerValidation.validateInsert(customer);
        if (validateInsert.size() == 0) {
            // Save new customer
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(customer);
            session.getTransaction().commit();
            session.close();

            // Show alert box
            AlertBoxHelper.showMessageBox("Thêm thành công");
            // Close stage
            StageHelper.closeStage(event);
            // Refresh content table
            CustomerCategoryController.getInstance().refresh();
            // Unhide host only when orders add is not show
            MainNavigatorController.instance.getHost().setDisable(OrderCategoryController.getInstance().ordersAddUpdateIsShow);
            // Refresh Orders customer list
            if (OrderAddController.getInstance() != null) {
                OrderAddController.getInstance().initialize(null, null);
            }
        } else {
            errorMessage.setText(validateInsert.get(0));
        }
    }

    @FXML
    void close(MouseEvent event) {
        StageHelper.closeStage(event);
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(OrderCategoryController.getInstance().ordersAddUpdateIsShow);
    }

    @FXML
    void requestFocus(MouseEvent event) {
        host.requestFocus();
    }
}
