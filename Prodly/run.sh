#!/bin/bash

echo "========================================"
echo "PRODLY - Role-Based Onboarding Engine"
echo "========================================"
echo ""

# Check if C++ executable exists
if [ ! -f "cpp_core/prodly_core" ]; then
    echo "C++ executable not found!"
    echo ""
    echo "Please compile C++ core first:"
    echo "  1. Run: ./compile_cpp.sh"
    echo "  2. Or manually: cd cpp_core && g++ -std=c++11 -I./include src/*.cpp main.cpp -o prodly_core"
    echo ""
    exit 1
fi

# Check if JSON library exists
if [ ! -f "java_gui/lib/json-simple-1.1.1.jar" ]; then
    echo "JSON library not found!"
    echo ""
    echo "Please download json-simple-1.1.1.jar and place it in:"
    echo "  java_gui/lib/json-simple-1.1.1.jar"
    echo ""
    echo "Download from: https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple/1.1.1"
    echo ""
    exit 1
fi

echo "Starting Java GUI..."
echo ""

cd java_gui

# Try to run from build/classes if it exists, otherwise try to compile first
if [ -f "build/classes/prodly/Main.class" ]; then
    java -cp "build/classes:lib/json-simple-1.1.1.jar" prodly.Main
else
    echo "Java classes not found. Attempting to compile..."
    echo ""
    mkdir -p build/classes
    cd src
    javac -cp "../lib/json-simple-1.1.1.jar" -d ../build/classes prodly/**/*.java
    if [ $? -ne 0 ]; then
        echo ""
        echo "Compilation failed. Please compile manually or use NetBeans."
        exit 1
    fi
    cd ..
    java -cp "build/classes:lib/json-simple-1.1.1.jar" prodly.Main
fi

cd ..

