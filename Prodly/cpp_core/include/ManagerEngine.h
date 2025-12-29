#ifndef MANAGER_ENGINE_H
#define MANAGER_ENGINE_H

#include <vector>
#include <string>
#include <list>
#include <queue>
#include <unordered_map>
#include <unordered_set>

using namespace std;

struct Employee {
    string id;
    string name;
    int level;
    double progress;
    vector<string> dependencies; // For graph traversal
};

class ManagerEngine {
private:
    // Level-1 DSA: Linked List for employee chain
    list<Employee> employeeList;
    
    // Level-2 DSA: Graph for team structure (BFS/DFS)
    unordered_map<string, vector<string>> teamGraph;
    unordered_map<string, Employee> employees;

public:
    ManagerEngine();
    
    // Find at-risk employees using Linked List traversal (Level-1)
    vector<Employee> findRiskEmployees(int thresholdLevel);
    
    // Get team hierarchy using BFS (Level-2)
    vector<vector<string>> getTeamHierarchy(const string& managerId);
    
    // Find blocked employees using DFS (Level-2)
    vector<string> findBlockedEmployees(const string& startId);
    
    // Get team statistics
    struct TeamStats {
        int totalEmployees;
        int averageLevel;
        double averageProgress;
        int atRiskCount;
    };
    TeamStats getTeamStats();
    
    // Add employee
    void addEmployee(const Employee& emp);
};

#endif
