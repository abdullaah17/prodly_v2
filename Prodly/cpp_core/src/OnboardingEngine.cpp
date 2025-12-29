#include "../include/OnboardingEngine.h"
#include <algorithm>
#include <unordered_set>

OnboardingEngine::OnboardingEngine(const string& role) : role(role) {
    tasks.clear();
    dependencyGraph.clear();
    nextTasksGraph.clear();
    allTasks.clear();
    
    // Role-based task initialization
    if (role == "employee") {
        // Employee onboarding path
        tasks["T1"] = Task("T1", "Company Introduction", "Learn about company culture and values", "Orientation", 2);
        tasks["T2"] = Task("T2", "Tool Setup", "Install and configure development tools", "Technical", 4);
        tasks["T3"] = Task("T3", "First Assignment", "Complete your first coding task", "Technical", 8);
        tasks["T4"] = Task("T4", "Code Review Process", "Learn how to submit and review code", "Process", 3);
        tasks["T5"] = Task("T5", "Team Introduction", "Meet your team members", "Social", 1);
        
        // Dependency graph (Level-2 DSA)
        dependencyGraph["T2"] = {"T1"};
        dependencyGraph["T3"] = {"T2"};
        dependencyGraph["T4"] = {"T3"};
        dependencyGraph["T5"] = {"T1"};
        
        nextTasksGraph["T1"] = {"T2", "T5"};
        nextTasksGraph["T2"] = {"T3"};
        nextTasksGraph["T3"] = {"T4"};
        
        // Level-1 DSA: Array initialization
        allTasks = {tasks["T1"], tasks["T2"], tasks["T3"], tasks["T4"], tasks["T5"]};
        
        // Level-1 DSA: Queue initialization
        available.push("T1");
        
    } else if (role == "manager") {
        // Manager onboarding path
        tasks["M1"] = Task("M1", "Management Fundamentals", "Learn management principles", "Leadership", 6);
        tasks["M2"] = Task("M2", "Team Dashboard Access", "Access team analytics dashboard", "Technical", 2);
        tasks["M3"] = Task("M3", "Performance Review Process", "Learn how to conduct reviews", "Process", 4);
        tasks["M4"] = Task("M4", "Resource Planning", "Learn resource allocation", "Planning", 5);
        
        dependencyGraph["M2"] = {"M1"};
        dependencyGraph["M3"] = {"M2"};
        dependencyGraph["M4"] = {"M3"};
        
        nextTasksGraph["M1"] = {"M2"};
        nextTasksGraph["M2"] = {"M3"};
        nextTasksGraph["M3"] = {"M4"};
        
        allTasks = {tasks["M1"], tasks["M2"], tasks["M3"], tasks["M4"]};
        available.push("M1");
        
    } else if (role == "admin") {
        // Admin onboarding path
        tasks["A1"] = Task("A1", "System Administration", "Learn system administration", "Technical", 8);
        tasks["A2"] = Task("A2", "User Management", "Manage users and roles", "Administration", 4);
        tasks["A3"] = Task("A3", "Security Protocols", "Learn security best practices", "Security", 6);
        
        dependencyGraph["A2"] = {"A1"};
        dependencyGraph["A3"] = {"A2"};
        
        nextTasksGraph["A1"] = {"A2"};
        nextTasksGraph["A2"] = {"A3"};
        
        allTasks = {tasks["A1"], tasks["A2"], tasks["A3"]};
        available.push("A1");
    }
}

void OnboardingEngine::completeTask(const string& taskId) {
    if (tasks.find(taskId) == tasks.end()) return;
    
    tasks[taskId].completed = true;
    
    // Unlock next tasks (Graph traversal - Level-2 DSA)
    if (nextTasksGraph.find(taskId) != nextTasksGraph.end()) {
        for (const string& nextId : nextTasksGraph[taskId]) {
            if (canStartTask(nextId)) {
                available.push(nextId); // Queue operation (Level-1 DSA)
            }
        }
    }
}

vector<Task> OnboardingEngine::getAvailableTasks() {
    vector<Task> result;
    queue<string> temp = available;
    unordered_set<string> seen;
    
    // Process queue (Level-1 DSA)
    while (!temp.empty()) {
        string id = temp.front();
        temp.pop();
        
        if (seen.find(id) != seen.end()) continue;
        seen.insert(id);
        
        if (tasks.find(id) != tasks.end() && !tasks[id].completed && canStartTask(id)) {
            result.push_back(tasks[id]);
        }
    }
    
    return result;
}

vector<Task> OnboardingEngine::getAllTasks() {
    return allTasks; // Array return (Level-1 DSA)
}

int OnboardingEngine::getCompletionPercent() {
    if (allTasks.empty()) return 0;
    
    int completed = 0;
    for (const Task& task : allTasks) {
        if (tasks.find(task.id) != tasks.end() && tasks[task.id].completed) {
            completed++;
        }
    }
    
    return (completed * 100) / allTasks.size();
}

int OnboardingEngine::getEstimatedDaysRemaining() {
    int totalHours = 0;
    for (const Task& task : allTasks) {
        if (tasks.find(task.id) != tasks.end() && !tasks[task.id].completed) {
            totalHours += tasks[task.id].estimatedHours;
        }
    }
    return (totalHours + 7) / 8; // Assuming 8 hours per day
}

string OnboardingEngine::getCurrentStage() {
    int percent = getCompletionPercent();
    if (percent < 25) return "Getting Started";
    if (percent < 50) return "Learning Basics";
    if (percent < 75) return "Building Skills";
    if (percent < 100) return "Almost There";
    return "Completed";
}

bool OnboardingEngine::canStartTask(const string& taskId) {
    // Check prerequisites using graph (Level-2 DSA)
    if (dependencyGraph.find(taskId) == dependencyGraph.end()) {
        return true; // No prerequisites
    }
    
    for (const string& prereq : dependencyGraph[taskId]) {
        if (tasks.find(prereq) == tasks.end() || !tasks[prereq].completed) {
            return false; // Prerequisite not met
        }
    }
    
    return true;
}
