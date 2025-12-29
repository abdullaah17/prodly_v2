#include <iostream>
#include <fstream>
#include <string>

#include "include/AuthEngine.h"
#include "include/OnboardingEngine.h"

using namespace std;

/*
  Simple JSON value extractor
  NOTE: This is intentional (no external libraries allowed)
*/
string extractValue(const string& json, const string& key) {
    size_t pos = json.find("\"" + key + "\"");
    if (pos == string::npos) return "";
    pos = json.find(":", pos);
    pos = json.find("\"", pos) + 1;
    size_t end = json.find("\"", pos);
    return json.substr(pos, end - pos);
}

int main() {
    ifstream input("input.json");
    ofstream output("output.json");


    if (!input.is_open()) {
        output << "{ \"status\": \"error\", \"message\": \"input.json not found\" }";
        return 1;
    }

    // Read full JSON
    string json, line;
    while (getline(input, line)) {
        json += line;
    }
    input.close();

    // Extract common fields
    string action   = extractValue(json, "action");
    string username = extractValue(json, "username");
    string password = extractValue(json, "password");
    string role     = extractValue(json, "role");

    AuthEngine auth;
    string message;

    // ---------------- SIGNUP ----------------
    if (action == "signup") {

        bool success = auth.signup(username, password, role, message);

        output << "{ "
               << "\"status\": \"" << (success ? "success" : "error") << "\", "
               << "\"message\": \"" << message << "\" "
               << "}";

    }
    // ---------------- LOGIN ----------------
    else if (action == "login") {

        string userRole;
        bool success = auth.login(username, password, userRole, message);

        if (!success) {
            output << "{ "
                   << "\"status\": \"error\", "
                   << "\"message\": \"" << message << "\" "
                   << "}";
        } else {

            // 🔹 ONBOARDING ENGINE STARTS HERE
            OnboardingEngine engine(userRole);
            vector<Task> tasks = engine.getAvailableTasks();
            int completion = engine.getCompletionPercent();

            output << "{ ";
            output << "\"status\": \"success\", ";
            output << "\"role\": \"" << userRole << "\", ";
            output << "\"completion\": " << completion << ", ";
            output << "\"tasks\": [";

            for (size_t i = 0; i < tasks.size(); i++) {
                output << "{ "
                       << "\"id\": \"" << tasks[i].id << "\", "
                       << "\"name\": \"" << tasks[i].name << "\" "
                       << "}";
                if (i != tasks.size() - 1)
                    output << ", ";
            }

            output << "] ";
            output << "}";
        }
    }
    // ---------------- UNKNOWN ACTION ----------------
    else {
        output << "{ "
               << "\"status\": \"error\", "
               << "\"message\": \"Unknown action\" "
               << "}";
    }

    output.close();
    return 0;
}
