# PRODLY - Role-Based Onboarding Engine

A unified desktop SaaS-style application that delivers personalized onboarding paths, task tracking, progress analytics, and smart recommendations based on role and performance.

## 🏗️ Architecture Overview

Prodly is built as **ONE unified system** using **two separate codebases**:

```
prodly/
├── cpp_core/        (C++ – Core logic, DSAs, engines)
└── java_gui/        (Java Swing – UI, validation, visualization)
```

### Integration Flow

The system uses **file-based IPC (Inter-Process Communication)** for seamless integration:

```
Java GUI → writes input.json
C++ Core → reads input.json
C++ Core → processes DSA logic
C++ Core → writes output.json
Java GUI → reads output.json & visualizes results
```

## 🧮 Data Structures & Algorithms (DSAs)

Each engine implements both Level-1 and Level-2 DSAs as required:

### 1. Onboarding Engine
- **Level-1 DSA**: Arrays (role-based task sets), Queues (FIFO task availability)
- **Level-2 DSA**: Graph (task dependency flow)
- **Purpose**: Manages role-based onboarding paths with task dependencies
- **Operations**: Task completion, dependency unlocking, progress tracking
- **Time Complexity**: 
  - Task completion: O(1) average
  - Get available tasks: O(V) where V = unlocked tasks
  - Dependency check: O(E) where E = edges from task

### 2. Evaluation Engine
- **Level-1 DSA**: Hash Table (skill-score mapping)
- **Level-2 DSA**: Heap/Priority Queue (top skills ranking)
- **Purpose**: Evaluates skill scores and identifies top-performing skills
- **Operations**: Score evaluation, top-k skills extraction
- **Time Complexity**:
  - Evaluation: O(n) where n = number of skills
  - Top-k extraction: O(k log n) using heap

### 3. Leveling Engine
- **Level-1 DSA**: Sorting (score ordering)
- **Level-2 DSA**: Priority Queue (batch level assignment)
- **Purpose**: Assigns skill levels based on evaluation scores
- **Operations**: Level assignment, batch processing
- **Time Complexity**:
  - Single level assignment: O(n log n) for sorting
  - Batch assignment: O(n log n) using priority queue

### 4. Manager Engine
- **Level-1 DSA**: Linked List (employee chain traversal)
- **Level-2 DSA**: BFS/DFS (team hierarchy, blocked employee detection)
- **Purpose**: Provides team analytics and identifies at-risk employees
- **Operations**: Team stats, hierarchy traversal, risk detection
- **Time Complexity**:
  - Risk detection: O(n) linked list traversal
  - Hierarchy: O(V + E) BFS
  - Blocked detection: O(V + E) DFS

### 5. Upskill Engine
- **Level-1 DSA**: Stack (learning path tracking)
- **Level-2 DSA**: Trie (skill search) + Recommendation Graph (personalized paths)
- **Purpose**: Generates personalized learning recommendations
- **Operations**: Skill search, recommendation generation
- **Time Complexity**:
  - Trie search: O(m) where m = prefix length
  - Recommendations: O(V + E) graph traversal

## 📁 Project Structure

```
prodly/
│
├── cpp_core/
│   ├── include/
│   │   ├── AuthEngine.h
│   │   ├── OnboardingEngine.h
│   │   ├── EvaluationEngine.h
│   │   ├── LevelingEngine.h
│   │   ├── ManagerEngine.h
│   │   ├── UpskillEngine.h
│   │   ├── RoleGateEngine.h
│   │   └── AuditEngine.h
│   ├── src/
│   │   ├── AuthEngine.cpp
│   │   ├── OnboardingEngine.cpp
│   │   ├── EvaluationEngine.cpp
│   │   ├── LevelingEngine.cpp
│   │   ├── ManagerEngine.cpp
│   │   ├── UpskillEngine.cpp
│   │   ├── RoleGateEngine.cpp
│   │   └── AuditEngine.cpp
│   ├── main.cpp
│   └── input.json / output.json (IPC files)
│
├── java_gui/
│   └── src/
│       └── prodly/
│           ├── Main.java
│           ├── rolegate/
│           │   └── LoginScreen.java
│           ├── dashboard/
│           │   ├── EmployeeDashboard.java
│           │   ├── ManagerDashboard.java
│           │   └── AdminDashboard.java
│           ├── onboarding/
│           │   ├── OnboardingScreen.java
│           │   ├── OnboardingController.java
│           │   └── TaskBoardUI.java
│           ├── evaluation/
│           │   ├── AssessmentUI.java
│           │   ├── EvaluationController.java
│           │   └── EvaluationValidator.java
│           ├── leveling/
│           │   ├── LevelView.java
│           │   ├── LevelController.java
│           │   └── LevelSummaryUI.java
│           ├── manager/
│           │   ├── ManagerDashboardUI.java
│           │   └── RiskController.java
│           ├── upskilling/
│           │   ├── UpskillUI.java
│           │   ├── ReEvaluationUI.java
│           │   └── ReEvaluationController.java
│           ├── audit/
│           │   ├── AuditLogUI.java
│           │   └── AuditController.java
│           └── integration/
│               ├── InputWriter.java
│               ├── OutputReader.java
│               └── CppRunner.java
│
├── README.md
└── .gitignore
```

## 🚀 Getting Started

### Prerequisites

- **C++ Compiler**: GCC/Clang (C++11 or later)
- **Java JDK**: Version 8 or later
- **Build Tools**: 
  - For C++: Make or CMake
  - For Java: NetBeans or command-line javac

### Building the C++ Core

```bash
cd cpp_core
g++ -std=c++11 -I./include src/*.cpp main.cpp -o prodly_core.exe
# On Linux/Mac: -o prodly_core
```

### Building the Java GUI

**Using NetBeans:**
1. Open NetBeans
2. File → Open Project → Select `java_gui` folder
3. Build Project (F11)

**Using Command Line:**
```bash
cd java_gui/src
javac -d ../build prodly/**/*.java
# Note: Requires JSON library (json-simple) in classpath
```

### Running the Application

1. **Start from Java GUI:**
   ```bash
   cd java_gui
   java -cp "build:lib/*" prodly.Main
   ```

2. **The Login Screen will appear:**
   - Default admin: `admin` / `admin123`
   - Or create a new account with any role

## 🎯 Features

### 1. Authentication & Role Gate
- **Login/Signup** with role-based access
- **Roles**: Employee, Manager, Admin
- **Session handling** within desktop app

### 2. Role-Based Onboarding Engine
- **Different paths** per role (Employee, Manager, Admin)
- **Task dependencies** using graph structure
- **Sequential progression** logic
- **Progress tracking** with completion percentage

### 3. Employee Dashboard
- Current onboarding stage
- Assigned tasks with descriptions
- Learning progress visualization
- Completion percentage and estimated completion date
- Task completion interface

### 4. Manager Overview Dashboard
- View all team members
- Track onboarding progress
- Detect blocked or slow employees
- Productivity visibility without micromanagement
- Team statistics (average level, progress, at-risk count)

### 5. Task & Learning Module System
- Skill-linked tasks
- Role-specific learning paths
- Completion tracking
- Dependency handling

### 6. Progress Tracking Engine
- Dynamic progress calculation
- Visual indicators (progress bars, labels)
- Auto-updates on task completion

### 7. Smart Recommendations (Rule-Based)
- "What to learn next" suggestions
- "Pending critical tasks" identification
- Role + progress based recommendations
- AI-upgrade ready architecture

### 8. Professional Desktop UI
- **Java Swing** with modern design
- Multi-screen navigation
- Responsive layouts
- Dashboard-style interface
- Modular UI files
- Clear separation of controllers & views

## 🔄 Integration Flow Details

### Example: Login Flow

1. **Java GUI** creates JSON request:
   ```json
   {
     "action": "login",
     "username": "john",
     "password": "pass123"
   }
   ```

2. **InputWriter** writes to `input.json`

3. **CppRunner** executes `prodly_core.exe`

4. **C++ main.cpp** reads `input.json`, processes via `AuthEngine`

5. **C++** writes response to `output.json`:
   ```json
   {
     "status": "success",
     "role": "employee",
     "username": "john"
   }
   ```

6. **OutputReader** reads `output.json`

7. **Java GUI** parses response and navigates to appropriate dashboard

### Example: Get Onboarding Tasks

1. **Java** sends: `{"action": "get_onboarding", "role": "employee", "username": "john"}`
2. **C++ OnboardingEngine** processes:
   - Initializes role-based tasks
   - Builds dependency graph
   - Returns available tasks
3. **Java** displays tasks in table with progress bar

## 📊 Screens Overview

1. **Login Screen**: Authentication with role selection
2. **Employee Dashboard**: Task list, progress, navigation to modules
3. **Manager Dashboard**: Team overview, statistics, analytics
4. **Admin Dashboard**: System administration, audit logs
5. **Onboarding Screen**: Detailed task view with dependencies
6. **Evaluation Screen**: Skill assessment interface
7. **Level View**: Current level display with progression
8. **Upskill UI**: Personalized recommendations
9. **Audit Log UI**: System activity logs

## 🛠️ Development Notes

### C++ Development (VS Code)
- All core logic in `cpp_core/`
- Each engine is a separate module
- JSON parsing is simplified (production would use a library)

### Java Development (NetBeans)
- UI components in `java_gui/src/prodly/`
- Integration layer handles C++ communication
- Professional Swing UI with modern styling

### Adding New Actions

1. **C++**: Add handler in `main.cpp`
2. **C++**: Implement logic in appropriate engine
3. **Java**: Add UI component if needed
4. **Java**: Update integration layer to call new action

## 📝 Dependencies

### C++
- Standard C++11 library
- No external dependencies

### Java
- `json-simple` library for JSON parsing
- Java Swing (included in JDK)

## 🎓 Educational Value

This project demonstrates:
- **Multi-language system integration**
- **File-based IPC**
- **DSA implementation** (Arrays, Queues, Graphs, Hash Tables, Heaps, Sorting, Priority Queues, Linked Lists, BFS/DFS, Stacks, Trie)
- **Object-oriented design**
- **Desktop application architecture**
- **Role-based access control**
- **Progress tracking systems**

## 📄 License

This project is built for educational and portfolio purposes.

## 👥 Authors

Built as a comprehensive desktop SaaS application demonstrating advanced software engineering principles.

---

**Note**: This is a production-quality engineering build suitable for university final projects, portfolio showcases, and system design demonstrations.
