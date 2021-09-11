package entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "type")
    private String type;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Column(name = "status")
    private String status;
    @Column(name = "description")
    private String description;
    @Temporal(TemporalType.DATE)
    @Column(name = "created_date")
    private Date createdDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "updated_date")
    private Date updatedDate;

    public Orders() {
    }

    public Orders(String id, String type, Employee employee, Customer customer, String status, String description, Date createdDate, Date updatedDate) {
        this.id = id;
        this.type = type;
        this.employee = employee;
        this.customer = customer;
        this.status = status;
        this.description = description;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Orders(Orders orders) {
        this.id = orders.id;
        this.type = orders.type;
        this.employee = orders.employee;
        this.customer = orders.customer;
        this.status = orders.status;
        this.description = orders.description;
        this.createdDate = orders.createdDate;
        this.updatedDate = orders.updatedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @PrePersist
    protected void onCreate() {
        createdDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = new Date();
    }
}
