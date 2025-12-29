#include "../include/AuthEngine.h"

AuthEngine::AuthEngine() {
    // Preloaded admin for testing
    users["admin"] = {"admin123", "admin"};
}

bool AuthEngine::signup(const string& username, const string& password, const string& role, string& message) {
    if (users.find(username) != users.end()) {
        message = "User already exists";
        return false;
    }

    users[username] = {password, role};
    message = "User registered";
    return true;
}

bool AuthEngine::login(const string& username, const string& password, string& role, string& message) {
    if (users.find(username) == users.end()) {
        message = "Invalid credentials";
        return false;
    }

    if (users[username].password != password) {
        message = "Invalid credentials";
        return false;
    }

    role = users[username].role;
    message = "Login successful";
    return true;
}
