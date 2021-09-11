package entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "permissions")
public class Permissions {
    @Id
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Temporal(TemporalType.DATE)
    @Column(name = "created_date")
    private Date createdDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "updated_date")
    private Date updatedDate;

    public Permissions() {
    }

    public Permissions(String code, String name, Date createdDate, Date updatedDate) {
        this.code = code;
        this.name = name;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
