package prodly.integration;

import java.io.BufferedReader;
import java.io.FileReader;

public class OutputReader {

    public static String read() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("output.json"));
        String line;
        StringBuilder sb = new StringBuilder();

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        return sb.toString();
    }
}
