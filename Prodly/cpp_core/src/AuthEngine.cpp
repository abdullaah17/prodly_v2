#include "../include/AuthEngine.h"
#include "../include/DataPersistence.h"
#include <algorithm>
#include <chrono>
#include <ctime>
#include <iomanip>
#include <sstream>

string getCurrentDate() {
    auto now = chrono::system_clock::now();
    time_t now_c = chrono::system_clock::to_time_t(now);
    tm* ltm = localtime(&now_c);
    stringstream ss;
    ss << put_time(ltm, "%Y-%m-%d");
    return ss.str();
}

void AuthEngine::saveUsersToFile() {
    DataPersistence persistence;
    unordered_map<string, UserData> userDataMap;
    
    // Convert User to UserData
    for (const auto& pair : users) {
        UserData userData;
        userData.password = pair.second.password;
        userData.role = pair.second.role;
        userData.createdAt = pair.second.createdAt.empty() ? getCurrentDate() : pair.second.createdAt;
        userDataMap[pair.first] = userData;
    }
    
    persistence.saveUsers(userDataMap);
}

AuthEngine::AuthEngine() {
    // Try to load users from file
    DataPersistence persistence;
    unordered_map<string, UserData> loadedUsers;
    
    if (persistence.loadUsers(loadedUsers)) {
        // Convert UserData to User
        for (const auto& pair : loadedUsers) {
            User user;
            user.password = pair.second.password;
            user.role = pair.second.role;
            user.createdAt = pair.second.createdAt.empty() ? getCurrentDate() : pair.second.createdAt;
            users[pair.first] = user;
        }
    }
    
    // If no users loaded or admin doesn't exist, create default admin
    if (users.empty() || users.find("admin") == users.end()) {
        User adminUser;
        adminUser.password = "admin123";
        adminUser.role = "admin";
        adminUser.createdAt = getCurrentDate();
        users["admin"] = adminUser;
        // Save default user
        saveUsersToFile();
    }
}

bool AuthEngine::signup(const string& username, const string& password, const string& role, string& message) {
    if (users.find(username) != users.end()) {
        message = "User already exists";
        return false;
    }

    User newUser;
    newUser.password = password;
    newUser.role = role;
    newUser.createdAt = getCurrentDate();
    users[username] = newUser;
    
    // Save to file
    saveUsersToFile();
    
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
    
    // Save to file
    saveUsersToFile();
    
    message = "User deleted successfully";
    return true;
}

bool AuthEngine::updateUserRole(const string& username, const string& newRole, string& message) {
    if (users.find(username) == users.end()) {
        message = "User not found";
        return false;
    }
    
    users[username].role = newRole;
    
    // Save to file
    saveUsersToFile();
    
    message = "User role updated successfully";
    return true;
}

bool AuthEngine::updateUserPassword(const string& username, const string& newPassword, string& message) {
    if (users.find(username) == users.end()) {
        message = "User not found";
        return false;
    }
    
    users[username].password = newPassword;
    
    // Save to file
    saveUsersToFile();
    
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
