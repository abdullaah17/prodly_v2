@echo off
echo ========================================
echo PRODLY - Role-Based Onboarding Engine
echo ========================================
echo.

REM Check if C++ executable exists
if not exist "cpp_core\prodly_core.exe" (
    echo C++ executable not found!
    echo.
    echo Please compile C++ core first:
    echo   1. Run compile_cpp.bat
    echo   2. Or manually: cd cpp_core ^&^& g++ -std=c++11 -I./include src/*.cpp main.cpp -o prodly_core.exe
    echo.
    pause
    exit /b 1
)

REM Check if JSON library exists
if not exist "java_gui\lib\json-simple-1.1.1.jar" (
    echo JSON library not found!
    echo.
    echo Please download json-simple-1.1.1.jar and place it in:
    echo   java_gui\lib\json-simple-1.1.1.jar
    echo.
    echo Download from: https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple/1.1.1
    echo.
    pause
    exit /b 1
)

echo Starting Java GUI...
echo.

cd java_gui

REM Try to run from build/classes if it exists, otherwise try to compile first
if exist "build\classes\prodly\Main.class" (
    java -cp "build/classes;lib/json-simple-1.1.1.jar" prodly.Main
) else (
    echo Java classes not found. Attempting to compile...
    echo.
    cd src
    javac -cp "../lib/json-simple-1.1.1.jar" -d ../build/classes prodly/**/*.java
    if errorlevel 1 (
        echo.
        echo Compilation failed. Please compile manually or use NetBeans.
        pause
        exit /b 1
    )
    cd ..
    java -cp "build/classes;lib/json-simple-1.1.1.jar" prodly.Main
)

cd ..
pause

