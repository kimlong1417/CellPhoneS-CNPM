package controllers;

import dataModel.OrdersAddTableModel;
import entities.Customer;
import entities.Merchandise;
import entities.Orders;
import entities.OrdersDetail;
import holders.OrdersHolder;
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
import org.hibernate.SessionFactory;
import repositories.CustomerRepository;
import repositories.MerchandiseRepository;
import repositories.OrdersDetailRepository;
import utils.*;
import validation.OrdersValidation;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class OrderUpdateController implements Initializable {
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
    private TextField descriptionHolder;
    @FXML
    private TextField merchandiseHolder;
    @FXML
    private TextField quantityHolder;
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

    // Get Orders from OrderCategoryController select(MouseEvent event)
    Orders curOrders = OrdersHolder.getInstance().getOrders();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        OrderCategoryController.getInstance().ordersAddUpdateIsShow = true;

        List<Customer> customerList = CustomerRepository.getAll();
        List<Merchandise> merchandiseList = curOrders.getType().equals("Bán hàng") ?
                MerchandiseRepository.getHasQuantity() :
                MerchandiseRepository.getAll();

        if (customerList != null && !customerList.isEmpty() && merchandiseList != null && !merchandiseList.isEmpty()) {
            // Add item to Customer ComboBox
            AutoCompletionBinding<String> cHolder = TextFields.bindAutoCompletion(customerHolder, customerList.stream().map(Customer::getFullName).collect(Collectors.toList()));
            cHolder.setOnAutoCompleted(stringAutoCompletionEvent -> setCustomer(null));
            // Add item to Merchandise ComboBox
            AutoCompletionBinding<String> iHolder = TextFields.bindAutoCompletion(merchandiseHolder, merchandiseList.stream().map(Merchandise::getName).collect(Collectors.toList()));
            iHolder.setOnAutoCompleted(t -> setMerchandiseInventoryQuantity());
        }

        customerHolder.textProperty().addListener(t -> setCustomer(null));

        // Set customer
        customerHolder.setText(curOrders.getCustomer().getFullName());
        phoneHolder.setText(curOrders.getCustomer().getPhone());
        addressHolder.setText(curOrders.getCustomer().getAddress());
        descriptionHolder.setText(curOrders.getDescription());
        emailHolder.setText(curOrders.getCustomer().getEmail());

        // Set OrdersDetail
        List<OrdersDetail> ordersDetailList = OrdersDetailRepository.getByOrdersId(curOrders.getId());
        for (OrdersDetail item : ordersDetailList) {
            OrdersAddTableModel ordersAddTableModel = new OrdersAddTableModel();
            ordersAddTableModel.setMerchandiseName(item.getMerchandise().getName());
            ordersAddTableModel.setQuantity(item.getQuantity());
            ordersAddTableModel.setAmount(NumberHelper.addComma(item.getMerchandise().getPrice()));
            ordersAddTableModel.setSumAmount(NumberHelper.addComma(String.valueOf(item.getAmount())));
            ordersAddTableModelList.add(ordersAddTableModel);
        }

        TableHelper.setOrdersAddTable(ordersAddTableModelList, merchandiseTable, merchandiseCol, quantityCol, amountCol, sumAmountCol);

        // Update sumQuantity and sumAmount
        int sumQuantity = ordersAddTableModelList.stream().mapToInt(OrdersAddTableModel::getQuantity).sum();
        List<Long> allAmount = ordersAddTableModelList.stream().map(t -> Long.parseLong(NumberHelper.removeComma(t.getSumAmount()))).collect(Collectors.toList());
        Long sumAllAmount = allAmount.stream().mapToLong(Long::longValue).sum();

        sumOrdersMerchandiseQuantity.setText(NumberHelper.addComma(String.valueOf(sumQuantity)));
        sumOrdersMerchandiseAmount.setText(NumberHelper.addComma(String.valueOf(sumAllAmount)));
        errorMessage.setText("");
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
            if (chosenCustomer.getType().equals("Khách hàng")) {
                descriptionHolder.setText("Khách hàng " + chosenCustomer.getFullName() + " mua hàng");
            } else {
                descriptionHolder.setText("Mua hàng từ nhà cung cấp " + chosenCustomer.getFullName());
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
    void update(ActionEvent event) {
        Session session;
        if (OrdersValidation.validateUpdate(curOrders).size() == 0) {
            Customer ordersCustomer = CustomerRepository.getByName(customerHolder.getText());
            // Update orders
            curOrders.setType(ordersCustomer.getType().equals("Khách hàng") ? "Bán hàng" : "Nhập hàng");
            curOrders.setCustomer(ordersCustomer);
            curOrders.setStatus("Chưa hoàn tất");
            curOrders.setDescription(descriptionHolder.getText());

            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(curOrders);
            session.getTransaction().commit();
            session.close();
            // Update OrdersDetail
            List<OrdersDetail> oldOrdersDetails = OrdersDetailRepository.getByOrdersId(curOrders.getId());
            OrdersDetailRepository.deleteByOrdersId(curOrders.getId());
            List<OrdersDetail> newOrdersDetails = new ArrayList<>();
            for (OrdersAddTableModel item : ordersAddTableModelList) {
                OrdersDetail ordersDetail = new OrdersDetail();
                ordersDetail.setId(UUIDHelper.generateType4UUID().toString());
                ordersDetail.setOrders(curOrders);
                ordersDetail.setMerchandise(MerchandiseRepository.getByName(item.getMerchandiseName()));
                ordersDetail.getMerchandise().setQuantity(item.getQuantity());
                ordersDetail.setQuantity(item.getQuantity());
                ordersDetail.setAmount(Long.parseLong(NumberHelper.removeComma(item.getSumAmount())));
                newOrdersDetails.add(ordersDetail);

                session = HibernateUtils.getSessionFactory().openSession();
                session.beginTransaction();
                session.save(ordersDetail);
                session.getTransaction().commit();
                session.close();
            }
            // Update merchandise quantity
            if (curOrders.getType().equals("Bán hàng")) {
                updateMerchandiseQuantity(newOrdersDetails, oldOrdersDetails);
            }
            // Show alert box
            AlertBoxHelper.showMessageBox("Cập nhật thành công");
            // Refresh content table
            if (OrderCategoryController.getInstance() != null) {
                OrderCategoryController.getInstance().refresh();
            }
            // Unhide host only when orders add is not show
            MainNavigatorController.instance.getHost().setDisable(false);
            // Clear Orders Holder
            OrdersHolder.getInstance().setOrders(curOrders);
            // Close stage
            StageHelper.closeStage(event);
            // Update merchandise category
            MerchandiseCategoryController.getInstance().refresh();
        } else {
            errorMessage.setText(OrdersValidation.validateUpdate(curOrders).get(0));
        }
    }

    @FXML
    void delete(ActionEvent event) {
        Session session;

        List<String> validateDelete = OrdersValidation.validateDelete(curOrders);
        if (validateDelete.size() == 0) {
            // Update merchandise quantity
            List<OrdersDetail> oldOrdersDetails = OrdersDetailRepository.getByOrdersId(curOrders.getId());
            updateMerchandiseQuantity(new ArrayList<>(), oldOrdersDetails);
            // Delete orders detail and orders
            OrdersDetailRepository.deleteByOrdersId(curOrders.getId());
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(curOrders);
            session.getTransaction().commit();
            session.close();

            // Show alert box
            AlertBoxHelper.showMessageBox("Xoá thành công");
            // Close stage
            StageHelper.closeStage(event);
            // Refresh content table
            OrderCategoryController.getInstance().refresh();
            // Clear Orders Holder
            OrdersHolder.getInstance().setOrders(null);
            // Unhide host
            MainNavigatorController.instance.getHost().setDisable(false);
            // Update merchandise category
            MerchandiseCategoryController.getInstance().refresh();
        } else {
            errorMessage.setText(validateDelete.get(0));
        }
    }

    @FXML
    void close(MouseEvent event) {
        StageHelper.closeStage(event);
        OrdersHolder.getInstance().setOrders(null);
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
            errors.add("Chưa nhập hàng hoá");
        }
        if (quantityHolder.getText().isEmpty()) {
            errors.add("Chưa nhập số lượng");
        } else {
            if (!NumberHelper.isNumber(quantityHolder.getText())) {
                errors.add("Số lượng phải là chữ số");
            } else if (quantityHolder.getText().equals("0")) {
                errors.add("Số lượng phải khác 0");
            } else {
                if (Integer.parseInt(quantityHolder.getText()) > 1000) {
                    errors.add("Không được nhập số lượng lớn hơn 1000");
                }
                if (customer == null) {
                    errors.add("Chưa chọn khách hàng");
                } else if (customer.getType().equals("Khách hàng")) {
                    if (Integer.parseInt(quantityHolder.getText()) > merchandise.getQuantity()) {
                        errors.add("Không đủ số lượng hàng hoá trong kho");
                    }
                }
            }
        }
        return errors;
    }

    void updateMerchandiseQuantity(List<OrdersDetail> newOrdersDetails, List<OrdersDetail> oldOrdersDetail) {
        Session session;
        // for insert or update order detail
        for (OrdersDetail item : newOrdersDetails) {
            int oldQuantity = 0;
            if (oldOrdersDetail != null && oldOrdersDetail.size() > 0) {
                int index = oldOrdersDetail.stream().map(t -> t.getMerchandise().getId()).collect(Collectors.toList()).indexOf(item.getMerchandise().getId());
                if (index != -1) {
                    oldQuantity = oldOrdersDetail.get(index).getQuantity();
                }

                Merchandise merchandise = MerchandiseRepository.getById(item.getMerchandise().getId());
                merchandise.setQuantity(merchandise.getQuantity() + (oldQuantity - item.getMerchandise().getQuantity()));
                session = HibernateUtils.getSessionFactory().openSession();
                session.beginTransaction();
                session.saveOrUpdate(merchandise);
                session.getTransaction().commit();
                session.close();
            }
        }

        // for delete order detail
        if (oldOrdersDetail != null) {
            for (OrdersDetail item : oldOrdersDetail) {
                int index = newOrdersDetails.stream().map(t -> t.getId()).collect(Collectors.toList()).indexOf(item.getId());
                if (index == -1 && item.getMerchandise() != null) {
                    Merchandise merchandise = MerchandiseRepository.getById(item.getMerchandise().getId());
                    merchandise.setQuantity(merchandise.getQuantity() + item.getQuantity());
                    session = HibernateUtils.getSessionFactory().openSession();
                    session.beginTransaction();
                    session.saveOrUpdate(merchandise);
                    session.getTransaction().commit();
                    session.close();
                }
            }
        }
    }
}