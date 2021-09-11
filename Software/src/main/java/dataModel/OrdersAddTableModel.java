package dataModel;

public class OrdersAddTableModel {
    private String merchandiseName;
    private Integer quantity;
    private String amount;
    private String sumAmount;

    public OrdersAddTableModel() {
    }

    public OrdersAddTableModel(String merchandiseName, Integer quantity, String amount, String sumAmount) {
        this.merchandiseName = merchandiseName;
        this.quantity = quantity;
        this.amount = amount;
        this.sumAmount = sumAmount;
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

    public String getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
    }
}
