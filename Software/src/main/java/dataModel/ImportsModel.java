package dataModel;

import entities.Imports;

import java.util.Date;

public class ImportsModel {
    private Imports imports;
    private Date createdDate;
    private String customerName;
    private String description;
    private Integer sumQuantity;
    private String sumAmount;

    public ImportsModel() {
    }

    public ImportsModel(Imports imports, Date createdDate, String customerName, String description, Integer sumQuantity, String sumAmount) {
        this.imports = imports;
        this.createdDate = createdDate;
        this.customerName = customerName;
        this.description = description;
        this.sumQuantity = sumQuantity;
        this.sumAmount = sumAmount;
    }

    public Imports getImports() {
        return imports;
    }

    public void setImports(Imports imports) {
        this.imports = imports;
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
