package prodly.integration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

public class OutputReader {

    public static String read() throws Exception {
        // Read from parent directory (Prodly) where C++ writes it
        File currentDir = new File(System.getProperty("user.dir"));
        File outputFile;
        
        if (currentDir.getName().equals("java_gui")) {
            // We're in java_gui, read from parent directory
            outputFile = new File(currentDir.getParent(), "output.json");
        } else {
            // Read from current directory
            outputFile = new File("output.json");
        }
        
        BufferedReader br = new BufferedReader(new FileReader(outputFile));
        String line;
        StringBuilder sb = new StringBuilder();

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        return sb.toString();
    }
}
