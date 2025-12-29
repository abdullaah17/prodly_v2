@echo off
echo ========================================
echo Compiling PRODLY C++ Core
echo ========================================
echo.

cd cpp_core

echo Compiling with g++...
g++ -std=c++11 -I./include src/*.cpp main.cpp -o prodly_core.exe

if errorlevel 1 (
    echo.
    echo ERROR: Compilation failed!
    echo.
    echo Troubleshooting:
    echo 1. Make sure g++ is installed (MinGW-w64)
    echo 2. Check that all source files exist
    echo 3. Verify include directory structure
    pause
    exit /b 1
)

echo.
echo ========================================
echo SUCCESS: C++ Core compiled!
echo Executable: cpp_core\prodly_core.exe
echo ========================================
echo.

cd ..
pause

