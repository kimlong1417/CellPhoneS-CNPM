package holders;

import entities.Imports;

public final class ImportsHolder {

    private Imports imports;
    private final static ImportsHolder INSTANCE = new ImportsHolder();

    private ImportsHolder() {}

    public static ImportsHolder getInstance() { return INSTANCE; }

    public void setImports(Imports i) { this.imports = i; }

    public Imports getImports() { return this.imports; }
}
