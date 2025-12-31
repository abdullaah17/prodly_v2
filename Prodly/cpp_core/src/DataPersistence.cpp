#include "../include/DataPersistence.h"
#include <sstream>
#include <algorithm>

DataPersistence::DataPersistence() {
    usersFile = "data/users.dat";
    settingsFile = "data/settings.dat";
}

string DataPersistence::escapeString(const string& str) {
    string result;
    for (char c : str) {
        if (c == '|' || c == '\n' || c == '\r') {
            result += "\\";
        }
        result += c;
    }
    return result;
}

string DataPersistence::unescapeString(const string& str) {
    string result;
    for (size_t i = 0; i < str.length(); i++) {
        if (str[i] == '\\' && i + 1 < str.length()) {
            i++;
        }
        result += str[i];
    }
    return result;
}

bool DataPersistence::saveUsers(const unordered_map<string, UserData>& users) {
    ofstream file(usersFile);
    if (!file.is_open()) {
        return false;
    }
    
    for (const auto& pair : users) {
        file << escapeString(pair.first) << "|"
             << escapeString(pair.second.password) << "|"
             << escapeString(pair.second.role) << "|"
             << escapeString(pair.second.createdAt) << "\n";
    }
    
    file.close();
    return true;
}

bool DataPersistence::loadUsers(unordered_map<string, UserData>& users) {
    ifstream file(usersFile);
    if (!file.is_open()) {
        return false;
    }
    
    users.clear();
    string line;
    while (getline(file, line)) {
        if (line.empty()) continue;
        
        stringstream ss(line);
        string token;
        vector<string> tokens;
        
        while (getline(ss, token, '|')) {
            tokens.push_back(unescapeString(token));
        }
        
        if (tokens.size() >= 3) {
            UserData user;
            user.password = tokens[1];
            user.role = tokens[2];
            user.createdAt = tokens.size() > 3 ? tokens[3] : "2024-01-01";
            users[tokens[0]] = user;
        }
    }
    
    file.close();
    return true;
}

bool DataPersistence::saveSettings(const unordered_map<string, string>& settings) {
    ofstream file(settingsFile);
    if (!file.is_open()) {
        return false;
    }
    
    for (const auto& pair : settings) {
        file << escapeString(pair.first) << "="
             << escapeString(pair.second) << "\n";
    }
    
    file.close();
    return true;
}

bool DataPersistence::loadSettings(unordered_map<string, string>& settings) {
    ifstream file(settingsFile);
    if (!file.is_open()) {
        return false;
    }
    
    settings.clear();
    string line;
    while (getline(file, line)) {
        if (line.empty()) continue;
        
        size_t pos = line.find('=');
        if (pos != string::npos) {
            string key = unescapeString(line.substr(0, pos));
            string value = unescapeString(line.substr(pos + 1));
            settings[key] = value;
        }
    }
    
    file.close();
    return true;
}

bool DataPersistence::createBackup(const string& backupPath) {
    // Simple backup - copy files
    ifstream usersIn(usersFile);
    ifstream settingsIn(settingsFile);
    
    if (!usersIn.is_open() && !settingsIn.is_open()) {
        return false;
    }
    
    ofstream backup(backupPath);
    if (!backup.is_open()) {
        return false;
    }
    
    backup << "=== PRODLY BACKUP ===\n";
    
    if (usersIn.is_open()) {
        backup << "=== USERS ===\n";
        string line;
        while (getline(usersIn, line)) {
            backup << line << "\n";
        }
        usersIn.close();
    }
    
    if (settingsIn.is_open()) {
        backup << "=== SETTINGS ===\n";
        string line;
        while (getline(settingsIn, line)) {
            backup << line << "\n";
        }
        settingsIn.close();
    }
    
    backup.close();
    return true;
}

bool DataPersistence::restoreFromBackup(const string& backupPath) {
    // Simplified restore - would need proper parsing
    return false; // Not fully implemented
}

bool DataPersistence::dataFilesExist() {
    ifstream users(usersFile);
    ifstream settings(settingsFile);
    bool usersExist = users.is_open();
    bool settingsExist = settings.is_open();
    if (usersExist) users.close();
    if (settingsExist) settings.close();
    return usersExist || settingsExist;
}


