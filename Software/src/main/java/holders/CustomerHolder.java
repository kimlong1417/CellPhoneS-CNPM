package holders;

import entities.Customer;

public final class CustomerHolder {

    private Customer customer;
    private final static CustomerHolder INSTANCE = new CustomerHolder();

    private CustomerHolder() {
    }

    public static CustomerHolder getInstance() {
        return INSTANCE;
    }

    public void setCustomer(Customer u) {
        this.customer = u;
    }

    public Customer getCustomer() {
        return this.customer;
    }
}