# PRODLY - Quick Start Guide

## 🚀 Fastest Way to Run

### Prerequisites Check
- ✅ C++ compiler installed (`g++` or `cl`)
- ✅ Java JDK 8+ installed (`java -version`)
- ✅ JSON library downloaded

### 1. Compile C++ (One Command)

**Windows:**
```bash
cd Prodly/cpp_core
g++ -std=c++11 -I./include src/*.cpp main.cpp -o prodly_core.exe
```

**Linux/Mac:**
```bash
cd Prodly/cpp_core
g++ -std=c++11 -I./include src/*.cpp main.cpp -o prodly_core
```

### 2. Set Up Java JSON Library

1. Download `json-simple-1.1.1.jar` from: https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple/1.1.1
2. Create folder: `Prodly/java_gui/lib/`
3. Place JAR file in that folder

### 3. Run Java Application

**Using NetBeans (Easiest):**
1. Open NetBeans
2. File → Open Project → Select `Prodly/java_gui`
3. Right-click project → Run (F6)

**Using Command Line:**
```bash
cd Prodly/java_gui
java -cp "build/classes;lib/json-simple-1.1.1.jar" prodly.Main
```
(Use `:` instead of `;` on Linux/Mac)

### 4. Login

- **Username:** `admin`
- **Password:** `admin123`

Or create a new account!

---

**Need more details?** See [COMPILE_AND_RUN.md](COMPILE_AND_RUN.md)

