#!/bin/bash

echo "========================================"
echo "Compiling PRODLY C++ Core"
echo "========================================"
echo ""

cd cpp_core

echo "Compiling with g++..."
g++ -std=c++11 -I./include src/*.cpp main.cpp -o prodly_core

if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Compilation failed!"
    echo ""
    echo "Troubleshooting:"
    echo "1. Make sure g++ is installed"
    echo "2. Check that all source files exist"
    echo "3. Verify include directory structure"
    exit 1
fi

echo ""
echo "========================================"
echo "SUCCESS: C++ Core compiled!"
echo "Executable: cpp_core/prodly_core"
echo "========================================"
echo ""

cd ..
chmod +x cpp_core/prodly_core

