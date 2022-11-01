package SymbolTable;

public class SymbolTablePositionModel {
    private Integer listIndex;
    private Integer positionIndex;

    public SymbolTablePositionModel(int listIndex, int positionIndex) {
        this.listIndex = listIndex;
        this.positionIndex = positionIndex;
    }

    public int getListIndex() {
        return listIndex;
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    @Override
    public String toString() {
        return "(" +
                listIndex.toString() +
                ", " +
                positionIndex.toString() +
                ")";
    }
}
