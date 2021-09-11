package entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "full_name")
    private String fullName;
    @Temporal(TemporalType.DATE)
    @Column(name = "birth_day")
    private Date birthDay;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Temporal(TemporalType.DATE)
    @Column(name = "created_date")
    private Date createdDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "updated_date")
    private Date updatedDate;

    public Employee() {
    }

    public Employee(Employee input) {
        this.id = input.id;
        this.fullName = input.fullName;
        this.birthDay = input.birthDay;
        this.phone = input.phone;
        this.email = input.email;
        this.password = input.password;
        this.createdDate = input.createdDate;
        this.updatedDate = input.updatedDate;
    }

    public Employee(String id, String fullName, Date birthDay, String phone, String email, String password, Date createdDate, Date updatedDate) {
        this.id = id;
        this.fullName = fullName;
        this.birthDay = birthDay;
        this.phone = phone;
        this.email = email;
        this.password = password;
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

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
