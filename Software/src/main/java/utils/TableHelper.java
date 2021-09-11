package utils;

import dataModel.*;
import controllers.OrderAddController;
import entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;

public class TableHelper {

    public static void setCustomerTable(List<Customer> customerList,
                                        TableView<Customer> table,
                                        TableColumn<Customer, String> nameCol,
                                        TableColumn<Customer, String> phoneCol,
                                        TableColumn<Customer, String> emailCol,
                                        TableColumn<Customer, String> addressCol,
                                        TableColumn<Customer, String> typeCol) {
        table.getItems().clear();
        ObservableList<Customer> data = FXCollections.observableList(customerList);

        // Associate data with columns
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Add item table
        table.getItems().clear();
        table.setItems(data);
    }

    public static void setEmployeeTable(List<Employee> employeeList,
                                        TableView<Employee> table,
                                        TableColumn<Employee, String> nameCol,
                                        TableColumn<Employee, String> phoneCol,
                                        TableColumn<Employee, String> emailCol,
                                        TableColumn<Employee, Date> dateOfBirthCol) {
        table.getItems().clear();
        ObservableList<Employee> data = FXCollections.observableList(employeeList);

        // Associate data with columns
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        dateOfBirthCol.setCellValueFactory(new PropertyValueFactory<>("birthDay"));

        // Add item table
        table.getItems().clear();
        table.setItems(data);
    }

    public static void setPermissionNameTable(List<Permissions> permissionsList,
                                              TableView<Permissions> table,
                                              TableColumn<Permissions, String> nameCol) {
        table.getItems().clear();
        ObservableList<Permissions> data = FXCollections.observableList(permissionsList);

        // Associate data with columns
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Add item table
        table.getItems().clear();
        table.setItems(data);
    }

    public static void setRolesTable(List<Roles> rolesList,
                                     TableView<Roles> table,
                                     TableColumn<Roles, String> nameCol,
                                     TableColumn<Roles, Date> createdDateCol) {
        table.getItems().clear();
        ObservableList<Roles> data = FXCollections.observableList(rolesList);

        // Associate data with columns
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        createdDateCol.setCellValueFactory(new PropertyValueFactory<>("createdDate"));

        // Add item table
        table.getItems().clear();
        table.setItems(data);
    }

    public static void setReceiptTable(List<ReceiptModel> receiptModeltList,
                                        TableView<ReceiptModel> table,
                                        TableColumn<ReceiptModel, Date> dateCol,
                                        TableColumn<ReceiptModel, String> nameCol,
                                       TableColumn<ReceiptModel, String> descriptionCol,
                                        TableColumn<ReceiptModel, Integer> quantityCol,
                                        TableColumn<ReceiptModel, String> amountCol) {
        table.getItems().clear();
        ObservableList<ReceiptModel> data = FXCollections.observableList(receiptModeltList);

        // Associate data with columns
        dateCol.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("sumQuantity"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("sumAmount"));

        // Add item table
        table.getItems().clear();
        table.setItems(data);
    }

    public static void setOrdersModelTable(List<OrdersModel> ordersModelList,
                                           TableView<OrdersModel> table,
                                           TableColumn<OrdersModel, Date> createdDateCol,
                                           TableColumn<OrdersModel, String> customerNameCol,
                                           TableColumn<OrdersModel, String> descriptionCol,
                                           TableColumn<OrdersModel, String> totalAmountCol,
                                           TableColumn<OrdersModel, String> statusCol,
                                           TableColumn<OrdersModel, String> typeCol) {
        table.getItems().clear();
        ObservableList<OrdersModel> data = FXCollections.observableList(ordersModelList);

        // Associate data with columns
        createdDateCol.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        totalAmountCol.setCellValueFactory(new PropertyValueFactory<>("sumAmount"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Add item table
        table.getItems().clear();
        table.setItems(data);
    }

    public static void setMerchandiseTable(List<Merchandise> merchandiseList,
                                           TableView<Merchandise> table,
                                           TableColumn<Merchandise, String> nameCol,
                                           TableColumn<Merchandise, String> typeCol,
                                           TableColumn<Merchandise, Integer> quantityCol,
                                           TableColumn<Merchandise, String> priceCol) {
        table.getItems().clear();
        // Add comma for price and import price
        for (Merchandise item : merchandiseList) {
            item.setImportPrice(item.getImportPrice() != null ? NumberHelper.addComma(item.getImportPrice()) : null);
            item.setPrice(item.getPrice() != null ? NumberHelper.addComma(item.getPrice()) : null);
        }
        ObservableList<Merchandise> data = FXCollections.observableList(merchandiseList);

        // Associate data with columns
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Add item table
        table.getItems().clear();
        table.setItems(data);
    }

    public static void setReceiptOrdersModelTable(List<ReceiptOrdersModel> receiptOrdersModelList,
                                      TableView<ReceiptOrdersModel> table,
                                      TableColumn<ReceiptOrdersModel, Date> dateCol,
                                      TableColumn<ReceiptOrdersModel, String> descriptionCol,
                                      TableColumn<ReceiptOrdersModel, String> employeeCol) {
        ObservableList<ReceiptOrdersModel> data = FXCollections.observableList(receiptOrdersModelList);

        // Associate data with columns
        dateCol.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        employeeCol.setCellValueFactory(new PropertyValueFactory<>("employeeName"));

        //Add item table
        table.setItems(data);
    }
    public static void setOrdersAddTable(List<OrdersAddTableModel> ordersAddTableModelList,
                                         TableView<OrdersAddTableModel> table,
                                         TableColumn<OrdersAddTableModel, String> merchandiseCol,
                                         TableColumn<OrdersAddTableModel, Integer> quantityCol,
                                         TableColumn<OrdersAddTableModel, String> amountCol,
                                         TableColumn<OrdersAddTableModel, String> sumAmountCol) {
        ObservableList<OrdersAddTableModel> data = FXCollections.observableList(ordersAddTableModelList);

        // Associate data with columns
        merchandiseCol.setCellValueFactory(new PropertyValueFactory<>("merchandiseName"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        sumAmountCol.setCellValueFactory(new PropertyValueFactory<>("sumAmount"));

        // Add item table
        table.setItems(data);
    }

    public static void setOrdersDetailModelTable(List<OrdersDetailModel> ordersDetailModelList,
                                                 TableView<OrdersDetailModel> table,
                                                 TableColumn<OrdersDetailModel, String> merchandiseCol,
                                                 TableColumn<OrdersDetailModel, Integer> quantityCol,
                                                 TableColumn<OrdersDetailModel, Integer> amountCol,
                                                 TableColumn<OrdersDetailModel, Integer> finalAmountCol) {
        ObservableList<OrdersDetailModel> data = FXCollections.observableList(ordersDetailModelList);

        // Associate date with columns
        merchandiseCol.setCellValueFactory(new PropertyValueFactory<>("merchandiseName"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        finalAmountCol.setCellValueFactory(new PropertyValueFactory<>("finalAmount"));

        // Add item table
        table.setItems(data);
    }

    public static void setImportsDetailModelTable(List<ImportsDetailModel> ordersDetailModelList,
                                                 TableView<ImportsDetailModel> table,
                                                 TableColumn<ImportsDetailModel, String> merchandiseCol,
                                                 TableColumn<ImportsDetailModel, Integer> quantityCol,
                                                 TableColumn<ImportsDetailModel, Integer> amountCol,
                                                 TableColumn<ImportsDetailModel, Integer> finalAmountCol) {
        ObservableList<ImportsDetailModel> data = FXCollections.observableList(ordersDetailModelList);

        // Associate date with columns
        merchandiseCol.setCellValueFactory(new PropertyValueFactory<>("merchandiseName"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        finalAmountCol.setCellValueFactory(new PropertyValueFactory<>("finalAmount"));

        // Add item table
        table.setItems(data);
    }

    public static void setImportsModelTable(List<ImportsModel> importsModelList,
                                            TableView<ImportsModel> table,
                                            TableColumn<ImportsModel, Date> dateCol,
                                            TableColumn<ImportsModel, String> nameCol,
                                            TableColumn<ImportsModel, String> descriptionCol,
                                            TableColumn<ImportsModel, Integer> quantityCol,
                                            TableColumn<ImportsModel, String> amountCol) {

        ObservableList<ImportsModel> data = FXCollections.observableList(importsModelList);

        // Associate data with columns
        dateCol.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("sumQuantity"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("sumAmount"));

        // Add item to table
        table.setItems(data);
    }

    public static void setWorkShiftTable(List<WorkShift> workShiftList,
                                         TableView<WorkShift> table,
                                         TableColumn<WorkShift, String> dateCol,
                                         TableColumn<WorkShift, String> nameCol,
                                         TableColumn<WorkShift, String> timeInCol,
                                         TableColumn<WorkShift, String> timeOutCol) {
        ObservableList<WorkShift> data = FXCollections.observableList(workShiftList);

        // Associate data with columns
        dateCol.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        timeInCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        timeOutCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        // Add item to table
        table.setItems(data);
    }

    public static void setWorkTableModelTable(List<WorkTableModel> dataList,
                                              TableView<WorkTableModel> table,
                                              TableColumn<WorkTableModel, String> nameCol,
                                              TableColumn<WorkTableModel, String> shiftCol,
                                              TableColumn<WorkTableModel, String> timeInCol,
                                              TableColumn<WorkTableModel, String> timeOutCol,
                                              TableColumn<WorkTableModel, String> workDaysCol) {
        ObservableList<WorkTableModel> data = FXCollections.observableList(dataList);

        // Associate data with columns
        nameCol.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        shiftCol.setCellValueFactory(new PropertyValueFactory<>("shiftName"));
        timeInCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        timeOutCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        workDaysCol.setCellValueFactory(new PropertyValueFactory<>("workDays"));

        // Add item to table
        table.setItems(data);
    }
}
