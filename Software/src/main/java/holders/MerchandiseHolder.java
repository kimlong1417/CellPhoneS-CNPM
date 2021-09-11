package holders;

import entities.Merchandise;

public final class MerchandiseHolder {

    private Merchandise merchandise;
    private final static MerchandiseHolder INSTANCE = new MerchandiseHolder();

    private MerchandiseHolder() {
    }

    public static MerchandiseHolder getInstance() { return INSTANCE; }

    public void setMerchandise(Merchandise merchandise) { this.merchandise = merchandise; }

    public Merchandise getMerchandise() { return this.merchandise; }
}
