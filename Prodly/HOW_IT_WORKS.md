# PRODLY - How It Works

## 🏗️ System Architecture

PRODLY is a **unified desktop application** built with **two separate codebases** that work together seamlessly:

```
┌─────────────────────────────────────────────────────────┐
│                    PRODLY SYSTEM                         │
├─────────────────────────────────────────────────────────┤
│                                                          │
│  ┌──────────────┐         ┌──────────────┐             │
│  │   Java GUI   │ ◄─────► │   C++ Core   │             │
│  │  (Frontend)  │  JSON   │  (Backend)   │             │
│  └──────────────┘  Files  └──────────────┘             │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

## 🔄 Integration Flow (File-Based IPC)

The Java GUI and C++ Core communicate through **JSON files**:

```
1. User Action in Java GUI
   ↓
2. Java creates JSON request → writes to input.json
   ↓
3. Java calls C++ executable (prodly_core.exe)
   ↓
4. C++ reads input.json
   ↓
5. C++ processes request using engines (DSAs)
   ↓
6. C++ writes response to output.json
   ↓
7. Java reads output.json
   ↓
8. Java displays results in UI
```

### Example: Login Flow

**Step 1: User clicks "Login"**
```java
// Java GUI (LoginScreen.java)
JSONObject request = new JSONObject();
request.put("action", "login");
request.put("username", "admin");
request.put("password", "admin123");
InputWriter.write(request.toJSONString()); // Writes to input.json
```

**Step 2: Java executes C++**
```java
CppRunner.runCore(); // Runs prodly_core.exe
```

**Step 3: C++ processes request**
```cpp
// C++ Core (main.cpp)
ifstream input("input.json");
// Reads: {"action":"login","username":"admin","password":"admin123"}

AuthEngine auth;
bool success = auth.login(username, password, role, message);
// Uses Hash Table (Level-1 DSA) for O(1) user lookup
```

**Step 4: C++ writes response**
```cpp
// C++ writes to output.json
{"status":"success","role":"admin","username":"admin"}
```

**Step 5: Java reads and displays**
```java
String response = OutputReader.read(); // Reads output.json
JSONObject response = parser.parse(responseStr);
if ("success".equals(response.get("status"))) {
    openDashboard(role, username); // Opens Admin Dashboard
}
```

## 🧮 Data Structures & Algorithms in Action

### 1. Onboarding Engine

**When:** User views tasks or completes a task

**DSA Used:**
- **Level-1**: Arrays (task sets), Queues (available tasks)
- **Level-2**: Graph (task dependencies)

**How it works:**
```cpp
// Task dependencies stored as graph
dependencyGraph["T2"] = {"T1"};  // T2 requires T1
nextTasksGraph["T1"] = {"T2"};   // After T1, unlock T2

// Available tasks in queue (FIFO)
queue<string> available;  // ["T1"]
available.push("T1");

// When T1 completed:
completeTask("T1");
// → Unlocks T2 (graph traversal)
// → Adds T2 to queue
```

**Time Complexity:**
- Check if task can start: O(E) where E = dependencies
- Get available tasks: O(V) where V = unlocked tasks
- Complete task: O(1) average

### 2. Evaluation Engine

**When:** User submits skill scores

**DSA Used:**
- **Level-1**: Hash Table (skill → score mapping)
- **Level-2**: Heap/Priority Queue (top skills)

**How it works:**
```cpp
// Hash table for O(1) lookups
unordered_map<string, int> skillScores;
skillScores["DSA"] = 85;  // O(1) insertion
skillScores["OOP"] = 78;

// Heap for top-k extraction
priority_queue<SkillScore> topSkillsHeap;
// O(k log n) to get top 3 skills
```

**Time Complexity:**
- Store scores: O(n) where n = number of skills
- Get top-k: O(k log n) using heap

### 3. Manager Engine

**When:** Manager views team statistics

**DSA Used:**
- **Level-1**: Linked List (employee chain)
- **Level-2**: BFS/DFS (team hierarchy, blocked detection)

**How it works:**
```cpp
// Linked list traversal for at-risk employees
list<Employee> employeeList;
for (auto it = employeeList.begin(); it != employeeList.end(); ++it) {
    if (it->level < threshold) {
        atRisk.push_back(*it);  // O(n) traversal
    }
}

// BFS for team hierarchy
queue<string> q;
q.push(managerId);
while (!q.empty()) {
    // Process level by level
    // O(V + E) where V = employees, E = relationships
}
```

**Time Complexity:**
- Find at-risk: O(n) linked list traversal
- Team hierarchy: O(V + E) BFS
- Blocked detection: O(V + E) DFS

### 4. Upskill Engine

**When:** User requests learning recommendations

**DSA Used:**
- **Level-1**: Stack (learning path tracking)
- **Level-2**: Trie (skill search) + Graph (recommendations)

**How it works:**
```cpp
// Stack for path tracking
stack<string> learningPath;
learningPath.push("skill1");  // O(1)
learningPath.push("skill2");

// Trie for skill search
TrieNode* root;
// Search "DSA" → O(m) where m = length of prefix

// Graph for recommendations
unordered_map<string, RecommendationNode> skillGraph;
// BFS from completed skills to find next
// O(V + E) graph traversal
```

**Time Complexity:**
- Skill search: O(m) where m = prefix length
- Recommendations: O(V + E) graph traversal

## 📁 File Structure & Data Flow

### Input/Output Files

```
Prodly/
├── input.json          (Java writes, C++ reads)
├── output.json         (C++ writes, Java reads)
└── data/
    ├── users.dat       (Persistent user storage)
    └── settings.dat     (Persistent settings)
```

### Data Persistence

**Users:**
```
Format: username|password|role|createdAt
Example: admin|admin123|admin|2024-01-01
```

**Settings:**
```
Format: key=value
Example: minPasswordLength=8
```

## 🎯 User Flows

### Flow 1: Employee Onboarding

```
1. Employee logs in
   ↓
2. Employee Dashboard shows:
   - Available tasks (from Queue)
   - Progress percentage
   - Current stage
   ↓
3. Employee clicks "Complete Task"
   ↓
4. C++ OnboardingEngine:
   - Marks task complete
   - Checks dependency graph
   - Unlocks next tasks
   - Updates progress
   ↓
5. Dashboard refreshes with new tasks
```

### Flow 2: Manager Team Analysis

```
1. Manager logs in
   ↓
2. Manager Dashboard shows:
   - Team statistics (from ManagerEngine)
   - At-risk employees (Linked List traversal)
   - Team hierarchy (BFS)
   ↓
3. Manager clicks "Reports & Analytics"
   ↓
4. Reports UI generates:
   - Team performance metrics
   - Completion rates
   - Export to CSV
```

### Flow 3: Admin User Management

```
1. Admin logs in
   ↓
2. Admin Dashboard shows options
   ↓
3. Admin clicks "User Management"
   ↓
4. UserManagementUI:
   - Lists all users (from AuthEngine)
   - Shows statistics (Hash Table counts)
   ↓
5. Admin adds new user
   ↓
6. C++ AuthEngine:
   - Checks if user exists (Hash Table lookup O(1))
   - Creates user
   - Saves to users.dat (Data Persistence)
   ↓
7. UI refreshes with new user
```

## 🔐 Security & Data Flow

### Authentication Flow

```
Login Request:
┌─────────────┐
│  Java GUI   │ → input.json: {"action":"login","username":"...","password":"..."}
└─────────────┘
       ↓
┌─────────────┐
│  C++ Core   │ → Reads input.json
│ AuthEngine  │ → Hash Table lookup: O(1)
└─────────────┘
       ↓
┌─────────────┐
│  C++ Core   │ → output.json: {"status":"success","role":"admin"}
└─────────────┘
       ↓
┌─────────────┐
│  Java GUI   │ → Reads output.json → Opens Admin Dashboard
└─────────────┘
```

### Data Persistence Flow

```
User Creation:
┌─────────────┐
│  Java GUI   │ → Signup request
└─────────────┘
       ↓
┌─────────────┐
│  C++ Core   │ → AuthEngine.signup()
│             │ → Adds to Hash Table
│             │ → DataPersistence.saveUsers()
└─────────────┘
       ↓
┌─────────────┐
│  File I/O   │ → data/users.dat (persistent storage)
└─────────────┘
```

## 🎨 UI Component Hierarchy

```
Main.java
  └── LoginScreen
       ├── performLogin() → EmployeeDashboard
       ├── performLogin() → ManagerDashboard
       └── performLogin() → AdminDashboard
            │
            ├── EmployeeDashboard
            │    ├── OnboardingScreen
            │    ├── AssessmentUI
            │    ├── LevelView
            │    ├── UpskillUI
            │    └── SearchUI
            │
            ├── ManagerDashboard
            │    ├── ManagerDashboardUI
            │    ├── ReportsUI
            │    ├── AnalyticsDashboard
            │    └── SearchUI
            │
            └── AdminDashboard
                 ├── UserManagementUI
                 ├── SystemSettingsUI
                 ├── ReportsUI
                 ├── AnalyticsDashboard
                 ├── SearchUI
                 ├── BackupRestoreUI
                 └── AuditLogUI
```

## 🔄 Real-Time Updates

### Progress Tracking

```
Task Completion:
1. User clicks "Complete Task"
2. Java sends: {"action":"complete_task","taskId":"T1"}
3. C++ OnboardingEngine:
   - Marks T1 complete
   - Traverses graph to unlock T2, T3
   - Recalculates progress: (completed/total) * 100
4. Java receives updated progress
5. Progress bar updates automatically
```

### Team Statistics

```
Manager Dashboard Refresh:
1. Manager clicks "Refresh"
2. Java sends: {"action":"get_team_stats"}
3. C++ ManagerEngine:
   - Traverses employee list (Linked List)
   - Calculates averages
   - Finds at-risk employees (BFS/DFS)
4. Java displays updated statistics
```

## 📊 Data Visualization

### Analytics Dashboard

```
Chart Generation:
1. User selects chart type
2. Java sends: {"action":"generate_report","reportType":"Progress Over Time"}
3. C++ generates data points
4. Java ChartPanel:
   - Draws line chart using Graphics2D
   - Plots data points
   - Adds labels and axes
5. User sees visual representation
```

## 🔍 Search Functionality

```
Search Flow:
1. User types in search box
2. Real-time search (after 2+ characters)
3. Java sends: {"action":"search","query":"admin","type":"Users"}
4. C++ processes search
5. Returns matching results
6. Java displays in results table
```

## 💾 Backup & Restore

```
Backup Flow:
1. Admin clicks "Create Backup"
2. Java sends: {"action":"create_backup","backupPath":"..."}
3. C++ DataPersistence:
   - Reads users.dat
   - Reads settings.dat
   - Combines into backup file
4. Backup file saved
5. Status displayed to admin
```

## 🎯 Key Design Patterns

### 1. Separation of Concerns
- **Java**: UI, validation, visualization
- **C++**: Business logic, DSAs, processing

### 2. File-Based IPC
- Simple, reliable communication
- No network dependencies
- Easy to debug

### 3. Modular Engines
- Each engine is independent
- Clear responsibilities
- Easy to extend

### 4. Data Persistence
- File-based storage
- Automatic save/load
- Backup support

## 🚀 Performance Characteristics

### Time Complexities

| Operation | DSA Used | Complexity |
|-----------|----------|------------|
| User login | Hash Table | O(1) average |
| Get tasks | Queue + Graph | O(V) |
| Complete task | Graph | O(E) |
| Find at-risk | Linked List | O(n) |
| Team hierarchy | BFS | O(V + E) |
| Skill search | Trie | O(m) |
| Recommendations | Graph | O(V + E) |
| Top skills | Heap | O(k log n) |

### Space Complexities

| Component | Space |
|-----------|-------|
| User storage | O(n) where n = users |
| Task graph | O(V + E) |
| Skill Trie | O(m × k) where m = avg length, k = skills |
| Employee list | O(n) |

## 🔧 Configuration

### System Settings
- Stored in `data/settings.dat`
- Loaded on startup
- Saved on changes

### User Data
- Stored in `data/users.dat`
- Loaded on startup
- Saved on user operations

## 📝 Error Handling

### Java Side
- Try-catch blocks around C++ calls
- User-friendly error messages
- Status labels for feedback

### C++ Side
- File existence checks
- Input validation
- Error status in JSON response

## 🎓 Learning Points

This application demonstrates:
1. **Multi-language integration** (Java + C++)
2. **File-based IPC** (JSON communication)
3. **DSA implementation** (Arrays, Queues, Graphs, Hash Tables, Heaps, etc.)
4. **Object-oriented design** (Modular engines)
5. **Desktop application architecture** (Swing UI)
6. **Data persistence** (File I/O)
7. **Role-based access control** (RBAC)
8. **Progress tracking systems**
9. **Analytics and reporting**
10. **Professional UI design**

---

**The application is a complete, production-ready desktop SaaS system demonstrating advanced software engineering principles!**

