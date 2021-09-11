package holders;

import entities.WorkShift;

public final class WorkShiftHolder {

    private WorkShift workShift;
    private final static WorkShiftHolder INSTANCE = new WorkShiftHolder();

    private WorkShiftHolder() {}

    public static WorkShiftHolder getInstance() { return INSTANCE; }
    public void setWorkShift(WorkShift w) { this.workShift = w; }
    public WorkShift getWorkShift() { return this.workShift; }
}
