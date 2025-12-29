package prodly;

import prodly.integration.InputWriter;
import prodly.integration.CppRunner;
import prodly.integration.OutputReader;

public class Main {
    public static void main(String[] args) {
        try {
            String inputJson = "{ \"action\": \"ping\", \"source\": \"Java GUI\" }";

            InputWriter.write(inputJson);
            CppRunner.runCore();
            String response = OutputReader.read();

            System.out.println("C++ Response:");
            System.out.println(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
