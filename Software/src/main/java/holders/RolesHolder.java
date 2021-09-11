package holders;

import entities.Roles;

public final class RolesHolder {

    private Roles roles;
    private final static RolesHolder INSTANCE = new RolesHolder();

    private RolesHolder() {
    }

    public static RolesHolder getInstance() { return INSTANCE; }

    public void setRoles(Roles roles) { this.roles = roles; }

    public Roles getRoles() { return this.roles; }
}
