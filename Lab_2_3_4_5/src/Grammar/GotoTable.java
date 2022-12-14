package Grammar;

import Helpers.ProductionDotIndexTuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GotoTable {
    public static List<Map<String, Integer>> initTable(List<String> nonterminalsAndTerminals, List<Map<String, List<ProductionDotIndexTuple>>> states) {
        List<Map<String, Integer>> table = new ArrayList<>();
        nonterminalsAndTerminals = nonterminalsAndTerminals;
        states = states;
        for (int i=0; i<states.size(); i++) {
            table.add(new HashMap<>());
            for (var elem : nonterminalsAndTerminals) {
                table.get(i).put(elem, -1);
            }
        }
        return table;
    }
}
