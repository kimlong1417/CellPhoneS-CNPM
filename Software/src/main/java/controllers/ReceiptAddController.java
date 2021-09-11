package controllers;

import com.jfoenix.controls.JFXButton;
import dataModel.OrdersDetailModel;
import dataModel.ReceiptOrdersModel;
import entities.Customer;
import entities.Orders;
import entities.OrdersDetail;
import entities.Receipt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import repositories.CustomerRepository;
import repositories.OrdersDetailRepository;
import repositories.OrdersRepository;
import utils.*;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ReceiptAddController implements Initializable {
    @FXML
    private AnchorPane host;
    @FXML
    private TextField customerHolder;
    @FXML
    private TextField phoneHolder;

    // Orders table
    @FXML
    private TableView<ReceiptOrdersModel> ordersTable;
    @FXML
    private TableColumn<ReceiptOrdersModel, Date> dateCol;
    @FXML
    private TableColumn<ReceiptOrdersModel, String> descriptionCol;
    @FXML
    private TableColumn<ReceiptOrdersModel, String> employeeCol;

    @FXML
    private TextField addressHolder;

    // Detail table
    @FXML
    private TableView<OrdersDetailModel> detailTable;
    @FXML
    private TableColumn<OrdersDetailModel, String> merchandiseCol;
    @FXML
    private TableColumn<OrdersDetailModel, Integer> quantityCol;
    @FXML
    private TableColumn<OrdersDetailModel, Integer> amountCol;
    @FXML
    private TableColumn<OrdersDetailModel, Integer> finalAmountCol;

    @FXML
    private JFXButton saveButton;
    @FXML
    private TextField sumQuantityHolder;
    @FXML
    private TextField sumAmountHolder;

    private Orders chosenOrders;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add customer to choose customer textfield
        List<Customer> customerList = CustomerRepository.getAllCustomerActiveOrders();
        if (customerList != null && customerList.size() > 0) {
            // Add item to Customer textfield
            AutoCompletionBinding<String> cHolder = TextFields.bindAutoCompletion(customerHolder, customerList.stream().map(Customer::getFullName).collect(Collectors.toList()));
            cHolder.setOnAutoCompleted(stringAutoCompletionEvent -> showChosenCustomer(null));
        }
    }

    @FXML
    void showChosenCustomer(ActionEvent event) {
        // Clear detail table, sumQuantityHolder, sumAmountHolder
        detailTable.getItems().clear();
        sumAmountHolder.clear();
        sumQuantityHolder.clear();

        // Show customer info
        Customer customer = CustomerRepository.getByName(customerHolder.getText());
        phoneHolder.setText(customer.getPhone());
        addressHolder.setText(customer.getAddress());
        // Show customer orders info
        List<Orders> ordersList = OrdersRepository.getActiveByCustomerName(customerHolder.getText());
        if (ordersList != null && ordersList.size() > 0) {
            List<ReceiptOrdersModel> receiptOrdersModelList = new ArrayList<>();
            for (Orders item : ordersList) {
                ReceiptOrdersModel receiptOrdersModel = new ReceiptOrdersModel();
                receiptOrdersModel.setOrders(item);
                receiptOrdersModel.setCreatedDate(item.getCreatedDate());
                receiptOrdersModel.setDescription(item.getDescription());
                receiptOrdersModel.setEmployeeName(item.getEmployee().getFullName());
                receiptOrdersModelList.add(receiptOrdersModel);
            }
            TableHelper.setReceiptOrdersModelTable(receiptOrdersModelList, ordersTable, dateCol, descriptionCol, employeeCol);
        }
    }

    @FXML
    void select(MouseEvent event) {
        if (event.getClickCount() == 2) {
            // Show chosen orders detail in detailTable
            Orders orders = new Orders(ordersTable.getSelectionModel().getSelectedItem().getOrders());
            ordersTable.getSelectionModel().clearSelection();
            List<OrdersDetail> ordersDetailList = OrdersDetailRepository.getByOrdersId(orders.getId());
            if (ordersDetailList != null && !ordersDetailList.isEmpty()) {
                List<OrdersDetailModel> ordersDetailModelList = new ArrayList<>();
                for (OrdersDetail item : ordersDetailList) {
                    OrdersDetailModel ordersDetailModel = new OrdersDetailModel();
                    ordersDetailModel.setMerchandiseName(item.getMerchandise().getName());
                    ordersDetailModel.setQuantity(item.getQuantity());
                    ordersDetailModel.setAmount(NumberHelper.addComma(item.getMerchandise().getPrice()));
                    ordersDetailModel.setFinalAmount(NumberHelper.addComma(String.valueOf(item.getAmount())));
                    ordersDetailModelList.add(ordersDetailModel);
                }
                // set SumQuantityHolder and SumAmountHolder
                Integer sumQuantity = ordersDetailModelList.stream().mapToInt(OrdersDetailModel::getQuantity).sum();
                Integer sumAmount = ordersDetailModelList.stream().mapToInt(t -> Integer.parseInt(NumberHelper.removeComma(t.getFinalAmount()))).sum();
                sumQuantityHolder.setText(String.valueOf(sumQuantity));
                sumAmountHolder.setText(NumberHelper.addComma(String.valueOf(sumAmount)));
                // Set table
                TableHelper.setOrdersDetailModelTable(ordersDetailModelList, detailTable, merchandiseCol, quantityCol, amountCol, finalAmountCol);
                saveButton.setDisable(false);
                // Set chosen orders
                chosenOrders = orders;
            }
        }
    }

    @FXML
    void save(ActionEvent event) {
        Session session;
        // Save new receipt
        Receipt receipt = new Receipt();
        receipt.setId(UUIDHelper.generateType4UUID().toString());
        receipt.setOrders(chosenOrders);
        receipt.setEmployee(chosenOrders.getEmployee());
        receipt.setDescription(chosenOrders.getDescription());

        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(receipt);
        session.getTransaction().commit();
        session.close();

        // Close stage
        StageHelper.closeStage(event);
        // Show alert box
        AlertBoxHelper.showMessageBox("Thêm thành công");
        // Refresh content table
        ReceiptCategoryController.getInstance().refresh();
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(false);

        // Update orders status
        chosenOrders.setStatus("Hoàn tất");
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate(chosenOrders);
        session.getTransaction().commit();
        session.close();
        OrderCategoryController.getInstance().initialize(null, null);
        // Update Statistic
        if (StatisticRevenueController.getInstance() != null) {
            StatisticRevenueController.getInstance().refresh(null);
        }
        if (StatisticCustomerController.getInstance() != null) {
            StatisticCustomerController.getInstance().refresh(null);
        }
        if (StatisticMerchandiseController.getInstance() != null) {
            StatisticMerchandiseController.instance.refresh(null);
        }
    }

    @FXML
    void close(MouseEvent event) {
        StageHelper.closeStage(event);
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(false);
    }

    @FXML
    void requestFocus(MouseEvent event) {
        host.requestFocus();
    }
}
