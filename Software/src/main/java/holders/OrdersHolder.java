package holders;

import entities.Orders;

public final class OrdersHolder {

    private Orders orders;
    private final static OrdersHolder INSTANCE = new OrdersHolder();

    private OrdersHolder() {}

    public static OrdersHolder getInstance() { return INSTANCE; }

    public void setOrders(Orders orders) { this.orders = orders; }

    public Orders getOrders() { return this.orders; }
}
