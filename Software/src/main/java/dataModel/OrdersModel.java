package dataModel;

import entities.Orders;

import java.util.Date;

public class OrdersModel {
    private Date createdDate;
    private String customerName;
    private String description;
    private String sumAmount;
    private String status;
    private String type;
    private Orders orders;

    public OrdersModel() {
    }

    public OrdersModel(Date createdDate, String customerName, String description, String sumAmount, String status, String type, Orders orders) {
        this.createdDate = createdDate;
        this.customerName = customerName;
        this.description = description;
        this.sumAmount = sumAmount;
        this.status = status;
        this.type = type;
        this.orders = orders;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
