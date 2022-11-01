package Tests;

import FileOperations.ReadFromFile;
import FileOperations.WriteToFile;
import Scanner.Scanner;

import java.io.FileNotFoundException;

public class TestScanner {
    public static void main(String[] args) throws Exception {
        System.out.print("P1 syntactical analysis: ");
        String programText_P1 = ReadFromFile.Read("src\\P1.txt");
        Scanner scanner_P1 = new Scanner(programText_P1);
        System.out.println(AnalyzeProgram(scanner_P1));
        WriteToFile.Write("src\\Pif_P1.out", scanner_P1.getPif().toString());
        WriteToFile.Write("src\\SymbolTable_P1.out", scanner_P1.getSymbolTable().toString());

        System.out.print("P2 syntactical analysis: ");
        String programText_P2 = ReadFromFile.Read("src\\P2.txt");
        Scanner scanner_P2 = new Scanner(programText_P2);
        System.out.println(AnalyzeProgram(scanner_P2));
        WriteToFile.Write("src\\Pif_P2.out", scanner_P2.getPif().toString());
        WriteToFile.Write("src\\SymbolTable_P2.out", scanner_P2.getSymbolTable().toString());

        System.out.print("P3 syntactical analysis: ");
        String programText_P3 = ReadFromFile.Read("src\\P3.txt");
        Scanner scanner_P3 = new Scanner(programText_P3);
        System.out.println(AnalyzeProgram(scanner_P3));
        WriteToFile.Write("src\\Pif_P3.out", scanner_P3.getPif().toString());
        WriteToFile.Write("src\\SymbolTable_P3.out", scanner_P3.getSymbolTable().toString());

        System.out.print("P1err syntactical analysis: ");
        String programText_P1err = ReadFromFile.Read("src\\P1err.txt");
        Scanner scanner_P1err = new Scanner(programText_P1err);
        System.out.println(AnalyzeProgram(scanner_P1err));
        WriteToFile.Write("src\\Pif_P1err.txt", scanner_P1err.getPif().toString());
        WriteToFile.Write("src\\SymbolTable_P1err.out", scanner_P1err.getSymbolTable().toString());
    }

    private static String AnalyzeProgram(Scanner scanner) throws Exception {
        var errorMessageList = scanner.AnalyzeString();
        if(errorMessageList.isEmpty()) {
            return "lexically correct";
        } else {
            var errorsString = "";
            for(var errorMessage : errorMessageList) {
                errorsString += "[" + errorMessage.toString() + "] ";
            }
            return errorsString;
        }
    }
}
