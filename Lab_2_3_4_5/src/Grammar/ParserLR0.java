package Grammar;

import Helpers.ProductionDotIndexTuple;
import Tests.TestFA;
import com.sun.source.tree.Tree;

import java.util.*;
import java.util.stream.Collectors;

public class ParserLR0 {
    private TreeNode root = null;
    private Grammar grammar;
    private Map<Integer, String> actionTable = new HashMap<>();

    private final List<Map<String, Integer>> gotoTable = new ArrayList<>();

    public ParserLR0(Grammar grammar) {
        this.grammar = grammar;
    }

    public Map<String, List<ProductionDotIndexTuple>> computeClosureLR0(String source, List<ProductionDotIndexTuple> initialProductions)
    {
        // A -> .ABa
        // source: A, initialProductions: {[A B a], dotIndex = 0}

        Map<String, List<ProductionDotIndexTuple>> closure = new HashMap<>();
        closure.put(source, initialProductions);
        var isClosureChanged = true;

        while (isClosureChanged) {
            isClosureChanged = false;
            List<ProductionDotIndexTuple> closureValues = new ArrayList<>();
            for (var productionDotTupleList: closure.values()) {
                for (var productionDotTuple: productionDotTupleList) {
                    closureValues.add(new ProductionDotIndexTuple(
                            productionDotTuple.getProductionSource(),
                            productionDotTuple.getProductionRhs(),
                            productionDotTuple.getDotIndex()));
                }
            }
            for (var productionDotTuple: closureValues) {
                    var currentProductionRhs = productionDotTuple.getProductionRhs();
                    //  currentProductionRhs = [A B a]
                    var currentDotIndex = productionDotTuple.getDotIndex();
                    //  currentDotIndex = 0
                    if (currentDotIndex < currentProductionRhs.size()) {
                        var currentSource = currentProductionRhs.get(currentDotIndex);
                        if (grammar.nonterminalList.contains(currentSource)) {
                            for (var production : this.grammar.productionMap.get(currentSource)) {
                                if (production.isEmpty()) continue;
                                //  productionRhs = [A B]
                                if (!closure.containsKey(currentSource)) {
                                    closure.put(currentSource, new ArrayList<>(List.of(new ProductionDotIndexTuple(currentSource, production, 0))));
                                    isClosureChanged = true;
                                    continue;
                                }
                                var currentSourceProductionsList = closure.get(currentSource);
                                //  currentSourceProductionsList = [{[A B], dotIndex - }]
                                var isProductionAlreadyInList = false;

                                for (var productionDotTupleToCheck: currentSourceProductionsList) {
                                    var productionDotTupleToCheckRhs = productionDotTupleToCheck.getProductionRhs();
                                    if (productionDotTupleToCheckRhs.size() == production.size()) {
                                        var areProductionSymbolsTheSame = true;
                                        for(int i = 0; i < production.size(); i++) {
                                            if(!productionDotTupleToCheckRhs.get(i).equals(production.get(i))) {
                                                areProductionSymbolsTheSame = false;
                                                break;
                                            }
                                        }
                                        if (areProductionSymbolsTheSame && productionDotTupleToCheck.getDotIndex() == 0) {
                                            isProductionAlreadyInList = true;
                                            break;
                                        }
                                    }
                                }
                                if(!isProductionAlreadyInList) {
                                    currentSourceProductionsList.add(new ProductionDotIndexTuple(currentSource, production, 0));
                                    isClosureChanged = true;
                                }
                            }
                        }
                    }
            }
        }

        return closure;
    }

    private void populateActionTable(List<Map<String, List<ProductionDotIndexTuple>>> closure, Map<String, List<List<String>>>  initialProductionsMap) {
        Integer currentStateIndex = 0;

        for(var stateProductions : closure) {
            if (isAccept(stateProductions) && isReduce(stateProductions)) {
                System.out.println("Shift-Reduce conflict detected!");
                System.out.println(stateProductions);
            }
            else if(isAccept(stateProductions)) {
                actionTable.put(currentStateIndex, "acc");
            } else if(isReduce(stateProductions)) {
                var onlyKey = stateProductions.keySet().toArray()[0].toString();
                var production = stateProductions.get(onlyKey).get(0);
                actionTable.put(currentStateIndex, "reduce " + getInitialProductionIndex(production));
            } else {
                actionTable.put(currentStateIndex, "shift");
            }
            currentStateIndex = currentStateIndex + 1;
        }
    }

    private boolean isAccept(Map<String, List<ProductionDotIndexTuple>> stateProductions) {
        var onlyKey = stateProductions.keySet().toArray()[0].toString();
        var production = stateProductions.get(onlyKey).get(0);
        return  stateProductions.size() == 1 &&
                production.getProductionSource().compareTo("S'") == 0 &&
                production.getProductionRhs().size() == 1 &&
                production.getProductionRhs().get(0).compareTo("S") == 0 &&
                production.getDotIndex() == 1;
    }

    private boolean isReduce(Map<String, List<ProductionDotIndexTuple>> stateProductions) {
        var onlyKey = stateProductions.keySet().toArray()[0].toString();
        var production = stateProductions.get(onlyKey).get(0);
        //  false negative
        System.out.println(stateProductions);
        if (stateProductions.containsKey("S'")) return false;
        //  conflict detection
        if (stateProductions.size() > 1) {
            int count = 0;
            for (var key : stateProductions.keySet()) {
                var value = stateProductions.get(key);
                for (var prod : value) {
                    if (prod.getDotIndex() == prod.getProductionRhs().size())
                        count ++;
                }
            }
            if (count > 1) {
                System.out.println("Reduce-reduce conflict!");
                System.out.println(stateProductions);
            }
            if (count == 1) {
                return true;
            }
        }
        return  stateProductions.size() == 1 &&
                production.getDotIndex() == production.getProductionRhs().size();
    }

    private int getInitialProductionIndex(ProductionDotIndexTuple production) {
        int index = 0;

        for(var initialProductionAsStringList : grammar.productionList) {
            var productionRhs = production.getProductionRhs();
            if (productionRhs.size() == initialProductionAsStringList.size() - 1) {
                boolean areRhsTheSame = true;
                for (int i = 0; i < productionRhs.size(); i++) {
                    if (productionRhs.get(i).compareTo(initialProductionAsStringList.get(1 + i)) != 0) {
                        areRhsTheSame = false;
                        break;
                    }
                }
                if (areRhsTheSame) {
                    return index;
                }
            }
            index = index + 1;
        }

        return -1;
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
                if (rhs.getProductionRhs().isEmpty()) continue;
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

        C.add(computeClosureLR0("S'", new ArrayList<>(List.of(new ProductionDotIndexTuple("S'",List.of("S"), 0)))));

        var grammarElements = new ArrayList<>(grammar.nonterminalList);
        grammarElements.addAll(grammar.terminalList);

        boolean done = false;
        while (!done) {
            done = true;
            List<Map<String, List<ProductionDotIndexTuple>>> toAdd = new ArrayList<>();
            for (var state : C) {
                var idxOfState = C.indexOf(state);
                for (var X : grammarElements) {
                    var gotoSubcall = goTo(state, X);
                    for (var newState : gotoSubcall) {
                        if (newState.toString().contains("[]")) {
                            continue;
                        }
                        if (C.toString().contains(newState.toString())) {
                            var otherIdx = 0;
                            var idxOf = -1;
                            for (var other : C) {
                                if (other.toString().equals(newState.toString())) {
                                    idxOf = otherIdx;
                                }
                                else {
                                    otherIdx += 1;
                                }
                            }
                            GotoTable.register(idxOfState, X, idxOf, gotoTable);
                        }
                        else if (!newState.isEmpty() && !C.contains(newState)) {
                            GotoTable.register(idxOfState, X, C.size(), gotoTable);
                            toAdd.add(newState);
                            done = false;
                        }
                    }
                }
            }
            C.addAll(toAdd);
        }

        return C;
    }

    public int gotoOp(int stateIdx, String element) {
        return GotoTable.get(gotoTable, stateIdx, element);
    }

    public void printGotoTable() {
        System.out.println("GoTo table: ");
        var C = colCanLR();

        for (int stateId=0; stateId<C.size(); stateId++) {
            System.out.println(stateId + ": " + C.get(stateId));
        }

        System.out.print("    ");
        for (var nonterm : grammar.nonterminalList) {
            System.out.print(nonterm + " ");
        }
        for (var term : grammar.terminalList) {
            System.out.print(term + " ");
        }
        System.out.println("");

        for (int stateId=0; stateId<C.size(); stateId++) {
            System.out.print(stateId + " |");
            for (var nonterm : grammar.nonterminalList) {
                System.out.print(gotoOp(stateId, nonterm) + " ");
            }
            for (var term : grammar.terminalList) {
                System.out.print(gotoOp(stateId, term) + " ");
            }
            System.out.println("");
        }
    }

    public void printActionTable() {
        System.out.println("Action table: ");
        var lr0Closure = colCanLR();
        populateActionTable(lr0Closure, grammar.productionMap);
        for(var actionTableKey : actionTable.keySet().stream().sorted().collect(Collectors.toList())) {
            System.out.println(actionTableKey + " | " + actionTable.get(actionTableKey));
        }

//        for (int stateId=0; stateId<lr0Closure.size(); stateId++) {
//            System.out.print(stateId + " |");
//            for (var nonterm : grammar.nonterminalList) {
//                System.out.print(gotoOp(stateId, nonterm) + " ");
//            }
//            for (var term : grammar.terminalList) {
//                System.out.print(gotoOp(stateId, term) + " ");
//            }
//            System.out.println("");
//        }
    }

    public boolean checkIfStringIsAcceptedByGrammar(String inputString) throws Exception {
        // Work stack is represented as a string in order to be easier for us to find and replace the corresponding symbols
        // when doing reduce. The digits (in our case: 0-6) represent the states, capital letters represent the non-terminals and
        // lowercase letters represent terminals
        // We initialize the work stack with 0
        String workStack = "0";
        String inputStack = inputString;
        String outputBand = "";
        boolean isAccepted = false;
        List <TreeNode> children = new ArrayList<>();

        while (!isAccepted) {
            var workStackTopStateId = workStack.charAt(workStack.length() - 1) - '0';
            var action = actionTable.get(workStackTopStateId);
            if(action.compareTo("acc") == 0) {
                System.out.println("Output band: " + outputBand);
                System.out.println("Work stack: " + workStack);
                System.out.println("Input stack: " + inputStack);
                System.out.println(children.get(0));
                return true;
            } else if(action.compareTo("shift") == 0) {
                // symbol is either a terminal or a non-terminal
                var symbol = inputStack.charAt(0);
                workStack = workStack + symbol;
                inputStack = inputStack.substring(1);
                var nextStateId = gotoOp(workStackTopStateId, String.valueOf(symbol));
                workStack = workStack + nextStateId;
                if (grammar.terminalList.contains(String.valueOf(symbol))) children.add(new TreeNode(Character.toString(symbol), new ArrayList<>()));
            } else {
                var reduceAction = action.split(" ");
                var reduceProductionId = reduceAction[1].charAt(0) - '0';
                var reduceProduction = grammar.productionList.get(reduceProductionId);
                List <String> needed = reduceProduction.subList(1, reduceProduction.size());
                needed = new ArrayList<>(needed);
                List <TreeNode> sublist = new ArrayList<>();
                for (var child: children) {
                    if (needed.contains(child.name)) {
                        needed.remove(child.name);
                        if (!sublist.isEmpty()) {
                            sublist.get(sublist.size()-1).rightSibling = child.index;
                        }
                        sublist.add(child);
                    }
                }
                for (var child : sublist) {
                    children.remove(child);
                }
                children.add(new TreeNode(reduceProduction.get(0), sublist));
                var reduceProductionRhsSymbolsList = reduceProduction.subList(1, reduceProduction.size());
                // do the actual reduction
                var workStackIndex = workStack.length() - 1;
                for(int reduceProductionSymbolsIndex = reduceProductionRhsSymbolsList.size() - 1; reduceProductionSymbolsIndex >= 0; reduceProductionSymbolsIndex--) {
                    var isSymbolInWorkStack = false;
                    workStackIndex = workStack.length() - 1;
                    var reduceProductionCurrentSymbol = reduceProductionRhsSymbolsList.get(reduceProductionSymbolsIndex);
                    while (workStackIndex >= 0) {
                        if(workStack.charAt(workStackIndex) == reduceProductionCurrentSymbol.charAt(0)) {
                            isSymbolInWorkStack = true;
                            break;
                        }
                        workStackIndex = workStackIndex - 1;
                    }
                    if(!isSymbolInWorkStack) {
                        throw new Exception("Symbol is not in work stack. There is an error!");
                    }
                }
                outputBand = reduceProductionId + outputBand;
                workStack = workStack.substring(0, workStackIndex);
                inputStack = inputStack + reduceProduction.get(0);
            }
        }

        return false;
    }
}
