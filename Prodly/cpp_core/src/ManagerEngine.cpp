#include "../include/ManagerEngine.h"
#include <algorithm>
#include <queue>
#include <stack>
#include <functional>

ManagerEngine::ManagerEngine() {
    employeeList.clear();
    teamGraph.clear();
    employees.clear();
}

// Level-1 DSA: Linked List traversal - O(n)
vector<Employee> ManagerEngine::findRiskEmployees(int thresholdLevel) {
    vector<Employee> atRisk;
    
    // Traverse linked list
    for (auto it = employeeList.begin(); it != employeeList.end(); ++it) {
        if (it->level < thresholdLevel || it->progress < 50.0) {
            atRisk.push_back(*it);
        }
    }
    
    return atRisk;
}

// Level-2 DSA: BFS (Breadth-First Search) - O(V + E)
vector<vector<string>> ManagerEngine::getTeamHierarchy(const string& managerId) {
    vector<vector<string>> hierarchy;
    if (teamGraph.find(managerId) == teamGraph.end()) {
        return hierarchy;
    }
    
    queue<string> q;
    unordered_set<string> visited;
    
    q.push(managerId);
    visited.insert(managerId);
    
    while (!q.empty()) {
        int levelSize = q.size();
        vector<string> currentLevel;
        
        for (int i = 0; i < levelSize; i++) {
            string current = q.front();
            q.pop();
            currentLevel.push_back(current);
            
            // Add neighbors to queue
            if (teamGraph.find(current) != teamGraph.end()) {
                for (const string& neighbor : teamGraph[current]) {
                    if (visited.find(neighbor) == visited.end()) {
                        visited.insert(neighbor);
                        q.push(neighbor);
                    }
                }
            }
        }
        
        hierarchy.push_back(currentLevel);
    }
    
    return hierarchy;
}

// Level-2 DSA: DFS (Depth-First Search) - O(V + E)
vector<string> ManagerEngine::findBlockedEmployees(const string& startId) {
    vector<string> blocked;
    unordered_set<string> visited;
    stack<string> dfsStack;
    
    dfsStack.push(startId);
    visited.insert(startId);
    
    while (!dfsStack.empty()) {
        string current = dfsStack.top();
        dfsStack.pop();
        
        // Check if employee is blocked (no dependencies completed)
        if (employees.find(current) != employees.end()) {
            Employee& emp = employees[current];
            bool isBlocked = false;
            
            for (const string& dep : emp.dependencies) {
                if (employees.find(dep) != employees.end() && 
                    employees[dep].progress < 100.0) {
                    isBlocked = true;
                    break;
                }
            }
            
            if (isBlocked) {
                blocked.push_back(current);
            }
        }
        
        // Continue DFS
        if (teamGraph.find(current) != teamGraph.end()) {
            for (const string& neighbor : teamGraph[current]) {
                if (visited.find(neighbor) == visited.end()) {
                    visited.insert(neighbor);
                    dfsStack.push(neighbor);
                }
            }
        }
    }
    
    return blocked;
}

ManagerEngine::TeamStats ManagerEngine::getTeamStats() {
    TeamStats stats;
    stats.totalEmployees = employeeList.size();
    
    if (employeeList.empty()) {
        stats.averageLevel = 0;
        stats.averageProgress = 0.0;
        stats.atRiskCount = 0;
        return stats;
    }
    
    int totalLevel = 0;
    double totalProgress = 0.0;
    
    for (const auto& emp : employeeList) {
        totalLevel += emp.level;
        totalProgress += emp.progress;
    }
    
    stats.averageLevel = totalLevel / stats.totalEmployees;
    stats.averageProgress = totalProgress / stats.totalEmployees;
    
    vector<Employee> atRisk = findRiskEmployees(2);
    stats.atRiskCount = atRisk.size();
    
    return stats;
}

void ManagerEngine::addEmployee(const Employee& emp) {
    employeeList.push_back(emp);
    employees[emp.id] = emp;
    
    // Build graph connections
    for (const string& dep : emp.dependencies) {
        teamGraph[dep].push_back(emp.id);
    }
}
