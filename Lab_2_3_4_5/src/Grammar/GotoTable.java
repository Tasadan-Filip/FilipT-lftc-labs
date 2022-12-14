package Grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GotoTable {
    public static void register(int firstState, String element, int secondState, List<Map<String, Integer>> gotoTable) {
        if (gotoTable == null) gotoTable = new ArrayList<>();
        while (firstState >= gotoTable.size()) gotoTable.add(new HashMap<>());
        gotoTable.get(firstState).put(element, secondState);
    }

    public static int get(List<Map<String, Integer>> gotoTable, int row, String elem) {
        if (row >= gotoTable.size()) return -1;
        if (!gotoTable.get(row).containsKey(elem)) return -1;
        return gotoTable.get(row).get(elem);
    }
}
