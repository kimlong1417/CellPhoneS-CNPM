package dataModel;

import entities.Receipt;

import java.util.Date;

public class ReceiptModel {
    private Receipt receipt;
    private Date createdDate;
    private String customerName;
    private String description;
    private Integer sumQuantity;
    private String sumAmount;

    public ReceiptModel() {
    }

    public ReceiptModel(Receipt receipt, Date createdDate, String customerName, String description, Integer sumQuantity, String sumAmount) {
        this.receipt = receipt;
        this.createdDate = createdDate;
        this.customerName = customerName;
        this.description = description;
        this.sumQuantity = sumQuantity;
        this.sumAmount = sumAmount;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
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

    public Integer getSumQuantity() {
        return sumQuantity;
    }

    public void setSumQuantity(Integer sumQuantity) {
        this.sumQuantity = sumQuantity;
    }

    public String getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
    }
}
