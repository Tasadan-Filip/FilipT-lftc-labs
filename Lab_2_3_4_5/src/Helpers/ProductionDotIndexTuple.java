package Helpers;

import java.util.List;
import java.util.Map;

public class ProductionDotIndexTuple {
    String productionSource;
    List<String> productionRhs;     //["A", "B", "C"]
    int dotIndex;                   // 1
    //  S -> A.BC
    //  X = B
    //  S -> AB.C


    public ProductionDotIndexTuple(String productionSource, List<String> productionRhs, int dotIndex) {
        this.productionSource = productionSource;
        this.productionRhs = productionRhs;
        this.dotIndex = dotIndex;
    }

    public String getProductionSource() {
        return productionSource;
    }

    public void setProductionSource(String productionSource) {
        this.productionSource = productionSource;
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
