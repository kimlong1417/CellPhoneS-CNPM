package entities;

import javax.persistence.*;

@Entity
@Table(name = "employee_roles")
public class EmployeeRoles {
    @Id
    @Column(name = "id")
    private String id;
    @ManyToOne
    @JoinColumn(name = "roles_id")
    private Roles roles;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public EmployeeRoles() {

    }

    public EmployeeRoles(String id, Roles roles, Employee employee) {
        this.id = id;
        this.roles = roles;
        this.employee = employee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
