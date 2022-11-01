package FileOperations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToFile {
    public static void Write(String fileName, String fileContent) {
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(fileContent);
            myWriter.close();
            System.out.println("Successfully wrote to the " + fileName + ".");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
