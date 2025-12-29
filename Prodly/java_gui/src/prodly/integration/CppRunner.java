package prodly.integration;

public class CppRunner {

    public static void runCore() throws Exception {
        ProcessBuilder pb = new ProcessBuilder(
            "..\\cpp_core\\prodly_core.exe"
        );
        pb.inheritIO();
        Process p = pb.start();
        p.waitFor();
    }
}
