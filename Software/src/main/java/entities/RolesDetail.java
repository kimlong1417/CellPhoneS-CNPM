package entities;

import javax.persistence.*;

@Entity
@Table(name = "roles_detail")
public class RolesDetail {
    @Id
    @Column(name = "id")
    private String id;
    @ManyToOne
    @JoinColumn(name = "roles_id")
    private Roles roles;
    @ManyToOne
    @JoinColumn(name = "permission_code")
    private Permissions permissions;

    public RolesDetail() {
    }

    public RolesDetail(String id, Roles roles, Permissions permissions) {
        this.id = id;
        this.roles = roles;
        this.permissions = permissions;
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

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }
}
