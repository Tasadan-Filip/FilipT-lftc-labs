package Tests;

import SymbolTable.SymbolTable;
import SymbolTable.SymbolTablePositionModel;
import java.util.LinkedList;
import java.util.List;

public class TestSymbolTable {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable(613);
        try {
            List<SymbolTablePositionModel> positionsList = new LinkedList<>();
            var position1 = symbolTable.AddElement("id1");
            var position2 = symbolTable.AddElement("id2");
            var position3 = symbolTable.AddElement("id1");
            var position4 = symbolTable.AddElement("cons1");
            positionsList.add(position1);
            positionsList.add(position2);
            positionsList.add(position3);
            positionsList.add(position4);
           System.out.println(positionsList);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
