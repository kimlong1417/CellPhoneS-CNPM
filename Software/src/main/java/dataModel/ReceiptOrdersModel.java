package dataModel;

import entities.Orders;

import java.util.Date;

public class ReceiptOrdersModel {

    private Orders orders;
    private Date createdDate;
    private String description;
    private String employeeName;

    public ReceiptOrdersModel() {
    }

    public ReceiptOrdersModel(Orders orders, String description, Date createdDate, String employeeName) {
        this.orders = orders;
        this.description = description;
        this.createdDate = createdDate;
        this.employeeName = employeeName;
    }

    @Override
    public String toString() {
        return "ReceiptOrdersModel{" +
                "orders=" + orders +
                ", description='" + description + '\'' +
                ", createdDate=" + createdDate +
                ", employeeName='" + employeeName + '\'' +
                '}';
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
