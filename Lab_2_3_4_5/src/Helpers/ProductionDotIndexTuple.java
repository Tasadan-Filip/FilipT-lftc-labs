package Helpers;

import java.util.List;
import java.util.Map;

public class ProductionDotIndexTuple {
    List<String> productionRhs;     //["A", "B", "C"]
    int dotIndex;                   // 1
    //  S -> A.BC
    //  X = B
    //  S -> AB.C

    public ProductionDotIndexTuple(List<String> productionRhs, int dotIndex) {
        this.productionRhs = productionRhs;
        this.dotIndex = dotIndex;
    }

    public List<String> getProductionRhs() {
        return productionRhs;
    }

    public void setProductionRhs(List<String> productionRhs) {
        this.productionRhs = productionRhs;
    }

    public int getDotIndex() {
        return dotIndex;
    }

    public void setDotIndex(int dotIndex) {
        this.dotIndex = dotIndex;
    }
}
