package entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "type")
    private String type;
    @Column(name = "address")
    private String address;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Temporal(TemporalType.DATE)
    @Column(name = "created_date")
    private Date createdDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "updated_date")
    private Date updatedDate;

    public Customer() {
    }

    public Customer(String id, String fullName, String type, String address, String phone, String email, Date createdDate, Date updatedDate) {
        this.id = id;
        this.fullName = fullName;
        this.type = type;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    protected void onCreate() { createdDate = new Date(); }

    @PreUpdate
    protected void onUpdate() { updatedDate = new Date(); }
}
