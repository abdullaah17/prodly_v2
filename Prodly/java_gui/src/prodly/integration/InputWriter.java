package prodly.integration;

import java.io.FileWriter;
import java.io.IOException;

public class InputWriter {

    public static void write(String json) throws IOException {
        FileWriter writer = new FileWriter("input.json");
        writer.write(json);
        writer.close();
    }
}
