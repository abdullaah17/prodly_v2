#include <iostream>
#include <fstream>
#include <string>
#include "include/AuthEngine.h"

using namespace std;

// Naive JSON value extractor (no libraries allowed)
string extractValue(const string& json, const string& key) {
    size_t pos = json.find(key);
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

    string json, line;
    while (getline(input, line)) json += line;
    input.close();

    string action = extractValue(json, "action");
    string username = extractValue(json, "username");
    string password = extractValue(json, "password");
    string role = extractValue(json, "role");

    AuthEngine auth;
    string message;

    if (action == "signup") {
        bool success = auth.signup(username, password, role, message);
        output << "{ \"status\": \"" << (success ? "success" : "error")
               << "\", \"message\": \"" << message << "\" }";
    }
    else if (action == "login") {
        string userRole;
        bool success = auth.login(username, password, userRole, message);
        output << "{ \"status\": \"" << (success ? "success" : "error") << "\", ";
        if (success) {
            output << "\"role\": \"" << userRole << "\", ";
        }
        output << "\"message\": \"" << message << "\" }";
    }
    else {
        output << "{ \"status\": \"error\", \"message\": \"Unknown action\" }";
    }

    output.close();
    return 0;
}
