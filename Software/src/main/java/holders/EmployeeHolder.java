package holders;

import entities.Employee;

public final class EmployeeHolder {

    private Employee employee;
    private final static EmployeeHolder INSTANCE = new EmployeeHolder();

    private EmployeeHolder() {

    }

    public static EmployeeHolder getInstance() { return INSTANCE; }

    public void setEmployee(Employee employee) { this.employee = employee; }

    public Employee getEmployee() { return this.employee; }
}
