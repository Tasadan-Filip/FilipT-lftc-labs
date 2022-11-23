package PIF;

import SymbolTable.SymbolTablePositionModel;

public class PifTuple {
    private String key;
    private SymbolTablePositionModel position;

    public PifTuple(String key, SymbolTablePositionModel position) {
        this.key = key;
        this.position = position;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SymbolTablePositionModel getPosition() {
        return position;
    }

    public void setPosition(SymbolTablePositionModel position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "(" +
                "key = '" + key + '\'' +
                ", position = " + position +
                ")";
    }
}
