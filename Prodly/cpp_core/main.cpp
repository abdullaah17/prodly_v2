#include <iostream>
#include <fstream>
#include <string>

#include "include/AuthEngine.h"
#include "include/OnboardingEngine.h"

using namespace std;

string extractValue(const string& json, const string& key) {
    size_t pos = json.find("\"" + key + "\"");
    if (pos == string::npos) return "";
    pos = json.find(":", pos);
    pos = json.find("\"", pos) + 1;
    size_t end = json.find("\"", pos);
    return json.substr(pos, end - pos);
}

int main() {
    // 🔴 ABSOLUTE TRUTH: where files are opened
    ifstream input("../input.json");
    ofstream output("../output.json");

    // 🔎 HARD PROOF CHECK
    if (!input.is_open()) {
        output << "{ \"status\": \"error\", \"message\": \"input.json not found\" }";
        output.close();
        return 0;
    }

    string json, line;
    while (getline(input, line)) {
        json += line;
    }
    input.close();

    string action   = extractValue(json, "action");
    string username = extractValue(json, "username");
    string password = extractValue(json, "password");
    string role     = extractValue(json, "role");

    AuthEngine auth;
    string message;

    if (action == "login") {
        string userRole;
        bool success = auth.login(username, password, userRole, message);

        if (!success) {
            output << "{ \"status\": \"error\", \"message\": \"" << message << "\" }";
        } else {
            OnboardingEngine engine(userRole);

            auto tasks = engine.getAvailableTasks();
            int completion = engine.getCompletionPercent();

            output << "{";
            output << "\"status\":\"success\",";
            output << "\"role\":\"" << userRole << "\",";
            output << "\"completion\":" << completion << ",";
            output << "\"tasks\":[";

            for (size_t i = 0; i < tasks.size(); i++) {
                output << "{";
                output << "\"id\":\"" << tasks[i].id << "\",";
                output << "\"name\":\"" << tasks[i].name << "\"";
                output << "}";
                if (i != tasks.size() - 1) output << ",";
            }

            output << "]";
            output << "}";
        }
    } else {
        output << "{ \"status\": \"error\", \"message\": \"Unknown action\" }";
    }

    output.close();
    return 0;
}
