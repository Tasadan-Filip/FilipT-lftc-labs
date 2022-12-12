package Grammar;

import Helpers.ProductionDotIndexTuple;

import java.util.*;
import java.util.stream.Collectors;

public class ParserLR0 {
    private Grammar grammar;

    public ParserLR0(Grammar grammar) {
        this.grammar = grammar;
    }

    public Map<String, List<ProductionDotIndexTuple>>  computeClosureLR0(String source, List<ProductionDotIndexTuple> initialProductions)
    {
        // A -> .ABa
        // source: A, destination: [A B a], dotIndex = 0

        Map<String, List<ProductionDotIndexTuple>> closure = new HashMap<>();
        closure.put(source, initialProductions);
        var isClosureChanged = true;

        while (isClosureChanged) {
            isClosureChanged = false;
            for(var productionDotTupleList: closure.values()) {
                for(var productionDotTuple: productionDotTupleList) {
                    var currentProductionRhs = productionDotTuple.getProductionRhs();
                    var currentDotIndex = productionDotTuple.getDotIndex();
                    if(currentDotIndex < currentProductionRhs.size()) {
                        var currentSource = currentProductionRhs.get(currentDotIndex);
                        if(currentSource.charAt(0) >= 'A' && currentSource.charAt(0) <= 'Z') {
                            for(var production : this.grammar.productionMap.get(currentSource)) {
                                var productionRhs = Arrays.asList(production.split(","));
                                var currentSourceProductionsList = closure.get(currentSource);
                                var isProductionAlreadyInList = false;

                                for(var productionDotTupleToCheck: currentSourceProductionsList) {
                                    var productionDotTupleToCheckRhs = productionDotTupleToCheck.getProductionRhs();
                                    if(productionDotTupleToCheckRhs.size() == productionRhs.size()) {
                                        var areProductionSymbolsAllDifferent = true;
                                        for(int i = 0; i < productionRhs.size(); i++) {
                                            if(!productionDotTupleToCheckRhs.get(i).equals(productionRhs.get(i))) {
                                                areProductionSymbolsAllDifferent = false;
                                                break;
                                            }
                                        }
                                        if(areProductionSymbolsAllDifferent) {
                                            isProductionAlreadyInList = true;
                                            break;
                                        }
                                    }
                                }
                                if(!isProductionAlreadyInList) {
                                    currentSourceProductionsList.add(new ProductionDotIndexTuple(productionRhs, 0));
                                    isClosureChanged = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return closure;
    }

    private List<Map<String, List<ProductionDotIndexTuple>>> goTo(Map<String, List<ProductionDotIndexTuple>> state, String X)
    {
        List<Map<String, List<ProductionDotIndexTuple>>> C = new ArrayList<>();

        Map <String, List<ProductionDotIndexTuple>> ans = new HashMap<>();
        for (var production : state.entrySet()) {
            var lhs = production.getKey();
            var C2 = new ArrayList<ProductionDotIndexTuple>();
            for (var rhs : production.getValue()) {
                int index = rhs.getDotIndex();
                if (!rhs.getProductionRhs().contains(X)) continue;
                if (rhs.getProductionRhs().indexOf(X) == rhs.getDotIndex())
                {
                    C2.add(new ProductionDotIndexTuple(rhs.getProductionRhs(), index+1));
                }
            }
            C.add(computeClosureLR0(lhs, C2));
        }
        return C;
    }

    public List<Map<String, List<ProductionDotIndexTuple>>> colCanLR()
    {
        List<Map<String, List<ProductionDotIndexTuple>>> C = new ArrayList<>();

        C.add(computeClosureLR0("S", List.of(new ProductionDotIndexTuple(List.of("S"), 0))));

        var grammarElements = grammar.nonterminalList;
        grammarElements.addAll(grammar.terminalList);

        boolean done = false;
        while (!done) {
            done = true;
            for (var state : C) {
                for (var X : grammarElements) {
                    var gotoSubcall = goTo(state, X);
                    if (gotoSubcall != null && !gotoSubcall.isEmpty() && C.contains(gotoSubcall)) {
                        C.addAll(gotoSubcall);
                    }
                }
            }
        }

        return C;
    }
}
