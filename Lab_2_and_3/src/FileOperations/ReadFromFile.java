package FileOperations;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadFromFile {
    public static String Read(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner sc = new Scanner(file);
        String programText = "";
        while (sc.hasNextLine()) {
            programText += sc.nextLine() + "\n";
        }

        return programText;
    }
}
