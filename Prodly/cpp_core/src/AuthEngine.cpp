#include "../include/AuthEngine.h"
#include <algorithm>

AuthEngine::AuthEngine() {
    users["admin"] = {"admin123", "admin", "2024-01-01"};
}

bool AuthEngine::signup(const string& username, const string& password, const string& role, string& message) {
    if (users.find(username) != users.end()) {
        message = "User already exists";
        return false;
    }

    users[username] = {password, role, "2024-01-01"}; // Simplified date
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

vector<pair<string, User>> AuthEngine::getAllUsers() {
    vector<pair<string, User>> result;
    for (const auto& pair : users) {
        result.push_back(pair);
    }
    return result;
}

bool AuthEngine::deleteUser(const string& username, string& message) {
    if (username == "admin") {
        message = "Cannot delete admin user";
        return false;
    }
    
    if (users.find(username) == users.end()) {
        message = "User not found";
        return false;
    }
    
    users.erase(username);
    message = "User deleted successfully";
    return true;
}

bool AuthEngine::updateUserRole(const string& username, const string& newRole, string& message) {
    if (users.find(username) == users.end()) {
        message = "User not found";
        return false;
    }
    
    users[username].role = newRole;
    message = "User role updated successfully";
    return true;
}

bool AuthEngine::updateUserPassword(const string& username, const string& newPassword, string& message) {
    if (users.find(username) == users.end()) {
        message = "User not found";
        return false;
    }
    
    users[username].password = newPassword;
    message = "Password updated successfully";
    return true;
}

bool AuthEngine::userExists(const string& username) {
    return users.find(username) != users.end();
}

int AuthEngine::getUserCount() {
    return users.size();
}

int AuthEngine::getUserCountByRole(const string& role) {
    int count = 0;
    for (const auto& pair : users) {
        if (pair.second.role == role) {
            count++;
        }
    }
    return count;
}
