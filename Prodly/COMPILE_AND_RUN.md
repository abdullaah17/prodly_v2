# PRODLY - Compilation and Running Guide

## Prerequisites

### Required Software:
1. **C++ Compiler**: 
   - Windows: MinGW-w64, MSVC, or Visual Studio
   - Linux: `g++` (usually pre-installed)
   - Mac: Xcode Command Line Tools (`g++` or `clang++`)

2. **Java JDK**: Version 8 or later
   - Download from: https://adoptium.net/ or Oracle JDK

3. **JSON Library for Java** (json-simple):
   - Download: https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple/1.1.1
   - Or use Maven/Gradle if preferred

## Step-by-Step Instructions

### Step 1: Compile the C++ Core

#### Windows (using MinGW or MSVC):

```bash
cd Prodly/cpp_core
g++ -std=c++11 -I./include src/*.cpp main.cpp -o prodly_core.exe
```

**OR using MSVC (Visual Studio Developer Command Prompt):**
```bash
cd Prodly/cpp_core
cl /EHsc /I./include src\*.cpp main.cpp /Fe:prodly_core.exe
```

#### Linux/Mac:

```bash
cd Prodly/cpp_core
g++ -std=c++11 -I./include src/*.cpp main.cpp -o prodly_core
```

**Verify compilation:**
- Windows: Check for `prodly_core.exe` in `cpp_core/` directory
- Linux/Mac: Check for `prodly_core` executable

### Step 2: Set Up Java Dependencies

#### Option A: Using NetBeans (Recommended)

1. **Download json-simple JAR:**
   - Download `json-simple-1.1.1.jar` from Maven Central
   - Place it in `Prodly/java_gui/lib/` directory (create `lib` folder if it doesn't exist)

2. **Open Project in NetBeans:**
   - Launch NetBeans
   - File → Open Project
   - Navigate to `Prodly/java_gui` folder
   - Click "Open Project"

3. **Add JSON Library:**
   - Right-click project → Properties
   - Libraries → Add JAR/Folder
   - Select `json-simple-1.1.1.jar` from `lib/` folder
   - Click OK

4. **Build Project:**
   - Right-click project → Build
   - Or press `F11`

#### Option B: Using Command Line

1. **Create lib directory and add JSON JAR:**
   ```bash
   cd Prodly/java_gui
   mkdir lib
   # Download json-simple-1.1.1.jar and place it in lib/
   ```

2. **Compile Java files:**
   ```bash
   cd Prodly/java_gui/src
   javac -cp "../lib/json-simple-1.1.1.jar" -d ../build/classes prodly/**/*.java
   ```

   **Note:** On Windows, use:
   ```bash
   javac -cp "..\lib\json-simple-1.1.1.jar" -d ..\build\classes prodly\**\*.java
   ```

### Step 3: Run the Application

#### Option A: Using NetBeans

1. **Set Main Class:**
   - Right-click project → Properties → Run
   - Main Class: `prodly.Main`
   - Click OK

2. **Run:**
   - Right-click project → Run
   - Or press `F6`
   - Or click the green play button

#### Option B: Using Command Line

**Windows:**
```bash
cd Prodly/java_gui
java -cp "build/classes;lib/json-simple-1.1.1.jar" prodly.Main
```

**Linux/Mac:**
```bash
cd Prodly/java_gui
java -cp "build/classes:lib/json-simple-1.1.1.jar" prodly.Main
```

### Step 4: Test the Application

1. **Login Screen should appear**
2. **Default credentials:**
   - Username: `admin`
   - Password: `admin123`
   - Role: `admin` (for signup)

3. **Or create a new account:**
   - Enter username and password
   - Select role (employee, manager, or admin)
   - Click "Sign Up"
   - Then login with your credentials

## Quick Start Scripts

### Windows Batch Script (`run.bat`)

Create `Prodly/run.bat`:
```batch
@echo off
echo Compiling C++ Core...
cd cpp_core
g++ -std=c++11 -I./include src/*.cpp main.cpp -o prodly_core.exe
if errorlevel 1 (
    echo C++ compilation failed!
    pause
    exit /b 1
)
echo C++ compilation successful!

echo.
echo Running Java GUI...
cd ../java_gui
java -cp "build/classes;lib/json-simple-1.1.1.jar" prodly.Main
pause
```

### Linux/Mac Shell Script (`run.sh`)

Create `Prodly/run.sh`:
```bash
#!/bin/bash
echo "Compiling C++ Core..."
cd cpp_core
g++ -std=c++11 -I./include src/*.cpp main.cpp -o prodly_core
if [ $? -ne 0 ]; then
    echo "C++ compilation failed!"
    exit 1
fi
echo "C++ compilation successful!"

echo ""
echo "Running Java GUI..."
cd ../java_gui
java -cp "build/classes:lib/json-simple-1.1.1.jar" prodly.Main
```

Make it executable:
```bash
chmod +x Prodly/run.sh
```

## Troubleshooting

### Issue: "prodly_core.exe not found" or "prodly_core not found"

**Solution:**
1. Verify the executable exists in `cpp_core/` directory
2. Check the path in `CppRunner.java` matches your executable name
3. On Linux/Mac, ensure executable has execute permissions:
   ```bash
   chmod +x cpp_core/prodly_core
   ```

### Issue: "ClassNotFoundException" for JSON classes

**Solution:**
1. Ensure `json-simple-1.1.1.jar` is in `java_gui/lib/` directory
2. Verify classpath includes the JAR file
3. In NetBeans: Right-click project → Clean and Build

### Issue: "input.json not found" error

**Solution:**
1. The Java GUI creates `input.json` automatically
2. Ensure you're running from `java_gui/` directory
3. Check file permissions

### Issue: C++ compilation errors

**Common fixes:**
1. **Missing includes:** Ensure all header files are in `cpp_core/include/`
2. **C++11 standard:** Use `-std=c++11` flag
3. **Path issues:** Run compilation from `cpp_core/` directory

### Issue: Java compilation errors

**Common fixes:**
1. **Missing JSON library:** Download and add to classpath
2. **Package structure:** Ensure files are in correct package directories
3. **JDK version:** Use JDK 8 or later

## Directory Structure After Compilation

```
Prodly/
├── cpp_core/
│   ├── prodly_core.exe (Windows) or prodly_core (Linux/Mac)
│   ├── include/
│   ├── src/
│   └── main.cpp
│
├── java_gui/
│   ├── build/classes/          (compiled .class files)
│   ├── lib/
│   │   └── json-simple-1.1.1.jar
│   ├── src/
│   └── input.json / output.json (created at runtime)
│
└── README.md
```

## Development Workflow

1. **Make changes to C++ code:**
   - Edit files in `cpp_core/src/` or `cpp_core/include/`
   - Recompile: `g++ -std=c++11 -I./include src/*.cpp main.cpp -o prodly_core.exe`

2. **Make changes to Java code:**
   - Edit files in `java_gui/src/`
   - In NetBeans: Save → Build (F11)
   - Or recompile manually

3. **Test changes:**
   - Run the application
   - Test the specific feature you modified

## Notes

- The C++ executable must be in `cpp_core/` directory
- The Java application must be run from `java_gui/` directory (or with correct working directory)
- IPC files (`input.json`, `output.json`) are created automatically
- On first run, you may need to create an account or use default admin credentials

## Alternative: Using CMake (Advanced)

For a more professional build system, you can create a `CMakeLists.txt`:

```cmake
cmake_minimum_required(VERSION 3.10)
project(ProdlyCore)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

include_directories(include)

file(GLOB SOURCES "src/*.cpp" "main.cpp")

add_executable(prodly_core ${SOURCES})
```

Then build with:
```bash
cd cpp_core
mkdir build
cd build
cmake ..
cmake --build .
```

