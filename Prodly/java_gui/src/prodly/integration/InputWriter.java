package prodly.integration;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class InputWriter {

    public static void write(String json) throws IOException {
        // Write to parent directory (Prodly) where C++ expects it
        File currentDir = new File(System.getProperty("user.dir"));
        File inputFile;
        
        if (currentDir.getName().equals("java_gui")) {
            // We're in java_gui, write to parent directory
            inputFile = new File(currentDir.getParent(), "input.json");
        } else {
            // Write to current directory
            inputFile = new File("input.json");
        }
        
        FileWriter writer = new FileWriter(inputFile);
        writer.write(json);
        writer.close();
    }
}
