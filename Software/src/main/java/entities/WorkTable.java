package entities;

import javax.persistence.*;

@Entity
@Table(name = "work_table")
public class WorkTable {
    @Id
    @Column(name = "id")
    private String id;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "shift_id")
    private WorkShift workShift;
    @Column(name = "day_of_week")
    private String dayOfWeek;

    public WorkTable(String id, Employee employee, WorkShift workShift, String dayOfWeek) {
        this.id = id;
        this.employee = employee;
        this.workShift = workShift;
        this.dayOfWeek = dayOfWeek;
    }

    public WorkTable() {
    }

    @Override
    public String toString() {
        return "WorkTable{" +
                "id='" + id + '\'' +
                ", employee=" + employee +
                ", workShift=" + workShift +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public WorkShift getWorkShift() {
        return workShift;
    }

    public void setWorkShift(WorkShift workShift) {
        this.workShift = workShift;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
