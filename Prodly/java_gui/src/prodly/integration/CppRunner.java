package prodly.integration;

import java.io.File;

public class CppRunner {

    public static void runCore() throws Exception {
        // Determine executable path based on OS
        String os = System.getProperty("os.name").toLowerCase();
        String exePath;
        
        if (os.contains("win")) {
            // Windows
            exePath = ".." + File.separator + "cpp_core" + File.separator + "prodly_core.exe";
        } else {
            // Linux/Mac
            exePath = ".." + File.separator + "cpp_core" + File.separator + "prodly_core";
        }
        
        File exeFile = new File(exePath);
        if (!exeFile.exists()) {
            // Try alternative path
            exePath = "cpp_core" + File.separator + "prodly_core.exe";
            exeFile = new File(exePath);
        }
        
        ProcessBuilder pb = new ProcessBuilder(exePath);
        pb.directory(new File("."));
        pb.redirectErrorStream(true);
        Process p = pb.start();
        p.waitFor();
    }
}
