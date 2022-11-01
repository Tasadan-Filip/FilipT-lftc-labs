package Scanner;

import Errors.LexicalError;
import PIF.*;
import SymbolTable.SymbolTable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Scanner {
    private SymbolTable symbolTable;
    private PIF pif;
    private String programTextToBeVerified;

    public Scanner(String programTextToBeVerified) {
        this.symbolTable = new SymbolTable(11);
        this.pif = new PIF();
        this.programTextToBeVerified = programTextToBeVerified;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public PIF getPif() {
        return pif;
    }

    public List<LexicalError> AnalyzeString() throws Exception {
        List<String> linesList = Arrays.stream(this.programTextToBeVerified.split("\n")).collect(Collectors.toList());
        List<LexicalError> lexicalErrorMessages = new LinkedList<>();
        Integer lineNumber = 1;
        for(var line : linesList) {
            List<String> tokensList = Arrays.stream(line.split(" ")).collect(Collectors.toList());
            for(var token : tokensList) {
                var lexicalCheckReturnValue = CheckIfLexicallyCorrect(token);
                if(lexicalCheckReturnValue == -1) {
                    lexicalErrorMessages.add(new LexicalError(lineNumber, token));
                } else if(lexicalCheckReturnValue == 1) {
                    pif.AddToPif(new PifTuple(token, null));
                } else if(lexicalCheckReturnValue == 2) {
                    var position = symbolTable.AddElement(token);
                    pif.AddToPif(new PifTuple(token, position));
                } else if(lexicalCheckReturnValue == 3) {
                    var position = symbolTable.AddElement(token.split("\\[")[0]);
                    pif.AddToPif(new PifTuple(token, position));
                }
            }
            lineNumber += 1;
        }

        return lexicalErrorMessages;
    }

    private int CheckIfLexicallyCorrect(String token) throws Exception {
        if(token.isEmpty()) {
            return 0;
        }
        if(CheckIfOperator(token) || CheckIfSeparator(token) || CheckIfKeyword(token)) {
            return 1;
        }
        if(CheckIfIdentifier(token) || CheckIfConstant(token)) {
            return 2;
        }
        if(CheckIfArray(token)) {
            return 3;
        }
        return -1;
    }

    private boolean CheckIfOperator(String token) {
        return token.matches("(\\+)|(-)|(\\*)|(/)|(==)|(!=)|(<)|(>)|(<=)|(=)|(>=)|(%)");
    }

    private boolean CheckIfSeparator(String token) {
        return token.matches("(\\[)|(])|(\\{)|(})|(;)|\\(|\\)");
    }

    private boolean CheckIfKeyword(String token) {
        return token.matches("char|const|for|else|if|int|bool|read|write|var|while");
    }

    private boolean CheckIfIdentifier(String token) {
        return token.matches("[a-zA-Z]+[a-zA-Z0-9]*");
    }

    private boolean CheckIfConstant(String token) {
        return CheckIfIntegerConstant(token) ||
                CheckIfCharacterConstant(token) ||
                CheckIfStringConstant(token);
    }

    private boolean CheckIfArray(String token) {
        return token.matches("[a-zA-z]+[a-zA-Z0-9]*\\[[1-9]+[0-9]*]");
    }

    private boolean CheckIfIntegerConstant(String token) {
        return token.matches("[0-9]|[1-9]+[0-9]*");
    }

    private boolean CheckIfCharacterConstant(String token) {
        return token.matches("'[a-zA-Z0-9]'");
    }

    private boolean CheckIfStringConstant(String token) {
        return token.matches("\"[a-zA-Z0-9]*\"");
    }


}
