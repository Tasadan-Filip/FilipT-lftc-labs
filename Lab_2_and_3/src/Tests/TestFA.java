package Tests;

import FA.FA;
import FileOperations.ReadFromFile;
import FileOperations.WriteToFile;
import Scanner.Scanner;

import java.io.FileNotFoundException;

public class TestFA {
    public static void main(String[] args) throws Exception {
        FA testFa = new FA("src\\FA.in");
//         testFa.DisplayElementMain();
         var verificationResult = testFa.VerifyIfSequenceIsAcceptedByFA("10000");
         System.out.println(verificationResult);
    }
}
