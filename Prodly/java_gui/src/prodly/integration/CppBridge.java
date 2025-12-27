package prodly.integration;

import java.io.*;

public class CppBridge {

    public static int runEvaluation(int dsa, int oop, int db) throws Exception {
        FileWriter fw = new FileWriter("integration/input/input.txt");
        fw.write(dsa + " " + oop + " " + db);
        fw.close();

        ProcessBuilder pb = new ProcessBuilder("prodly_core");
        Process p = pb.start();
        p.waitFor();

        BufferedReader br = new BufferedReader(
                new FileReader("integration/output/result.txt"));
        int level = Integer.parseInt(br.readLine());
        br.close();

        return level;
    }
}
