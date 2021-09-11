package controllers;

import com.jfoenix.controls.JFXButton;
import dataModel.ImportsDetailModel;
import dataModel.ReceiptOrdersModel;
import entities.*;
import holders.ImportsHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import repositories.ImportsDetailRepository;
import repositories.ImportsRepository;
import repositories.OrdersDetailRepository;
import repositories.OrdersRepository;
import utils.*;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ImportsUpdateController implements Initializable {
    @FXML
    private AnchorPane host;
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
    private TableView<ImportsDetailModel> detailTable;
    @FXML
    private TableColumn<ImportsDetailModel, String> merchandiseCol;
    @FXML
    private TableColumn<ImportsDetailModel, Integer> quantityCol;
    @FXML
    private TableColumn<ImportsDetailModel, Integer> amountCol;
    @FXML
    private TableColumn<ImportsDetailModel, Integer> finalAmountCol;

    @FXML
    private TextField sumQuantityHolder;
    @FXML
    private TextField sumAmountHolder;
    @FXML
    private Label errorMessage;
    @FXML
    private JFXButton updateImportsButton;

    // Get Imports from ImportsCategoryController select(MouseEvent event)
    Imports imports = ImportsHolder.getInstance().getImports();
    List<ImportsDetail> oldDetails = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Show Customer info
        Customer customer = imports.getOrders().getCustomer();
        customerHolder.setText(customer.getFullName());
        phoneHolder.setText(customer.getPhone());
        addressHolder.setText(customer.getAddress());

        // Show Customer Orders
        List<ReceiptOrdersModel> receiptOrdersModelList = new ArrayList<>();
        ReceiptOrdersModel receiptOrdersModel = new ReceiptOrdersModel();
        receiptOrdersModel.setOrders(imports.getOrders());
        receiptOrdersModel.setCreatedDate(imports.getOrders().getCreatedDate());
        receiptOrdersModel.setDescription(imports.getOrders().getDescription());
        receiptOrdersModel.setEmployeeName(imports.getOrders().getEmployee().getFullName());
        receiptOrdersModelList.add(receiptOrdersModel);
        TableHelper.setReceiptOrdersModelTable(receiptOrdersModelList, ordersTable, dateCol, descriptionCol, employeeCol);

        // Show ImportsDetail
        List<ImportsDetail> importsDetails = ImportsDetailRepository.getByImportsId(imports.getId());
        List<OrdersDetail> ordersDetails = OrdersDetailRepository.getByOrdersId(imports.getOrders().getId());
        List<ImportsDetailModel> importsDetailModels = new ArrayList<>();
        for (ImportsDetail item : importsDetails) {
            OrdersDetail ordersDetail = ordersDetails.stream().filter(t -> t.getMerchandise().getId()
                    .equals(item.getMerchandise().getId()))
                    .findFirst().orElse(null);
            // Remove delivered Merchandise
            this.removeDeliveredMerchandise(ordersDetails);
            ///
            ImportsDetailModel importsDetailModel = new ImportsDetailModel(item);
            importsDetailModel.setOrdersDetail(ordersDetail);
            importsDetailModel.setMerchandiseName(item.getMerchandise().getName());
            importsDetailModel.setQuantity(item.getQuantity());
            importsDetailModel.setAmount(NumberHelper.addComma(item.getMerchandise().getPrice()));
            importsDetailModel.setFinalAmount(NumberHelper.addComma(String.valueOf(item.getAmount())));
            importsDetailModels.add(importsDetailModel);
            // For use in updateImports
            oldDetails.add(new ImportsDetail(item));
        }
        // Set SumQuantityHolder and SumAmountHolder
        Integer sumQuantity = importsDetailModels.stream().mapToInt(ImportsDetailModel::getQuantity).sum();
        Integer sumAmount = importsDetailModels.stream().mapToInt(t -> Integer.parseInt(NumberHelper.removeComma(t.getFinalAmount()))).sum();
        sumQuantityHolder.setText(String.valueOf(sumQuantity));
        sumAmountHolder.setText(NumberHelper.addComma(String.valueOf(sumAmount)));
        // Set DetailTable
        TableHelper.setImportsDetailModelTable(importsDetailModels, detailTable, merchandiseCol, quantityCol, amountCol, finalAmountCol);
    }

    @FXML
    void pickMerchandise(MouseEvent event) {
        if (event.getClickCount() == 2) {
            // Show chosen merchandise name and price
            ImportsDetailModel importsDetailModel = detailTable.getSelectionModel().getSelectedItem();
            ordersTable.getSelectionModel().clearSelection();
            merchandiseHolder.setText(importsDetailModel.getMerchandiseName());
            quantityHolder.setText(importsDetailModel.getQuantity().toString());
        }
    }

    @FXML
    void updateImportsDetail(ActionEvent event) {
        errorMessage.setText("");
        List<ImportsDetailModel> importsDetailModels = new ArrayList<>();
        for (ImportsDetailModel item : detailTable.getItems()) {
            ImportsDetailModel importsDetailModel = new ImportsDetailModel(item);
            importsDetailModels.add(importsDetailModel);
        }

        for (ImportsDetailModel item : importsDetailModels) {
            if (item.getMerchandiseName().equals(merchandiseHolder.getText())) {
                int ordersQuantity = Objects.requireNonNull(OrdersDetailRepository.getById(item.getOrdersDetail().getId())).getQuantity();
                int changeQuantity = Integer.parseInt(quantityHolder.getText());
                int boughtQuantity = ImportsDetailRepository.getBoughtQuantityOfMerchandise(
                        item.getOrdersDetail().getOrders().getId(),
                        item.getImportsDetail().getMerchandise().getId(), item.getImportsDetail().getImports().getId()) != null ?
                        Math.toIntExact(Objects.requireNonNull(
                                ImportsDetailRepository.getBoughtQuantityOfMerchandise(
                                        item.getOrdersDetail().getOrders().getId(),
                                        item.getImportsDetail().getMerchandise().getId(), item.getImportsDetail().getImports().getId())
                        )) : 0;
                if ((boughtQuantity + changeQuantity) <= ordersQuantity) {
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
        Integer sumQuantity = importsDetailModels.stream().mapToInt(ImportsDetailModel::getQuantity).sum();
        Integer sumAmount = importsDetailModels.stream().mapToInt(t -> Integer.parseInt(NumberHelper.removeComma(t.getFinalAmount()))).sum();
        sumQuantityHolder.setText(String.valueOf(sumQuantity));
        sumAmountHolder.setText(NumberHelper.addComma(String.valueOf(sumAmount)));
        TableHelper.setImportsDetailModelTable(importsDetailModels, detailTable, merchandiseCol, quantityCol, amountCol, finalAmountCol);
    }

    @FXML
    void removeMerchandise(ActionEvent event) {
        List<ImportsDetailModel> importsDetailModels = new ArrayList<>();
        for (ImportsDetailModel item : detailTable.getItems()) {
            ImportsDetailModel importsDetailModel = new ImportsDetailModel(item);
            importsDetailModels.add(importsDetailModel);
        }
        importsDetailModels.removeIf(item -> item.getMerchandiseName().equals(merchandiseHolder.getText()));
        // set SumQuantityHolder and SumAmountHolder
        Integer sumQuantity = importsDetailModels.stream().mapToInt(ImportsDetailModel::getQuantity).sum();
        Integer sumAmount = importsDetailModels.stream().mapToInt(t -> Integer.parseInt(NumberHelper.removeComma(t.getFinalAmount()))).sum();
        sumQuantityHolder.setText(String.valueOf(sumQuantity));
        sumAmountHolder.setText(NumberHelper.addComma(String.valueOf(sumAmount)));
        TableHelper.setImportsDetailModelTable(importsDetailModels, detailTable, merchandiseCol, quantityCol, amountCol, finalAmountCol);

        updateImportsButton.setDisable(importsDetailModels.size() <= 0);
    }

    @FXML
    void updateImports(ActionEvent event) {
        Session session;

        // Update import detail
        List<ImportsDetail> changingDetails = new ArrayList<>();
        for (ImportsDetailModel item : detailTable.getItems()) {
            item.getImportsDetail().setQuantity(item.getQuantity());
            item.getImportsDetail().setAmount(Long.parseLong(NumberHelper.removeComma(item.getFinalAmount())));
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(item.getImportsDetail());
            session.getTransaction().commit();
            session.close();
            changingDetails.add(item.getImportsDetail());
        }

        // Update merchandise quantity
        for (ImportsDetail item : oldDetails) {
            ImportsDetail matchItem = changingDetails.stream().filter(t -> t.getId().equals(item.getId())).findAny().orElse(null);
            if (matchItem != null) {
                item.getMerchandise().setQuantity(item.getMerchandise().getQuantity() + matchItem.getQuantity() - item.getQuantity());
            } else {
                item.getMerchandise().setQuantity(item.getMerchandise().getQuantity() - item.getQuantity());
            }
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(item.getMerchandise());
            session.getTransaction().commit();
            session.close();
            // Update merchandise category
            MerchandiseCategoryController.getInstance().refresh();
        }

        // Clear holder
        ImportsHolder.getInstance().setImports(null);
        // Update orders status
        this.updateOrdersStatus();
        // Show alert box
        AlertBoxHelper.showMessageBox("Cập nhật thành công");
        // Refresh content table
        ImportsCategoryController.getInstance().refresh();
        // Unhide host only when orders add is not show
        MainNavigatorController.instance.getHost().setDisable(false);
        // Close stage
        StageHelper.closeStage(event);
    }

    @FXML
    void deleteImports(ActionEvent event) {
        Session session;

        // Delete ImportsDetail
        List<ImportsDetail> importsDetails = ImportsDetailRepository.getByImportsId(imports.getId());
        for (ImportsDetail item : importsDetails) {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(item);
            session.getTransaction().commit();
            session.close();

            // Update Merchandise Quantity
            item.getMerchandise().setQuantity(Math.max((item.getMerchandise().getQuantity() - item.getQuantity()), 0));
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(item.getMerchandise());
            session.getTransaction().commit();
            session.close();
        }

        // Delete Impors
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(imports);
        session.getTransaction().commit();
        session.close();

        // Update Orders status
        Orders orders = OrdersRepository.getById(imports.getOrders().getId());
        orders.setStatus("Chưa hoàn tất");
        session = HibernateUtils.getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate(orders);
        session.getTransaction().commit();
        session.close();
        OrderCategoryController.getInstance().refresh();

        // Clear Holder
        ImportsHolder.getInstance().setImports(null);
        // Show alert box
        AlertBoxHelper.showMessageBox("Xoá thành công");
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
        List<OrdersDetail> ordersDetails = OrdersDetailRepository.getByOrdersId(imports.getOrders().getId());
        List<Imports> importsList = ImportsRepository.getByOrdersId(imports.getOrders().getId());

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
        if (!imports.getOrders().getStatus().equals(status)) {
            imports.getOrders().setStatus(status);
            Session session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(imports.getOrders());
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
