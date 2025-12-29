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
#include "include/SystemSettings.h"
#include "include/DataPersistence.h"

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
    // File paths - try current directory first, then parent directory
    ifstream input("input.json");
    if (!input.is_open()) {
        input.open("../input.json");
    }
    
    ofstream output("output.json");
    if (!output.is_open()) {
        output.open("../output.json");
    }

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
        
    } else if (action == "list_users") {
        AuthEngine auth;
        auto users = auth.getAllUsers();
        
        response["status"] = "success";
        response["total"] = to_string(auth.getUserCount());
        response["employees"] = to_string(auth.getUserCountByRole("employee"));
        response["managers"] = to_string(auth.getUserCountByRole("manager"));
        response["admins"] = to_string(auth.getUserCountByRole("admin"));
        
        // Build users array
        stringstream usersJson;
        usersJson << "[";
        for (size_t i = 0; i < users.size(); i++) {
            if (i > 0) usersJson << ",";
            usersJson << "{";
            usersJson << "\"username\":\"" << users[i].first << "\",";
            usersJson << "\"role\":\"" << users[i].second.role << "\",";
            usersJson << "\"created\":\"" << users[i].second.createdAt << "\"";
            usersJson << "}";
        }
        usersJson << "]";
        response["users"] = usersJson.str();
        
    } else if (action == "delete_user") {
        string username = extractValue(json, "username");
        
        AuthEngine auth;
        string message;
        bool success = auth.deleteUser(username, message);
        
        if (success) {
            response["status"] = "success";
            response["message"] = message;
        } else {
            response["status"] = "error";
            response["message"] = message;
        }
        
    } else if (action == "update_user_role") {
        string username = extractValue(json, "username");
        string newRole = extractValue(json, "role");
        
        AuthEngine auth;
        string message;
        bool success = auth.updateUserRole(username, newRole, message);
        
        if (success) {
            response["status"] = "success";
            response["message"] = message;
        } else {
            response["status"] = "error";
            response["message"] = message;
        }
        
    } else if (action == "update_user_password") {
        string username = extractValue(json, "username");
        string newPassword = extractValue(json, "password");
        
        AuthEngine auth;
        string message;
        bool success = auth.updateUserPassword(username, newPassword, message);
        
        if (success) {
            response["status"] = "success";
            response["message"] = message;
        } else {
            response["status"] = "error";
            response["message"] = message;
        }
        
    } else if (action == "get_system_settings") {
        SystemSettings settings;
        
        response["status"] = "success";
        response["minPasswordLength"] = to_string(settings.getMinPasswordLength());
        response["sessionTimeout"] = to_string(settings.getSessionTimeout());
        response["requireStrongPassword"] = settings.getRequireStrongPassword() ? "true" : "false";
        response["enableAuditLog"] = settings.getEnableAuditLog() ? "true" : "false";
        response["defaultRole"] = settings.getDefaultRole();
        response["version"] = settings.getVersion();
        response["lastBackup"] = settings.getLastBackup();
        
        // Get user count
        AuthEngine auth;
        response["totalUsers"] = to_string(auth.getUserCount());
        
    } else if (action == "update_system_settings") {
        SystemSettings settings;
        
        string minPasswordStr = extractValue(json, "minPasswordLength");
        string sessionTimeoutStr = extractValue(json, "sessionTimeout");
        string requireStrongStr = extractValue(json, "requireStrongPassword");
        string enableAuditStr = extractValue(json, "enableAuditLog");
        string defaultRole = extractValue(json, "defaultRole");
        
        if (!minPasswordStr.empty()) {
            settings.setMinPasswordLength(stoi(minPasswordStr));
        }
        if (!sessionTimeoutStr.empty()) {
            settings.setSessionTimeout(stoi(sessionTimeoutStr));
        }
        if (!requireStrongStr.empty()) {
            settings.setRequireStrongPassword(requireStrongStr == "true");
        }
        if (!enableAuditStr.empty()) {
            settings.setEnableAuditLog(enableAuditStr == "true");
        }
        if (!defaultRole.empty()) {
            settings.setDefaultRole(defaultRole);
        }
        
        response["status"] = "success";
        response["message"] = "Settings updated successfully";
        
    } else if (action == "generate_report") {
        string reportType = extractValue(json, "reportType");
        string timeRange = extractValue(json, "timeRange");
        
        response["status"] = "success";
        
        // Generate report data based on type
        stringstream dataJson;
        dataJson << "[";
        
        if (reportType == "Onboarding Progress") {
            dataJson << "{\"metric\":\"Average Completion\",\"value\":\"68%\",\"change\":\"+5%\",\"status\":\"Improving\"},";
            dataJson << "{\"metric\":\"Tasks Completed\",\"value\":\"142\",\"change\":\"+12\",\"status\":\"On Track\"},";
            dataJson << "{\"metric\":\"Average Time\",\"value\":\"12 days\",\"change\":\"-2 days\",\"status\":\"Faster\"},";
            dataJson << "{\"metric\":\"Stuck Employees\",\"value\":\"3\",\"change\":\"-1\",\"status\":\"Improving\"}";
            
            response["summary"] = "Onboarding Progress Report\\n========================\\n\\n• Overall completion rate improved by 5%\\n• Average time decreased by 2 days\\n• 3 employees need support";
        } else if (reportType == "Skill Evaluation") {
            dataJson << "{\"metric\":\"Average Score\",\"value\":\"78/100\",\"change\":\"+3\",\"status\":\"Good\"},";
            dataJson << "{\"metric\":\"Top Skill\",\"value\":\"DSA\",\"change\":\"85%\",\"status\":\"Excellent\"},";
            dataJson << "{\"metric\":\"Needs Improvement\",\"value\":\"Testing\",\"change\":\"65%\",\"status\":\"Attention\"},";
            dataJson << "{\"metric\":\"Evaluations\",\"value\":\"45\",\"change\":\"+8\",\"status\":\"Active\"}";
            
            response["summary"] = "Skill Evaluation Report\\n=====================\\n\\n• Average score: 78/100\\n• DSA is strongest area\\n• Testing needs focus";
        } else if (reportType == "Team Performance") {
            dataJson << "{\"metric\":\"Team Average Level\",\"value\":\"3.2\",\"change\":\"+0.3\",\"status\":\"Growing\"},";
            dataJson << "{\"metric\":\"High Performers\",\"value\":\"12\",\"change\":\"+2\",\"status\":\"Excellent\"},";
            dataJson << "{\"metric\":\"At Risk\",\"value\":\"3\",\"change\":\"-1\",\"status\":\"Improving\"},";
            dataJson << "{\"metric\":\"Productivity Score\",\"value\":\"82%\",\"change\":\"+4%\",\"status\":\"Strong\"}";
            
            response["summary"] = "Team Performance Report\\n======================\\n\\n• Team level: 3.2 (growing)\\n• 12 high performers\\n• Productivity: 82%";
        } else {
            dataJson << "{\"metric\":\"Total Users\",\"value\":\"25\",\"change\":\"+3\",\"status\":\"Active\"},";
            dataJson << "{\"metric\":\"Active Sessions\",\"value\":\"18\",\"change\":\"+2\",\"status\":\"Normal\"}";
            
            response["summary"] = "General Report\\n=============\\n\\n• 25 total users\\n• 18 active sessions";
        }
        
        dataJson << "]";
        response["data"] = dataJson.str();
        
    } else if (action == "search") {
        string query = extractValue(json, "query");
        string searchType = extractValue(json, "type");
        
        response["status"] = "success";
        
        // Generate search results
        stringstream resultsJson;
        resultsJson << "[";
        
        if (searchType == "All" || searchType == "Users") {
            if (query.find("admin") != string::npos || query.find("user") != string::npos) {
                resultsJson << "{\"type\":\"User\",\"name\":\"admin\",\"details\":\"Administrator\",\"status\":\"Active\"},";
                resultsJson << "{\"type\":\"User\",\"name\":\"john_doe\",\"details\":\"Employee\",\"status\":\"Active\"},";
            }
        }
        
        if (searchType == "All" || searchType == "Tasks") {
            if (query.find("task") != string::npos || query.find("onboard") != string::npos) {
                resultsJson << "{\"type\":\"Task\",\"name\":\"Company Introduction\",\"details\":\"Orientation task\",\"status\":\"Available\"},";
                resultsJson << "{\"type\":\"Task\",\"name\":\"Tool Setup\",\"details\":\"Technical setup\",\"status\":\"In Progress\"},";
            }
        }
        
        if (searchType == "All" || searchType == "Skills") {
            if (query.find("dsa") != string::npos || query.find("skill") != string::npos) {
                resultsJson << "{\"type\":\"Skill\",\"name\":\"DSA\",\"details\":\"Data Structures & Algorithms\",\"status\":\"85%\"},";
                resultsJson << "{\"type\":\"Skill\",\"name\":\"OOP\",\"details\":\"Object-Oriented Programming\",\"status\":\"78%\"},";
            }
        }
        
        string resultsStr = resultsJson.str();
        if (resultsStr.back() == ',') {
            resultsStr.pop_back();
        }
        resultsStr += "]";
        
        response["results"] = resultsStr;
        response["count"] = "3";
        
    } else if (action == "create_backup") {
        string backupPath = extractValue(json, "backupPath");
        
        DataPersistence persistence;
        bool success = persistence.createBackup(backupPath);
        
        if (success) {
            response["status"] = "success";
            response["message"] = "Backup created successfully";
        } else {
            response["status"] = "error";
            response["message"] = "Failed to create backup";
        }
        
    } else if (action == "restore_backup") {
        string backupPath = extractValue(json, "backupPath");
        
        DataPersistence persistence;
        bool success = persistence.restoreFromBackup(backupPath);
        
        if (success) {
            response["status"] = "success";
            response["message"] = "Backup restored successfully";
        } else {
            response["status"] = "error";
            response["message"] = "Failed to restore backup";
        }
        
    } else {
        response["status"] = "error";
        response["message"] = "Unknown action: " + action;
    }

    // Write response
    output << buildJSON(response);
    output.close();

    return 0;
}
