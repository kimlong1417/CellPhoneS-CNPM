package controllers;

import com.jfoenix.controls.JFXButton;
import dataModel.OrdersDetailModel;
import dataModel.ReceiptModel;
import dataModel.ReceiptOrdersModel;
import entities.Merchandise;
import entities.Orders;
import entities.OrdersDetail;
import entities.Receipt;
import holders.ReceiptModelHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import repositories.OrdersDetailRepository;
import utils.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ReceiptDetailController implements Initializable {
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
    private TextField sumQuantityHolder;
    @FXML
    private TextField sumAmountHolder;

    // Get ReceiptModel from ReceiptCategoryController select(MouseEvent event)
    Receipt receipt = ReceiptModelHolder.getInstance().getReceiptModel().getReceipt();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (receipt != null) {
            // Set customer
            customerHolder.setText(receipt.getOrders().getCustomer().getFullName());
            phoneHolder.setText(receipt.getOrders().getCustomer().getPhone());
            addressHolder.setText(receipt.getOrders().getCustomer().getAddress());
            // Set orders
            List<ReceiptOrdersModel> receiptOrdersModelList = new ArrayList<>();
            ReceiptOrdersModel receiptOrdersModel = new ReceiptOrdersModel();
            receiptOrdersModel.setOrders(receipt.getOrders());
            receiptOrdersModel.setCreatedDate(receipt.getOrders().getCreatedDate());
            receiptOrdersModel.setDescription(receipt.getOrders().getDescription());
            receiptOrdersModel.setEmployeeName(receipt.getEmployee().getFullName());
            receiptOrdersModelList.add(receiptOrdersModel);
            TableHelper.setReceiptOrdersModelTable(receiptOrdersModelList, ordersTable, dateCol, descriptionCol, employeeCol);
            // Set orders detail
            List<OrdersDetail> ordersDetailList = OrdersDetailRepository.getByOrdersId(receipt.getOrders().getId());
            List<OrdersDetailModel> ordersDetailModelList = new ArrayList<>();
            for (OrdersDetail item : ordersDetailList) {
                OrdersDetailModel ordersDetailModel = new OrdersDetailModel();
                ordersDetailModel.setMerchandiseName(item.getMerchandise().getName());
                ordersDetailModel.setQuantity(item.getQuantity());
                ordersDetailModel.setAmount(NumberHelper.addComma(item.getMerchandise().getPrice()));
                ordersDetailModel.setFinalAmount(NumberHelper.addComma(String.valueOf(item.getAmount())));
                ordersDetailModelList.add(ordersDetailModel);
            }
            TableHelper.setOrdersDetailModelTable(ordersDetailModelList, detailTable, merchandiseCol, quantityCol, amountCol, finalAmountCol);
            // Set sumQuantity and sumAmount of orders detail
            Integer sumQuantity = ordersDetailList.stream().mapToInt(OrdersDetail::getQuantity).sum();
            Long sumAmount = ordersDetailList.stream().mapToLong(OrdersDetail::getAmount).sum();
            sumQuantityHolder.setText(NumberHelper.addComma(String.valueOf(sumQuantity)));
            sumAmountHolder.setText(NumberHelper.addComma(String.valueOf(sumAmount)));
        }
    }

    @FXML
    void delete(ActionEvent event) {
        // Delete receipt
        Session session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(receipt);
        session.getTransaction().commit();
        session.close();
        // Update orders status
        Orders orders = receipt.getOrders();
        orders.setStatus("Chưa hoàn tất");
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate(orders);
        session.getTransaction().commit();
        session.close();
        // Refresh content table
        ReceiptCategoryController.getInstance().refresh();
        OrderCategoryController.getInstance().refresh();
        // Set receipt model holder
        ReceiptModelHolder.getInstance().setReceiptModel(null);
        // Close stage
        StageHelper.closeStage(event);
        // Show alert box
        AlertBoxHelper.showMessageBox("Xoá thành công");
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(false);
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
        // Set receipt model holder
        ReceiptModelHolder.getInstance().setReceiptModel(null);
        // Unhide host
        MainNavigatorController.instance.getHost().setDisable(false);
    }
}
