package SymbolTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SymbolTable {
    private List<List<String>> tableStructure;
    int tableSize;

    public SymbolTable(int tableSize) {
        this.tableStructure = new ArrayList<>();
        this.tableSize = tableSize;
        this.InitializeSymbolTable();
    }

    public SymbolTablePositionModel AddElement(String elementToAdd) throws Exception {
        int elementHashValue = this.HashFunction(elementToAdd);
        int elementListPosition = SearchForElementInTableList(elementToAdd, elementHashValue);
        if(elementListPosition == -1) {
            List<String> tableList = this.tableStructure.get(elementHashValue);
            if(tableList != null) {
                tableList.add(elementToAdd);
                return new SymbolTablePositionModel(elementHashValue, tableList.size() - 1);
            } else {
                throw new Exception("Invalid hash value");
            }
        } else {
            return new SymbolTablePositionModel(elementHashValue, elementListPosition);
        }
    }

    public SymbolTablePositionModel GetPositionByElement(String elementToGet) throws Exception {
        int elementHashValue = this.HashFunction(elementToGet);
        int elementListPosition = SearchForElementInTableList(elementToGet, elementHashValue);
        if(elementListPosition == -1) {
            return new SymbolTablePositionModel(elementHashValue, elementListPosition);
        } else {
            throw new Exception("Element does not exist");
        }
    }

    private void InitializeSymbolTable() {
        for(int index = 0; index < this.tableSize; index++) {
            this.tableStructure.add(new ArrayList<>());
        }
    }

    private int HashFunction(String stringToHash) {
        int hashValue = 7;
        for (int index = 0; index < stringToHash.length(); index++) {
            hashValue = hashValue * 70 + stringToHash.charAt(index);
        }

        return hashValue % tableSize;
    }

    private int SearchForElementInTableList(String elementToFind, int hashValue) {
        List<String> tableList = this.tableStructure.get(hashValue);
        int currentPosition = 0;

        for(String listElement: tableList) {
            if(Objects.equals(elementToFind, listElement)) {
                return currentPosition;
            }
            currentPosition = currentPosition + 1;
        }

        return -1;
    }
}
