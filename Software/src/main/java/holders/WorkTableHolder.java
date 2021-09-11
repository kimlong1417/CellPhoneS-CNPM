package holders;

import entities.WorkTable;

public final class WorkTableHolder {

    private WorkTable workTable;
    private final static WorkTableHolder INSTANCE = new WorkTableHolder();

    private WorkTableHolder() {}

    public static WorkTableHolder getInstance() { return INSTANCE; }
    public void setWorkTable(WorkTable w) { this.workTable = w; }
    public WorkTable getWorkTable() { return this.workTable; }
}
