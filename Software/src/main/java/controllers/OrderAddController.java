package controllers;

import com.jfoenix.controls.JFXButton;
import dataModel.OrdersAddTableModel;
import entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import repositories.CustomerRepository;
import repositories.MerchandiseRepository;
import utils.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class OrderAddController implements Initializable {
    @FXML
    private AnchorPane host;

    @FXML
    private TableView<OrdersAddTableModel> merchandiseTable;
    @FXML
    private TableColumn<OrdersAddTableModel, String> merchandiseCol;
    @FXML
    private TableColumn<OrdersAddTableModel, Integer> quantityCol;
    @FXML
    private TableColumn<OrdersAddTableModel, String> amountCol;
    @FXML
    private TableColumn<OrdersAddTableModel, String> sumAmountCol;

    @FXML
    private TextField customerHolder;
    @FXML
    private TextField phoneHolder;
    @FXML
    private TextField addressHolder;
    @FXML
    private TextField merchandiseHolder;
    @FXML
    private TextField quantityHolder;
    @FXML
    private TextField descriptionHolder;
    @FXML
    private TextField sumOrdersMerchandiseQuantity;
    @FXML
    private TextField sumOrdersMerchandiseAmount;
    @FXML
    private Label errorMessage;
    @FXML
    private TextField inventoryHolder;
    @FXML
    private TextField emailHolder;

    public List<OrdersAddTableModel> ordersAddTableModelList = new ArrayList<>();

    // For other class cal function from this class
    public static OrderAddController instance;

    public OrderAddController() {
        instance = this;
    }

    public static OrderAddController getInstance() {
        return instance;
    }
    ///

    // Get logged in employee
    Employee loggedInEmployee = LoginController.getInstance().curEmployee;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        OrderCategoryController.getInstance().ordersAddUpdateIsShow = true;

        List<Customer> customerList = CustomerRepository.getAll();
        List<Merchandise> merchandiseList = MerchandiseRepository.getAll();

        if (customerList != null && !customerList.isEmpty() && merchandiseList != null && !merchandiseList.isEmpty()) {
            // Add item to Customer Combox
            AutoCompletionBinding<String> cHolder = TextFields.bindAutoCompletion(customerHolder, customerList.stream().map(Customer::getFullName).collect(Collectors.toList()));
            cHolder.setOnAutoCompleted(stringAutoCompletionEvent -> setCustomer(null));
            // Add item to Merchandise Combox
            AutoCompletionBinding<String> iHolder = TextFields.bindAutoCompletion(merchandiseHolder, merchandiseList.stream().map(Merchandise::getName).collect(Collectors.toList()));
            iHolder.setOnAutoCompleted(t -> setMerchandiseInventoryQuantity());
        }
    }

    @FXML
    void setCustomer(ActionEvent event) {
        Customer chosenCustomer = CustomerRepository.getByName(customerHolder.getText());
        // Get chosen customer from customerList
        if (chosenCustomer != null) {
            // Set phoneHolder and addressHolder
            phoneHolder.setText(chosenCustomer.getPhone());
            addressHolder.setText(chosenCustomer.getAddress());
            emailHolder.setText(chosenCustomer.getEmail());
            if (chosenCustomer.getType().equals("Kh??ch h??ng")) {
                descriptionHolder.setText("Kh??ch h??ng " + chosenCustomer.getFullName() + " mua h??ng");
            } else {
                descriptionHolder.setText("Mua h??ng t??? nh?? cung c???p " + chosenCustomer.getFullName());
            }
        }
    }

    void setMerchandiseInventoryQuantity() {
        Merchandise merchandise = MerchandiseRepository.getByName(merchandiseHolder.getText());
        if (merchandise != null) {
            inventoryHolder.setText(merchandise.getQuantity().toString());
        }
    }

    @FXML
    void addNewCustomer(ActionEvent event) {
        CustomerCategoryController.getInstance().insert(null);
    }

    @FXML
    void addNewMerchandise(ActionEvent event) throws IOException {
        MerchandiseCategoryController.getInstance().insert(null);
    }

    @FXML
    void chooseMerchandise(ActionEvent event) {
        Customer customer = CustomerRepository.getByName(customerHolder.getText());
        Merchandise merchandise = MerchandiseRepository.getByName(merchandiseHolder.getText());

        List<String> validateAddMerchandise = this.validateAddMerchandise(customer, merchandise);
        if (validateAddMerchandise.size() == 0) {
            OrdersAddTableModel ordersAddTableModel = new OrdersAddTableModel();
            ordersAddTableModel.setMerchandiseName(merchandise.getName());
            ordersAddTableModel.setQuantity(Integer.parseInt(quantityHolder.getText()));
            ordersAddTableModel.setAmount(NumberHelper.addComma(merchandise.getPrice()));
            Long sumAmount = Long.parseLong(quantityHolder.getText()) * Integer.parseInt(merchandise.getPrice());
            ordersAddTableModel.setSumAmount(NumberHelper.addComma(Long.toString(sumAmount)));
            // Remove duplicate merchandise
            ordersAddTableModelList.removeIf(t -> t.getMerchandiseName().equals(merchandise.getName()));
            ordersAddTableModelList.add(ordersAddTableModel);

            TableHelper.setOrdersAddTable(ordersAddTableModelList, merchandiseTable, merchandiseCol, quantityCol, amountCol, sumAmountCol);
            // Update sumQuantity and sumAmount
            int sumQuantity = ordersAddTableModelList.stream().mapToInt(OrdersAddTableModel::getQuantity).sum();
            List<Long> allAmount = ordersAddTableModelList.stream().map(t -> Long.parseLong(NumberHelper.removeComma(t.getSumAmount()))).collect(Collectors.toList());
            Long sumAllAmount = allAmount.stream().mapToLong(Long::longValue).sum();

            sumOrdersMerchandiseQuantity.setText(NumberHelper.addComma(String.valueOf(sumQuantity)));
            sumOrdersMerchandiseAmount.setText(NumberHelper.addComma(String.valueOf(sumAllAmount)));
            errorMessage.setText("");
            // Clear merchandiseHolder and quantityHolder
            merchandiseHolder.clear();
            quantityHolder.setText("");
        } else {
            errorMessage.setText(validateAddMerchandise.get(0));
        }
    }

    @FXML
    void removeChosenMerchandise(MouseEvent event) {
        if (event.getClickCount() == 2) {
            OrdersAddTableModel choosenMerchandise = merchandiseTable.getSelectionModel().getSelectedItem();
            merchandiseTable.getSelectionModel().clearSelection();
            ordersAddTableModelList.removeIf(t -> t.getMerchandiseName().equals(choosenMerchandise.getMerchandiseName()));

            TableHelper.setOrdersAddTable(ordersAddTableModelList, merchandiseTable, merchandiseCol, quantityCol, amountCol, sumAmountCol);

            // Update sumQuantity and sumAmount
            int sumQuantity = ordersAddTableModelList.stream().mapToInt(OrdersAddTableModel::getQuantity).sum();
            List<Integer> allAmount = ordersAddTableModelList.stream().map(t -> Integer.parseInt(NumberHelper.removeComma(t.getSumAmount()))).collect(Collectors.toList());
            int sumAllAmount = allAmount.stream().mapToInt(Integer::intValue).sum();

            sumOrdersMerchandiseQuantity.setText(NumberHelper.addComma(String.valueOf(sumQuantity)));
            sumOrdersMerchandiseAmount.setText(NumberHelper.addComma(String.valueOf(sumAllAmount)));
        }
    }

    @FXML
    void save(ActionEvent event) {
        Session session;
        Customer ordersCustomer = CustomerRepository.getByName(customerHolder.getText());
        // Save new Orders
        Orders orders = new Orders();
        orders.setId(UUIDHelper.generateType4UUID().toString());
        orders.setType(ordersCustomer.getType().equals("Kh??ch h??ng") ? "B??n h??ng" : "Nh???p h??ng");
        orders.setEmployee(loggedInEmployee);
        orders.setCustomer(ordersCustomer);
        orders.setStatus("Ch??a ho??n t???t");
        if (descriptionHolder.getText().isEmpty()) {
            if (ordersCustomer.getType().equals("Kh??ch h??ng")) {
                descriptionHolder.setText("Kh??ch h??ng " + ordersCustomer.getFullName() + " mua h??ng");
            } else {
                descriptionHolder.setText("Mua h??ng t??? nh?? cung c???p " + ordersCustomer.getFullName());
            }
        } else {
            orders.setDescription(descriptionHolder.getText());
        }
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(orders);
        session.getTransaction().commit();
        session.close();
        // Save new OrdersDetail
        for (OrdersAddTableModel item : ordersAddTableModelList) {
            OrdersDetail ordersDetail = new OrdersDetail();
            ordersDetail.setId(UUIDHelper.generateType4UUID().toString());
            ordersDetail.setOrders(orders);
            ordersDetail.setMerchandise(MerchandiseRepository.getByName(item.getMerchandiseName()));
            ordersDetail.setQuantity(item.getQuantity());
            ordersDetail.setAmount(Long.parseLong(NumberHelper.removeComma(item.getSumAmount())));

            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(ordersDetail);
            session.getTransaction().commit();
            session.close();
            // Update quantity in merchandise entity when sell merchandise
            if (ordersDetail.getOrders().getType().equals("B??n h??ng")) {
                ordersDetail.getMerchandise().setQuantity(ordersDetail.getMerchandise().getQuantity() - item.getQuantity());
                session = HibernateUtils.getSessionFactory().openSession();
                session.beginTransaction();
                session.saveOrUpdate(ordersDetail.getMerchandise());
                session.getTransaction().commit();
                session.close();
                // Update merchandise category
                MerchandiseCategoryController.getInstance().refresh();
            }
        }
        // Show alert box
        AlertBoxHelper.showMessageBox("Th??m th??nh c??ng");
        // Refresh content table
        if (OrderCategoryController.getInstance() != null) {
            OrderCategoryController.getInstance().refresh();
        }
        // Unhide host only when orders add is not show
        MainNavigatorController.instance.getHost().setDisable(false);
        // Close stage
        StageHelper.closeStage(event);
    }

    @FXML
    void close(MouseEvent event) {
        StageHelper.closeStage(event);
        OrderCategoryController.getInstance().ordersAddUpdateIsShow = false;
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(false);
    }

    @FXML
    void requestFocus(MouseEvent event) {
        host.requestFocus();
    }

    List<String> validateAddMerchandise(Customer customer, Merchandise merchandise) {
        List<String> errors = new ArrayList<>();

        if (merchandiseHolder.getText().isEmpty()) {
            errors.add("Ch??a nh???p h??ng ho??");
        }
        if (quantityHolder.getText().isEmpty()) {
            errors.add("Ch??a nh???p s??? l?????ng");
        } else {
            if (!NumberHelper.isNumber(quantityHolder.getText())) {
                errors.add("S??? l?????ng ph???i l?? ch??? s???");
            } else if (quantityHolder.getText().equals("0")) {
                errors.add("S??? l?????ng ph???i kh??c 0");
            } else {
                if (Integer.parseInt(quantityHolder.getText()) > 1000) {
                    errors.add("Kh??ng ???????c nh???p s??? l?????ng l???n h??n 1000");
                }
                if (customer == null) {
                    errors.add("Ch??a ch???n kh??ch h??ng");
                } else if (customer.getType().equals("Kh??ch h??ng")) {
                    if (Integer.parseInt(quantityHolder.getText()) > merchandise.getQuantity()) {
                        errors.add("Kh??ng ????? s??? l?????ng h??ng ho?? trong kho");
                    }
                }
            }
        }
        return errors;
    }
}