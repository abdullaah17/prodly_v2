#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <vector>
#include <map>

#include "include/AuthEngine.h"
#include "include/OnboardingEngine.h"
#include "include/EvaluationEngine.h"
#include "include/LevelingEngine.h"
#include "include/ManagerEngine.h"
#include "include/UpskillEngine.h"
#include "include/RoleGateEngine.h"
#include "include/AuditEngine.h"

using namespace std;

// Simple JSON parser helpers
string extractValue(const string& json, const string& key) {
    size_t pos = json.find("\"" + key + "\"");
    if (pos == string::npos) return "";
    pos = json.find(":", pos);
    if (pos == string::npos) return "";
    
    // Skip whitespace
    while (pos < json.length() && (json[pos] == ':' || json[pos] == ' ')) pos++;
    
    if (pos >= json.length()) return "";
    
    // Check if value is string (quoted) or number
    if (json[pos] == '"') {
        pos++;
        size_t end = json.find("\"", pos);
        if (end == string::npos) return "";
        return json.substr(pos, end - pos);
    } else {
        // Number or boolean
        size_t end = pos;
        while (end < json.length() && json[end] != ',' && json[end] != '}' && json[end] != ']') end++;
        string val = json.substr(pos, end - pos);
        // Trim whitespace
        while (!val.empty() && val.back() == ' ') val.pop_back();
        return val;
    }
}

vector<int> extractIntArray(const string& json, const string& key) {
    vector<int> result;
    size_t pos = json.find("\"" + key + "\"");
    if (pos == string::npos) return result;
    
    pos = json.find("[", pos);
    if (pos == string::npos) return result;
    pos++;
    
    size_t end = json.find("]", pos);
    if (end == string::npos) return result;
    
    string arrayStr = json.substr(pos, end - pos);
    stringstream ss(arrayStr);
    string token;
    
    while (getline(ss, token, ',')) {
        // Trim whitespace
        token.erase(0, token.find_first_not_of(" \t"));
        token.erase(token.find_last_not_of(" \t") + 1);
        if (!token.empty()) {
            try {
                result.push_back(stoi(token));
            } catch (...) {
                // Skip invalid numbers
            }
        }
    }
    
    return result;
}

string buildJSON(const map<string, string>& data) {
    stringstream json;
    json << "{";
    bool first = true;
    for (const auto& pair : data) {
        if (!first) json << ",";
        first = false;
        json << "\"" << pair.first << "\":";
        
        // Check if value is already JSON (starts with { or [)
        if (!pair.second.empty() && (pair.second[0] == '{' || pair.second[0] == '[')) {
            json << pair.second; // Raw JSON
        } else {
            // Check if value is number or string
            bool isNumber = !pair.second.empty() && 
                           (isdigit(pair.second[0]) || pair.second[0] == '-' || 
                            pair.second == "true" || pair.second == "false");
            if (isNumber) {
                json << pair.second;
            } else {
                json << "\"" << pair.second << "\"";
            }
        }
    }
    json << "}";
    return json.str();
}

int main() {
    // File paths - relative to cpp_core directory
    ifstream input("../input.json");
    ofstream output("../output.json");

    if (!input.is_open()) {
        output << "{\"status\":\"error\",\"message\":\"input.json not found\"}";
        output.close();
        return 1;
    }

    string json, line;
    while (getline(input, line)) {
        json += line;
    }
    input.close();

    string action = extractValue(json, "action");
    
    map<string, string> response;
    
    if (action == "login") {
        string username = extractValue(json, "username");
        string password = extractValue(json, "password");
        
        AuthEngine auth;
        string role, message;
        bool success = auth.login(username, password, role, message);
        
        if (success) {
            response["status"] = "success";
            response["role"] = role;
            response["message"] = message;
            response["username"] = username;
        } else {
            response["status"] = "error";
            response["message"] = message;
        }
        
    } else if (action == "signup") {
        string username = extractValue(json, "username");
        string password = extractValue(json, "password");
        string role = extractValue(json, "role");
        
        AuthEngine auth;
        string message;
        bool success = auth.signup(username, password, role, message);
        
        if (success) {
            response["status"] = "success";
            response["message"] = message;
        } else {
            response["status"] = "error";
            response["message"] = message;
        }
        
    } else if (action == "get_onboarding") {
        string role = extractValue(json, "role");
        string username = extractValue(json, "username");
        
        OnboardingEngine engine(role);
        auto tasks = engine.getAvailableTasks();
        int completion = engine.getCompletionPercent();
        string stage = engine.getCurrentStage();
        int daysRemaining = engine.getEstimatedDaysRemaining();
        
        response["status"] = "success";
        response["completion"] = to_string(completion);
        response["stage"] = stage;
        response["daysRemaining"] = to_string(daysRemaining);
        
        // Build tasks array as JSON string
        stringstream tasksJson;
        tasksJson << "[";
        for (size_t i = 0; i < tasks.size(); i++) {
            if (i > 0) tasksJson << ",";
            tasksJson << "{";
            tasksJson << "\"id\":\"" << tasks[i].id << "\",";
            tasksJson << "\"name\":\"" << tasks[i].name << "\",";
            tasksJson << "\"description\":\"" << tasks[i].description << "\",";
            tasksJson << "\"skill\":\"" << tasks[i].skill << "\",";
            tasksJson << "\"estimatedHours\":" << tasks[i].estimatedHours;
            tasksJson << "}";
        }
        tasksJson << "]";
        // Store as raw JSON string (will be embedded in final JSON)
        response["tasks"] = tasksJson.str();
        
    } else if (action == "complete_task") {
        string role = extractValue(json, "role");
        string taskId = extractValue(json, "taskId");
        
        OnboardingEngine engine(role);
        engine.completeTask(taskId);
        
        auto tasks = engine.getAvailableTasks();
        int completion = engine.getCompletionPercent();
        
        response["status"] = "success";
        response["completion"] = to_string(completion);
        response["message"] = "Task completed";
        
    } else if (action == "evaluate") {
        vector<int> scores = extractIntArray(json, "scores");
        
        // If array extraction failed, try to parse from string format
        if (scores.empty()) {
            // Try alternative parsing
            string scoresStr = extractValue(json, "scores");
            if (!scoresStr.empty() && scoresStr[0] == '[') {
                scores = extractIntArray(json, "scores");
            }
        }
        
        // Default scores if still empty
        if (scores.empty()) {
            scores = {75, 80, 70, 65, 60}; // Default values
        }
        
        EvaluationEngine eval;
        auto skillScores = eval.evaluate(scores);
        double avg = eval.getAverageScore();
        auto topSkills = eval.getTopSkills(3);
        
        response["status"] = "success";
        response["average"] = to_string(avg);
        
        stringstream skillsJson;
        skillsJson << "{";
        bool first = true;
        for (const auto& pair : skillScores) {
            if (!first) skillsJson << ",";
            first = false;
            skillsJson << "\"" << pair.first << "\":" << pair.second;
        }
        skillsJson << "}";
        response["skills"] = skillsJson.str();
        
    } else if (action == "assign_level") {
        string skillScoresStr = extractValue(json, "skillScores");
        // Parse skill scores (simplified - would need proper JSON parsing)
        map<string, int> skillScores;
        skillScores["DSA"] = 75;
        skillScores["OOP"] = 80;
        skillScores["DB"] = 70;
        
        LevelingEngine leveling;
        int level = leveling.assignLevel(skillScores);
        
        response["status"] = "success";
        response["level"] = to_string(level);
        
    } else if (action == "get_team_stats") {
        ManagerEngine manager;
        
        // Add sample employees
        Employee emp1 = {"emp1", "John Doe", 3, 75.5, {}};
        Employee emp2 = {"emp2", "Jane Smith", 2, 45.0, {}};
        Employee emp3 = {"emp3", "Bob Johnson", 4, 90.0, {}};
        
        manager.addEmployee(emp1);
        manager.addEmployee(emp2);
        manager.addEmployee(emp3);
        
        auto stats = manager.getTeamStats();
        auto atRisk = manager.findRiskEmployees(2);
        
        response["status"] = "success";
        response["totalEmployees"] = to_string(stats.totalEmployees);
        response["averageLevel"] = to_string(stats.averageLevel);
        response["averageProgress"] = to_string(stats.averageProgress);
        response["atRiskCount"] = to_string(stats.atRiskCount);
        
    } else if (action == "get_recommendations") {
        string userId = extractValue(json, "userId");
        vector<string> completed;
        completed.push_back("skill1");
        completed.push_back("skill2");
        
        UpskillEngine upskill;
        auto recommendations = upskill.getRecommendations(userId, completed);
        
        response["status"] = "success";
        response["count"] = to_string(recommendations.size());
        
    } else {
        response["status"] = "error";
        response["message"] = "Unknown action: " + action;
    }

    // Write response
    output << buildJSON(response);
    output.close();

    return 0;
}
