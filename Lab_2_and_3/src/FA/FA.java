package FA;

import FileOperations.ReadFromFile;
import Helpers.StringsTuple;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class FA {
    private List<String> statesList;
    private List<String> alphabetList;
    private List<String> transitionAsStringList;
    private Map<StringsTuple, String> transitionMap;
    private String initialState;
    private List<String> finalStatesList;

    public FA(String filePath) throws Exception {
        String fileText = ReadFromFile.Read(filePath);
        List<String> linesList = Arrays.stream(fileText.split("\n")).collect(Collectors.toList());
        int currentLineNumber = 1;
        for(var line: linesList) {
            if(currentLineNumber == 1) {
                statesList = Arrays.stream(line.split("\\|")).collect(Collectors.toList());
            }
            if(currentLineNumber == 2) {
                alphabetList = Arrays.stream(line.split("\\|")).collect(Collectors.toList());
            }
            if(currentLineNumber == 3) {
                transitionAsStringList = Arrays.stream(line.split("\\|")).collect(Collectors.toList());
            }
            if(currentLineNumber == 4) {
                initialState = line;
            }
            if(currentLineNumber == 5) {
                finalStatesList = Arrays.stream(line.split("\\|")).collect(Collectors.toList());
            }
            currentLineNumber = currentLineNumber + 1;
        }
        transitionMap = new HashMap<>();
        TransformListStringToTransition();
    }

    public void DisplayElementMain() {
        for(;;) {
            DisplayMenu();
            Scanner in = new Scanner(System.in);
            int command = in.nextInt();
            DisplayRequestedFaElement(command);
        }
    }

    public void DisplayMenu() {
        System.out.println("FA elements:");
        System.out.println("  1. Set of States");
        System.out.println("  2. Alphabet");
        System.out.println("  3. Transition");
        System.out.println("  4. Initial State");
        System.out.println("  4. Final States");
        System.out.println("Choose the index of the FA element that you want to display");
        System.out.print(">> ");
    }

    public void DisplayRequestedFaElement(int command) {
        switch (command) {
            case 1:
                DisplayStringList(statesList);
                break;
            case 2:
                DisplayStringList(alphabetList);
                break;
            case 3:
                DisplayStringList(transitionAsStringList);
                break;
            case 4:
                System.out.println(initialState);
                break;
            case 5:
                DisplayStringList(finalStatesList);
            default:
                break;
        }
    }

    public boolean VerifyIfSequenceIsAcceptedByFA(String dfaSequence) {
        var currentState = this.initialState;
        int index;
        for (index = 0; index < dfaSequence.length(); index++) {
            currentState = this.transitionMap.get(new StringsTuple(currentState, Character.toString(dfaSequence.charAt(index))));
            if(currentState == null) {
                return false;
            }
        }
        return index == dfaSequence.length() && this.finalStatesList.contains(currentState);
    }

    private void DisplayStringList(List<String> stringList) {
        for(var string : stringList) {
            System.out.println(string);
        }
    }

    private void TransformListStringToTransition() throws Exception {
        for(var transitionString : transitionAsStringList) {
            var transitionStringSplited = transitionString.split("=");
            var source = transitionStringSplited[0].split(",")[0];
            var symbol = transitionStringSplited[0].split(",")[1];
            var destination = transitionStringSplited[1];
            var keyTuple = new StringsTuple(source, symbol);
            if(transitionMap.get(keyTuple) != null) {
                throw new Exception("This FA is not deterministic");
            }
            transitionMap.put(keyTuple, destination);
        }
    }
}
