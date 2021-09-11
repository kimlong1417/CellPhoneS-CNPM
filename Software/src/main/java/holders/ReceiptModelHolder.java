package holders;

import dataModel.ReceiptModel;

public final class ReceiptModelHolder {

    private ReceiptModel receiptModel;
    private final static ReceiptModelHolder INSTANCE = new ReceiptModelHolder();

    private ReceiptModelHolder() {
    }

    public static ReceiptModelHolder getInstance() { return INSTANCE; }

    public void setReceiptModel(ReceiptModel u) { this.receiptModel = u; }

    public ReceiptModel getReceiptModel() { return this.receiptModel; }
}
