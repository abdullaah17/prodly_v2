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
        
        // Get absolute path to executable
        if (!exeFile.isAbsolute()) {
            // Make path relative to current working directory (java_gui)
            File currentDir = new File(System.getProperty("user.dir"));
            if (currentDir.getName().equals("java_gui")) {
                // We're in java_gui, so go up one level
                exeFile = new File(currentDir.getParent(), exePath);
            } else {
                exeFile = new File(currentDir, exePath);
            }
        }
        
        // Set working directory to parent of cpp_core (Prodly directory)
        // This is where C++ expects input.json and output.json
        File workingDir = exeFile.getParentFile().getParentFile();
        
        ProcessBuilder pb = new ProcessBuilder(exeFile.getAbsolutePath());
        pb.directory(workingDir);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        p.waitFor();
    }
}
