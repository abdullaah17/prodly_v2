#ifndef DATA_PERSISTENCE_H
#define DATA_PERSISTENCE_H

#include <string>
#include <unordered_map>
#include <vector>
#include <fstream>

using namespace std;

struct UserData {
    string password;
    string role;
    string createdAt;
};

class DataPersistence {
private:
    string usersFile;
    string settingsFile;
    
    // Helper methods
    string escapeString(const string& str);
    string unescapeString(const string& str);
    
public:
    DataPersistence();
    
    // User persistence
    bool saveUsers(const unordered_map<string, UserData>& users);
    bool loadUsers(unordered_map<string, UserData>& users);
    
    // Settings persistence
    bool saveSettings(const unordered_map<string, string>& settings);
    bool loadSettings(unordered_map<string, string>& settings);
    
    // Backup and restore
    bool createBackup(const string& backupPath);
    bool restoreFromBackup(const string& backupPath);
    
    // Check if data files exist
    bool dataFilesExist();
};

#endif

