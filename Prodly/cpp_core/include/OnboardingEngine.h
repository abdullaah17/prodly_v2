#ifndef ONBOARDING_ENGINE_H
#define ONBOARDING_ENGINE_H

#include <string>
#include <vector>
#include <unordered_map>
#include <queue>
#include <array>

using namespace std;

struct Task {
    string id;
    string name;
    string description;
    string skill;
    bool completed;
    int estimatedHours;
    
    Task() : completed(false), estimatedHours(0) {}
    Task(string i, string n, string d, string s, int h) 
        : id(i), name(n), description(d), skill(s), completed(false), estimatedHours(h) {}
};

class OnboardingEngine {
private:
    string role;
    
    // Level-1 DSA: Arrays for role-based task sets
    vector<Task> allTasks;
    
    // Level-1 DSA: Queue for available tasks (FIFO)
    queue<string> available;
    
    // Level-2 DSA: Graph for task dependencies
    unordered_map<string, vector<string>> dependencyGraph;  // task → prerequisites
    unordered_map<string, vector<string>> nextTasksGraph;   // task → next tasks
    unordered_map<string, Task> tasks;                      // taskId → Task

public:
    OnboardingEngine(const string& role);
    
    // Complete a task and unlock dependencies
    void completeTask(const string& taskId);
    
    // Get available tasks (no unmet prerequisites)
    vector<Task> getAvailableTasks();
    
    // Get all tasks for current role
    vector<Task> getAllTasks();
    
    // Get completion percentage
    int getCompletionPercent();
    
    // Get estimated completion date (days)
    int getEstimatedDaysRemaining();
    
    // Get current stage name
    string getCurrentStage();
    
    // Check if task can be started (all prerequisites met)
    bool canStartTask(const string& taskId);
};

#endif
