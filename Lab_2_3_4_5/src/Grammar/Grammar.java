package Grammar;

import FileOperations.ReadFromFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grammar {
    List<String> nonterminalList;
    List<String> terminalList;
    String startingSymbol;
    List<String> productionListAsString;
    // the element from the first position (index = 0) will be the source, and from index = 1 to ... are symbols of the lhs of production
    List<List<String>> productionList = new ArrayList<>();
    // A -> ['A,b,C', 'C,b']
    Map<String, List<List<String>>> productionMap;

    public Grammar(String filePath) throws Exception {
        String fileText = ReadFromFile.Read(filePath);
        List<String> linesList = Arrays.stream(fileText.split("\n")).collect(Collectors.toList());
        productionMap = new HashMap<>();
        int currentLineNumber = 1;
        productionListAsString = new LinkedList<>();
        for(var line: linesList) {
            if(currentLineNumber == 1) {
                nonterminalList = Arrays.stream(line.split(",")).collect(Collectors.toList());
            }
            if(currentLineNumber == 2) {
                terminalList = Arrays.stream(line.split(",")).collect(Collectors.toList());
            }
            if(currentLineNumber == 3) {
                startingSymbol = line;
            }
            if(currentLineNumber >= 4) {
                var lineProductions = Arrays.stream(line.split(";")).collect(Collectors.toList());
                productionListAsString = Stream.concat(productionListAsString.stream(), lineProductions.stream()).collect(Collectors.toList());
            }
            currentLineNumber = currentLineNumber + 1;
        }
        TransformProductionStringListToMap();
    }

    public void DisplayElementMain() {
        for(;;) {
            DisplayMenu();
            Scanner in = new Scanner(System.in);
            int command = in.nextInt();
            DisplayRequestedGrammarElement(command);
        }
    }

    public void DisplayMenu() {
        System.out.println("Grammar elements:");
        System.out.println("  1. Nonterminals");
        System.out.println("  2. Terminals");
        System.out.println("  3. Starting Symbol");
        System.out.println("  4. Set of Productions");
        System.out.println("Choose the index of the Grammar element that you want to display");
        System.out.print(">> ");
    }

    public void DisplayRequestedGrammarElement(int command) {
        switch (command) {
            case 1:
                DisplayStringList(nonterminalList);
                break;
            case 2:
                DisplayStringList(terminalList);
                break;
            case 3:
                System.out.println(startingSymbol);
                break;
            case 4:
                DisplayStringList(productionListAsString);
                break;
            default:
                break;
        }
    }

    private void DisplayStringList(List<String> stringList) {
        for(var string : stringList) {
            if(string.contains("$")) {
                string = string.replace("$","->");
            }
            System.out.println(string);
        }
    }

    private void TransformProductionStringListToMap() throws Exception {
        productionList.add(new ArrayList<>());
        for(var productionString : productionListAsString) {
            var transitionStringSplit = productionString.split("\\$");
            var lhs = transitionStringSplit[0];
            var rhs = transitionStringSplit[1];
            if(!IsProductionCFG(lhs)) {
                throw new Exception("This Grammar is not a CFG");
            }
            List<String> rhsList;
            if(rhs.contains(",")) {
                rhsList = Arrays.asList(rhs.split(","));
            } else {
                rhsList = new LinkedList<String>();
                rhsList.add(rhs);
            }
            if (!productionMap.containsKey(lhs)) {
                productionMap.put(lhs, new ArrayList<>());
            }
            var newProduction = new ArrayList<String>();
            newProduction.add(lhs);
            newProduction.addAll(rhsList);
            productionList.add(newProduction);
            productionMap.get(lhs).add(rhsList);
        }
    }

    private boolean IsProductionCFG(String productionLhs) {
        var productionLhsList = productionLhs.split(",");
        if(productionLhsList.length == 1) {
            return true;
        }
        return false;
    }
}
