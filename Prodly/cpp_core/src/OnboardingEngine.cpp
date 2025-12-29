#include "../include/OnboardingEngine.h"

OnboardingEngine::OnboardingEngine(const string& role) {

    // Example onboarding for EMPLOYEE
    if (role == "employee") {
        tasks["T1"] = {"T1", "Company Intro", false};
        tasks["T2"] = {"T2", "Tool Setup", false};
        tasks["T3"] = {"T3", "First Task", false};

        graph["T1"].push_back("T2");
        graph["T2"].push_back("T3");

        available.push("T1");
    }
}

void OnboardingEngine::completeTask(const string& taskId) {
    tasks[taskId].completed = true;

    for (auto& next : graph[taskId]) {
        available.push(next);
    }
}

vector<Task> OnboardingEngine::getAvailableTasks() {
    vector<Task> result;
    queue<string> temp = available;

    while (!temp.empty()) {
        string id = temp.front();
        temp.pop();
        if (!tasks[id].completed)
            result.push_back(tasks[id]);
    }
    return result;
}

int OnboardingEngine::getCompletionPercent() {
    int done = 0;
    for (auto& t : tasks) {
        if (t.second.completed) done++;
    }
    return (done * 100) / tasks.size();
}
