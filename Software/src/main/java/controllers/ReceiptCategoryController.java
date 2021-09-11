package controllers;

import dataModel.ReceiptModel;
import entities.Receipt;
import holders.ReceiptModelHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import repositories.OrdersDetailRepository;
import repositories.ReceiptRepository;
import utils.HibernateUtils;
import utils.NumberHelper;
import utils.StageHelper;
import utils.TableHelper;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ReceiptCategoryController implements Initializable {
    @FXML
    private AnchorPane host;
    @FXML
    private TableView<ReceiptModel> contentTable;
    @FXML
    private TableColumn<ReceiptModel, Date> dateCol;
    @FXML
    private TableColumn<ReceiptModel, String> nameCol;
    @FXML
    private TableColumn<ReceiptModel, String> descriptionCol;
    @FXML
    private TableColumn<ReceiptModel, Integer> quantityCol;
    @FXML
    private TableColumn<ReceiptModel, String> amountCol;
    @FXML
    private TextField searchBar;

    // For other class call function from this class
    public static ReceiptCategoryController instance;

    public ReceiptCategoryController() {
        instance = this;
    }

    public static ReceiptCategoryController getInstance() {
        return instance;
    }
    ///

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get all receipt
        List<Receipt> receiptList = ReceiptRepository.getAll();
        //Set receipt model
        if (receiptList != null && !receiptList.isEmpty()) {
            List<ReceiptModel> receiptModelList = new ArrayList<>();
            for (Receipt item : receiptList) {
                Long sumAmount = OrdersDetailRepository.getSumAmountById(item.getOrders().getId());
                int sumQuantity = Math.toIntExact(OrdersDetailRepository.getSumQuantityById(item.getOrders().getId()));

                ReceiptModel receiptModel = new ReceiptModel();
                receiptModel.setReceipt(item);
                receiptModel.setCreatedDate(item.getCreatedDate());
                receiptModel.setCustomerName(item.getOrders().getCustomer().getFullName());
                receiptModel.setDescription(item.getDescription());
                receiptModel.setSumQuantity(sumQuantity);
                receiptModel.setSumAmount(NumberHelper.addComma(String.valueOf(sumAmount)));
                receiptModelList.add(receiptModel);
            }
            TableHelper.setReceiptTable(receiptModelList, contentTable, dateCol, nameCol, descriptionCol, quantityCol, amountCol);
        }
    }

    @FXML
    void addReceipt(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/ReceiptAdd.fxml")));
        StageHelper.startStage(root);
        // Hide host
        MainNavigatorController.instance.getHost().setDisable(true);
    }

    @FXML
    void search(ActionEvent event) {
        // Get receipt like name
        List<Receipt> receiptList = ReceiptRepository.getLikeCustomerName(searchBar.getText());
        //Set receipt model
        if (receiptList != null && !receiptList.isEmpty()) {
            List<ReceiptModel> receiptModelList = new ArrayList<>();
            for (Receipt item : receiptList) {
                Long sumAmount = OrdersDetailRepository.getSumAmountById(item.getOrders().getId());
                Integer sumQuantity = Math.toIntExact(OrdersDetailRepository.getSumQuantityById(item.getOrders().getId()));

                ReceiptModel receiptModel = new ReceiptModel();
                receiptModel.setReceipt(item);
                receiptModel.setCreatedDate(item.getCreatedDate());
                receiptModel.setCustomerName(item.getOrders().getCustomer().getFullName());
                receiptModel.setDescription(item.getDescription());
                receiptModel.setSumQuantity(sumQuantity);
                receiptModel.setSumAmount(NumberHelper.addComma(String.valueOf(sumAmount)));
                receiptModelList.add(receiptModel);
            }
            TableHelper.setReceiptTable(receiptModelList, contentTable, dateCol, nameCol, descriptionCol, quantityCol, amountCol);
        }
    }

    @FXML
    void select(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            ReceiptModel receiptModel = contentTable.getSelectionModel().getSelectedItem();
            contentTable.getSelectionModel().clearSelection();
            // Store receipt to use in another class
            if (receiptModel != null) {
                ReceiptModelHolder.getInstance().setReceiptModel(receiptModel);
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/ReceiptDetail.fxml")));
                StageHelper.startStage(root);
                // Hide host
                MainNavigatorController.instance.getHost().setDisable(true);
            }
        }
    }

    // Refresh table
    public void refresh() {
        initialize(null, null);
    }

    @FXML
    void requestFocus(MouseEvent event) {
        host.requestFocus();
    }
}
