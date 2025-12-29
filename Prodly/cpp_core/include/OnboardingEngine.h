#ifndef ONBOARDING_ENGINE_H
#define ONBOARDING_ENGINE_H

#include <string>
#include <vector>
#include <unordered_map>
#include <queue>

using namespace std;

struct Task {
    string id;
    string name;
    bool completed;
};

class OnboardingEngine {
private:
    unordered_map<string, vector<string>> graph;   // task → next tasks
    unordered_map<string, Task> tasks;              // taskId → Task
    queue<string> available;                        // unlocked tasks

public:
    OnboardingEngine(const string& role);

    void completeTask(const string& taskId);
    vector<Task> getAvailableTasks();
    int getCompletionPercent();
};

#endif
