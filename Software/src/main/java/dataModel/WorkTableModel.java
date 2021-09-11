package dataModel;

import entities.WorkShift;
import entities.WorkTable;

public class WorkTableModel {
    private WorkShift workShift;
    private WorkTable workTable;
    private String employeeName;
    private String shiftName;
    private String startTime;
    private String endTime;
    private String workDays;

    public WorkTableModel() {
    }

    public WorkTableModel(WorkShift workShift, WorkTable workTable, String employeeName, String shiftName, String startTime, String endTime, String workDays) {
        this.workShift = workShift;
        this.workTable = workTable;
        this.employeeName = employeeName;
        this.shiftName = shiftName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.workDays = workDays;
    }

    public WorkTableModel(WorkTable wTable) {
        this.workShift = wTable.getWorkShift();
        this.workTable = wTable;
        this.employeeName = wTable.getEmployee().getFullName();
        this.shiftName = wTable.getWorkShift().getName();
        this.startTime = wTable.getWorkShift().getStartTime();
        this.endTime = wTable.getWorkShift().getEndTime();
        this.workDays = wTable.getDayOfWeek();
    }

    @Override
    public String toString() {
        return "WorkTableModel{" +
                "workShift=" + workShift +
                ", workTable=" + workTable +
                ", employeeName='" + employeeName + '\'' +
                ", shiftName='" + shiftName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", workDays='" + workDays + '\'' +
                '}';
    }

    public WorkShift getWorkShift() {
        return workShift;
    }

    public void setWorkShift(WorkShift workShift) {
        this.workShift = workShift;
    }

    public WorkTable getWorkTable() {
        return workTable;
    }

    public void setWorkTable(WorkTable workTable) {
        this.workTable = workTable;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWorkDays() {
        return workDays;
    }

    public void setWorkDays(String workDays) {
        this.workDays = workDays;
    }
}
