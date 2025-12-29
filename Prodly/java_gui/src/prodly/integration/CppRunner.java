package prodly.integration;

import java.io.File;

public class CppRunner {

    public static void runCore() throws Exception {
        // Determine executable path based on OS
        String os = System.getProperty("os.name").toLowerCase();
        String exeName;
        
        if (os.contains("win")) {
            exeName = "prodly_core.exe";
        } else {
            exeName = "prodly_core";
        }
        
        // Get current working directory
        File currentDir = new File(System.getProperty("user.dir"));
        File exeFile = null;
        File workingDir = null;
        
        // Try multiple possible locations
        if (currentDir.getName().equals("java_gui")) {
            // We're in java_gui directory
            File parentDir = currentDir.getParentFile();
            exeFile = new File(parentDir, "cpp_core" + File.separator + exeName);
            workingDir = parentDir; // Prodly directory
        } else if (currentDir.getName().equals("Prodly")) {
            // We're in Prodly directory
            exeFile = new File(currentDir, "cpp_core" + File.separator + exeName);
            workingDir = currentDir;
        } else {
            // Try relative to current directory
            exeFile = new File("cpp_core" + File.separator + exeName);
            if (!exeFile.exists()) {
                exeFile = new File(".." + File.separator + "cpp_core" + File.separator + exeName);
            }
            workingDir = new File(".");
        }
        
        // Verify executable exists
        if (!exeFile.exists() || !exeFile.isFile()) {
            throw new Exception("C++ executable not found at: " + exeFile.getAbsolutePath() + 
                "\nPlease compile the C++ core first using: cd cpp_core && g++ -std=c++11 -I./include src/*.cpp main.cpp -o " + exeName);
        }
        
        // Use absolute paths
        String absoluteExePath = exeFile.getAbsolutePath();
        String absoluteWorkingDir = workingDir.getAbsolutePath();
        
        ProcessBuilder pb = new ProcessBuilder(absoluteExePath);
        pb.directory(new File(absoluteWorkingDir));
        pb.redirectErrorStream(true);
        Process p = pb.start();
        p.waitFor();
    }
}
