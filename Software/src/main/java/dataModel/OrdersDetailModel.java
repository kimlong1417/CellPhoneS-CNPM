package dataModel;

import entities.OrdersDetail;

public class OrdersDetailModel {
    private OrdersDetail ordersDetail;
    private String merchandiseName;
    private Integer quantity;
    private String amount;
    private String finalAmount;

    public OrdersDetailModel() {
    }

    public OrdersDetailModel(OrdersDetail ordersDetail, String merchandiseName, Integer quantity, String amount, String finalAmount) {
        this.ordersDetail = ordersDetail;
        this.merchandiseName = merchandiseName;
        this.quantity = quantity;
        this.amount = amount;
        this.finalAmount = finalAmount;
    }

    public OrdersDetailModel(OrdersDetailModel ordersDetailModel) {
        this.merchandiseName = ordersDetailModel.getMerchandiseName();
        this.quantity = ordersDetailModel.getQuantity();
        this.amount = ordersDetailModel.getAmount();
        this.finalAmount = ordersDetailModel.getFinalAmount();
        this.ordersDetail = ordersDetailModel.getOrdersDetail();
    }

    public OrdersDetail getOrdersDetail() {
        return ordersDetail;
    }

    public void setOrdersDetail(OrdersDetail ordersDetail) {
        this.ordersDetail = ordersDetail;
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
