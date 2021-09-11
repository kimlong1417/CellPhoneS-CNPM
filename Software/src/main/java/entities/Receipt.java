package entities;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "receipt")
public class Receipt {
    @Id
    @Column(name = "id")
    private String id;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Orders orders;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @Column(name = "description")
    private String description;
    @Temporal(TemporalType.DATE)
    @Column(name = "created_date")
    private Date createdDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "updated_date")
    private Date updatedDate;

    public Receipt() {
    }

    public Receipt(String id, Orders orders, Employee employee, String description, Date createdDate, Date updatedDate) {
        this.id = id;
        this.orders = orders;
        this.employee = employee;
        this.description = description;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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

    @Override
    public String toString() {
        return "Receipt{" +
                "id='" + id + '\'' +
                ", orders=" + orders +
                ", employee=" + employee +
                ", description='" + description + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
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
