package PIF;

import SymbolTable.SymbolTablePositionModel;

import java.util.LinkedList;
import java.util.List;

public class PIF {
    List<PifTuple> pifList;

    public PIF() {
        pifList = new LinkedList<>();
    }

    public void AddToPif(PifTuple pifTuple) {
        pifList.add(pifTuple);
    }

    @Override
    public String toString() {
        var pifString = "";
        for(var pifTuple: pifList) {
            pifString += pifTuple.toString() + "\n";
        }

        return pifString;
    }
}
