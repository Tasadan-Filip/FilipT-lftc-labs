package Grammar;

import Helpers.ProductionDotIndexTuple;
import Helpers.StringsTuple;

import java.util.*;
import java.util.stream.Collectors;

public class ParserLR0 {
    private Grammar grammar;
    private Map<String, String> actionTable;

    private List<Map<String, Integer>> gotoTable;

    public ParserLR0(Grammar grammar) {
        this.grammar = grammar;
    }

    public Map<String, List<ProductionDotIndexTuple>> computeClosureLR0(String source, List<ProductionDotIndexTuple> initialProductions)
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
                                    currentSourceProductionsList.add(new ProductionDotIndexTuple(currentSource ,productionRhs, 0));
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

    private void populateActionTable(List<List<ProductionDotIndexTuple>> closure, List<ProductionDotIndexTuple> initialProductions) {
        Integer currentStateIndex = 0;

        for(var stateProductions : closure) {
            if(isAccept(stateProductions)) {
                actionTable.put("s" + currentStateIndex, "acc");
            } else if(isReduce(stateProductions)) {
                actionTable.put("s" + currentStateIndex, "reduce " + getInitialProductionIndex(stateProductions.get(0), initialProductions));
            } else {
                actionTable.put("s" + currentStateIndex, "shift");
            }
            currentStateIndex = currentStateIndex + 1;
        }
    }

    private boolean isAccept(List<ProductionDotIndexTuple> stateProductions) {
        var production = stateProductions.get(0);
        return  stateProductions.size() == 1 &&
                production.getProductionSource().compareTo("S'") == 0 &&
                production.getProductionRhs().size() == 1 &&
                production.getProductionRhs().get(0).compareTo("S") == 0 &&
                production.getDotIndex() == 1;
    }

    private boolean isReduce(List<ProductionDotIndexTuple> stateProductions) {
        var production = stateProductions.get(0);
        return  stateProductions.size() == 1 &&
                production.getProductionRhs().size() == 1 &&
                production.getDotIndex() == 1;
    }

    private int getInitialProductionIndex(ProductionDotIndexTuple production, List<ProductionDotIndexTuple> initialProductionList) {
        int index = 0;
        for(var initialProduction : initialProductionList) {
            if(production.getProductionSource().compareTo(initialProduction.getProductionSource()) == 0
            ) {
                var productionRhs = production.getProductionRhs();
                var initialProductionRhs = initialProduction.getProductionRhs();
                if(productionRhs.size() == initialProductionRhs.size()) {
                    boolean areRhsTheSame = true;
                    for(int i = 0; i < productionRhs.size(); i++) {
                        if(productionRhs.get(i).compareTo(initialProductionRhs.get(i)) != 0) {
                            areRhsTheSame = false;
                        }
                    }
                    if(areRhsTheSame) {
                        return index;
                    }
                }
            }
            index = index + 1;
        }
    }

    private List<Map<String, List<ProductionDotIndexTuple>>> goTo(Map<String, List<ProductionDotIndexTuple>> state, String X)
    {
        List<Map<String, List<ProductionDotIndexTuple>>> C = new ArrayList<>();

        for (var production : state.entrySet()) {
            var lhs = production.getKey();
            var C2 = new ArrayList<ProductionDotIndexTuple>();
            for (var rhs : production.getValue()) {
                int index = rhs.getDotIndex();
                if (!rhs.getProductionRhs().contains(X)) continue;
                if (rhs.getProductionRhs().indexOf(X) == rhs.getDotIndex())
                {
                    C2.add(new ProductionDotIndexTuple(lhs,rhs.getProductionRhs(), index+1));
                }
            }
            C.add(computeClosureLR0(lhs, C2));
        }
        return C;
    }

    public List<Map<String, List<ProductionDotIndexTuple>>> colCanLR()
    {
        List<Map<String, List<ProductionDotIndexTuple>>> C = new ArrayList<>();

        C.add(computeClosureLR0("S", List.of(new ProductionDotIndexTuple("S",List.of("S"), 0))));

        var grammarElements = grammar.nonterminalList;
        grammarElements.addAll(grammar.terminalList);

        boolean done = false;
        while (!done) {
            done = true;
            for (var state : C) {
                var idxOfState = C.indexOf(state);
                for (var X : grammarElements) {
                    var gotoSubcall = goTo(state, X);
                    for (var newState : gotoSubcall) {
                        if (!newState.isEmpty() && C.contains(newState)) {
                            GotoTable.register(idxOfState, X, C.size(), gotoTable);
                            C.add(newState);
                        }
                        if (C.contains(newState)) {
                            var idxOf = C.indexOf(newState);
                            GotoTable.register(idxOf, X, idxOf, gotoTable);
                        }
                    }
                }
            }
        }

        return C;
    }

    public int gotoOp(int stateIdx, String element) {
        return GotoTable.get(gotoTable, stateIdx, element);
    }

    public void printGotoTable() {
        var C = colCanLR();

        for (int stateId=0; stateId<C.size(); stateId++) {
            System.out.println(stateId + ": " + C.get(stateId));
        }

        System.out.println("  |");
        for (var nonterm : grammar.nonterminalList) {
            System.out.println(nonterm);
        }
        for (var term : grammar.nonterminalList) {
            System.out.println(term);
        }

        for (int stateId=0; stateId<C.size(); stateId++) {
            System.out.println(stateId + " |");
            for (var nonterm : grammar.nonterminalList) {
                System.out.println(gotoOp(stateId, nonterm));
            }
            for (var term : grammar.nonterminalList) {
                System.out.println(gotoOp(stateId, term));
            }
        }
    }
}
