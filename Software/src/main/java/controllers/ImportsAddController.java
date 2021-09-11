package controllers;

import com.jfoenix.controls.JFXButton;
import dataModel.OrdersDetailModel;
import dataModel.ReceiptOrdersModel;
import entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.Session;
import repositories.*;
import utils.*;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ImportsAddController implements Initializable {
    @FXML
    AnchorPane host;
    @FXML
    private TextField phoneHolder;
    @FXML
    private TextField customerHolder;

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
    @FXML
    private TextField merchandiseHolder;
    @FXML
    private TextField quantityHolder;

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
    @FXML
    private Label errorMessage;

    private Orders choosenOrders;

    // For other class to call function from this class
    public static ImportsAddController instance;

    public ImportsAddController() {
        instance = this;
    }

    public static ImportsAddController getInstance() {
        return instance;
    }
    ///

    // Get logged in employee
    Employee loggedInEmployee = LoginController.getInstance().curEmployee;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add data in textfield
        List<Customer> supplierList = CustomerRepository.getAllSupplierActiveOrders();
        if (supplierList != null && supplierList.size() > 0) {
            // Add item to customer textfield
            AutoCompletionBinding<String> cHolder = TextFields.bindAutoCompletion(customerHolder, supplierList.stream().map(Customer::getFullName).collect(Collectors.toList()));
            cHolder.setOnAutoCompleted(t -> showChosenCustomer(null));
        }
    }

    @FXML
    void showChosenCustomer(MouseEvent event) {
        // Show customer info
        Customer supplier = CustomerRepository.getByName(customerHolder.getText());
        phoneHolder.setText(supplier.getPhone());
        addressHolder.setText(supplier.getAddress());
        // Show orders
        detailTable.getItems().clear();
        sumAmountHolder.clear();
        sumQuantityHolder.clear();
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
    void showChosenOrders(MouseEvent event) {
        if (event.getClickCount() == 2) {
            // Show chosen orders detail in detailTable
            Orders orders = new Orders(ordersTable.getSelectionModel().getSelectedItem().getOrders());
            ordersTable.getSelectionModel().clearSelection();

            List<OrdersDetail> ordersDetails = OrdersDetailRepository.getByOrdersId(orders.getId());
            if (ordersDetails != null && !ordersDetails.isEmpty()) {
                // Remove delivered detail
                removeDeliveredMerchandise(ordersDetails);
                ///
                List<OrdersDetailModel> ordersDetailModelList = new ArrayList<>();
                for (OrdersDetail item : ordersDetails) {
                    OrdersDetailModel ordersDetailModel = new OrdersDetailModel();
                    ordersDetailModel.setOrdersDetail(item);
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
            }
            choosenOrders = new Orders(orders);
        }
    }

    @FXML
    void pickMerchandise(MouseEvent event) {
        if (event.getClickCount() == 2) {
            // Show chosen merchandise name and price
            OrdersDetailModel ordersDetailModel = detailTable.getSelectionModel().getSelectedItem();
            ordersTable.getSelectionModel().clearSelection();
            merchandiseHolder.setText(ordersDetailModel.getMerchandiseName());
            quantityHolder.setText(ordersDetailModel.getQuantity().toString());
        }
    }

    @FXML
    void updateOrdersDetail(ActionEvent event) {
        errorMessage.setText("");
        List<OrdersDetailModel> ordersDetailModelList = new ArrayList<>();
        for (OrdersDetailModel item : detailTable.getItems()) {
            OrdersDetailModel ordersDetailModel = new OrdersDetailModel(item);
            ordersDetailModelList.add(ordersDetailModel);
        }
        for (OrdersDetailModel item : ordersDetailModelList) {
            if (item.getMerchandiseName().equals(merchandiseHolder.getText())) {
                if (Integer.parseInt(quantityHolder.getText()) <= item.getOrdersDetail().getQuantity()) {
                    if (!quantityHolder.getText().equals("0")) {
                        item.setQuantity(Integer.parseInt(quantityHolder.getText()));
                        Long amount = Long.parseLong(NumberHelper.removeComma(item.getAmount()));
                        item.setFinalAmount(NumberHelper.addComma(String.valueOf(item.getQuantity() * amount)));
                    } else {
                        errorMessage.setText("Không được chọn số lượng hàng hoá là 0");
                    }
                    break;
                } else {
                    errorMessage.setText("Không được chọn số lượng hàng hoá lớn hơn số lượng của đơn hàng");
                }
            }
        }

        // set SumQuantityHolder and SumAmountHolder
        Integer sumQuantity = ordersDetailModelList.stream().mapToInt(OrdersDetailModel::getQuantity).sum();
        Integer sumAmount = ordersDetailModelList.stream().mapToInt(t -> Integer.parseInt(NumberHelper.removeComma(t.getFinalAmount()))).sum();
        sumQuantityHolder.setText(String.valueOf(sumQuantity));
        sumAmountHolder.setText(NumberHelper.addComma(String.valueOf(sumAmount)));
        TableHelper.setOrdersDetailModelTable(ordersDetailModelList, detailTable, merchandiseCol, quantityCol, amountCol, finalAmountCol);
    }

    @FXML
    void removeMerchandise(ActionEvent event) {
        List<OrdersDetailModel> ordersDetailModelList = new ArrayList<>();
        for (OrdersDetailModel item : detailTable.getItems()) {
            OrdersDetailModel ordersDetailModel = new OrdersDetailModel(item);
            ordersDetailModelList.add(ordersDetailModel);
        }
        ordersDetailModelList.removeIf(item -> item.getMerchandiseName().equals(merchandiseHolder.getText()));
        // set SumQuantityHolder and SumAmountHolder
        Integer sumQuantity = ordersDetailModelList.stream().mapToInt(OrdersDetailModel::getQuantity).sum();
        Integer sumAmount = ordersDetailModelList.stream().mapToInt(t -> Integer.parseInt(NumberHelper.removeComma(t.getFinalAmount()))).sum();
        sumQuantityHolder.setText(String.valueOf(sumQuantity));
        sumAmountHolder.setText(NumberHelper.addComma(String.valueOf(sumAmount)));
        TableHelper.setOrdersDetailModelTable(ordersDetailModelList, detailTable, merchandiseCol, quantityCol, amountCol, finalAmountCol);

        saveButton.setDisable(ordersDetailModelList.size() <= 0);
    }

    @FXML
    void save(ActionEvent event) {
        // Save imports
        Imports imports = new Imports();
        imports.setId(UUIDHelper.generateType4UUID().toString());
        imports.setOrders(choosenOrders);
        imports.setEmployee(loggedInEmployee);
        imports.setDescription(choosenOrders.getDescription());

        Session session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(imports);
        session.getTransaction().commit();
        session.close();
        // Save imports detail
        List<OrdersDetailModel> ordersDetailModels = detailTable.getItems();
        for (OrdersDetailModel item : ordersDetailModels) {
            ImportsDetail importsDetail = new ImportsDetail();
            importsDetail.setId(UUIDHelper.generateType4UUID().toString());
            importsDetail.setImports(imports);
            importsDetail.setMerchandise(item.getOrdersDetail().getMerchandise());
            importsDetail.setQuantity(item.getQuantity());
            importsDetail.setAmount(Long.parseLong(NumberHelper.removeComma(item.getFinalAmount())));

            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(importsDetail);
            session.getTransaction().commit();
            session.close();

            // Update merchandise quantity
            importsDetail.getMerchandise().setQuantity(item.getQuantity() + importsDetail.getMerchandise().getQuantity());
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(importsDetail.getMerchandise());
            session.getTransaction().commit();
            session.close();
            // Update merchandise category
            MerchandiseCategoryController.getInstance().refresh();
        }
        // Update orders status
        updateOrdersStatus();
        // Show alert box
        AlertBoxHelper.showMessageBox("Thêm thành công");
        // Refresh content table
        ImportsCategoryController.getInstance().refresh();
        // Unhide host only when orders add is not show
        MainNavigatorController.instance.getHost().setDisable(false);
        // Close stage
        StageHelper.closeStage(event);
        // Update merchandise category
        MerchandiseCategoryController.getInstance().refresh();
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

    void updateOrdersStatus() {
        Session session;
        List<OrdersDetail> ordersDetails = OrdersDetailRepository.getByOrdersId(choosenOrders.getId());
        List<Imports> importsList = ImportsRepository.getByOrdersId(choosenOrders.getId());

        int totalQuantityOrder = 0;
        for (OrdersDetail item : ordersDetails) {
            totalQuantityOrder += item.getQuantity();
        }

        int totalQuantityImport = 0;
        for (Imports item : importsList) {
            List<ImportsDetail> importsDetails = ImportsDetailRepository.getByImportsId(item.getId());
            for (ImportsDetail detail : importsDetails) {
                totalQuantityImport += detail.getQuantity();
            }
        }

        String status = totalQuantityImport < totalQuantityOrder ? "Chưa hoàn tất" : "Hoàn tất";
        if (!choosenOrders.getStatus().equals(status)) {
            choosenOrders.setStatus(status);
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(choosenOrders);
            session.getTransaction().commit();
            session.close();
            // Update OrdersCategory
            OrderCategoryController.getInstance().refresh();
        }
    }

    void removeDeliveredMerchandise(List<OrdersDetail> ordersDetails) {
        // tìm import bằng orders id
        List<Imports> imports = ImportsRepository.getByOrdersId(ordersDetails.get(0).getOrders().getId());

        // tìm sum amount import detail bằng import
        if (imports != null && imports.size() > 0) {
            List<ImportsDetail> importsDetails = ImportsDetailRepository.getDistinctByImportsIdIn(imports.stream().map(Imports::getId).collect(Collectors.toList()));

            // quet 2 cai nếu có merchandise id bằng nhau và amount bằng nhau thì bỏ đi
            List<OrdersDetail> deliveredItem = new ArrayList<>();
            for (OrdersDetail item : ordersDetails) {
                for (ImportsDetail imDetail : importsDetails) {
                    if (item.getMerchandise().getId().equals(imDetail.getMerchandise().getId())) {
                        item.setQuantity(item.getQuantity() - imDetail.getQuantity());
                        item.setAmount(item.getQuantity() * Long.parseLong(NumberHelper.removeComma(item.getMerchandise().getPrice())));
                        if (item.getQuantity() == 0) {
                            deliveredItem.add(item);
                        }
                    }
                }
            }
            ordersDetails.removeAll(deliveredItem);
        }
    }
}
