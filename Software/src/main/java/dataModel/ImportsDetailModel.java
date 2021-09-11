package dataModel;

import entities.ImportsDetail;
import entities.OrdersDetail;

public class ImportsDetailModel {
    private OrdersDetail ordersDetail;
    private ImportsDetail importsDetail;
    private String merchandiseName;
    private Integer quantity;
    private String amount;
    private String finalAmount;

    public ImportsDetailModel() {
    }

    public ImportsDetailModel(OrdersDetail ordersDetail, ImportsDetail importsDetail, String merchandiseName, Integer quantity, String amount, String finalAmount) {
        this.ordersDetail = ordersDetail;
        this.importsDetail = importsDetail;
        this.merchandiseName = merchandiseName;
        this.quantity = quantity;
        this.amount = amount;
        this.finalAmount = finalAmount;
    }

    public ImportsDetailModel(ImportsDetailModel i) {
        this.ordersDetail = i.ordersDetail;
        this.importsDetail = i.importsDetail;
        this.merchandiseName = i.merchandiseName;
        this.quantity = i.quantity;
        this.amount = i.amount;
        this.finalAmount = i.finalAmount;
    }

    public ImportsDetailModel(ImportsDetail importsDetail) {
        this.importsDetail = importsDetail;
    }

    @Override
    public String toString() {
        return "ImportsDetailModel{" +
                "ordersDetail=" + ordersDetail +
                ", importsDetail=" + importsDetail +
                ", merchandiseName='" + merchandiseName + '\'' +
                ", quantity=" + quantity +
                ", amount='" + amount + '\'' +
                ", finalAmount='" + finalAmount + '\'' +
                '}';
    }

    public OrdersDetail getOrdersDetail() {
        return ordersDetail;
    }

    public void setOrdersDetail(OrdersDetail ordersDetail) {
        this.ordersDetail = ordersDetail;
    }

    public ImportsDetail getImportsDetail() {
        return importsDetail;
    }

    public void setImportsDetail(ImportsDetail importsDetail) {
        this.importsDetail = importsDetail;
    }

    public String getMerchandiseName() {
        return merchandiseName;
    }

    public void setMerchandiseName(String merchandiseName) {
        this.merchandiseName = merchandiseName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(String finalAmount) {
        this.finalAmount = finalAmount;
    }
}
