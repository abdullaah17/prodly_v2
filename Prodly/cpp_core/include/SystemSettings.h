#ifndef SYSTEM_SETTINGS_H
#define SYSTEM_SETTINGS_H

#include <string>
#include <map>

using namespace std;

class SystemSettings {
private:
    map<string, string> settings;

public:
    SystemSettings();
    
    // Get settings
    int getMinPasswordLength();
    int getSessionTimeout();
    bool getRequireStrongPassword();
    bool getEnableAuditLog();
    string getDefaultRole();
    string getVersion();
    string getLastBackup();
    
    // Set settings
    void setMinPasswordLength(int length);
    void setSessionTimeout(int minutes);
    void setRequireStrongPassword(bool require);
    void setEnableAuditLog(bool enable);
    void setDefaultRole(const string& role);
    
    // Get all settings as map
    map<string, string> getAllSettings();
};

#endif

