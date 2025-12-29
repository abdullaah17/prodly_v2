#include "../include/SystemSettings.h"
#include "../include/DataPersistence.h"
#include <sstream>

SystemSettings::SystemSettings() {
    // Try to load settings from file
    DataPersistence persistence;
    unordered_map<string, string> loadedSettings;
    
    if (persistence.loadSettings(loadedSettings)) {
        // Convert unordered_map to map
        for (const auto& pair : loadedSettings) {
            settings[pair.first] = pair.second;
        }
    } else {
        // Initialize default settings
        settings["minPasswordLength"] = "8";
        settings["sessionTimeout"] = "30";
        settings["requireStrongPassword"] = "false";
        settings["enableAuditLog"] = "true";
        settings["defaultRole"] = "employee";
        settings["version"] = "1.0.0";
        settings["lastBackup"] = "Never";
        // Save default settings
        saveToFile();
    }
}

int SystemSettings::getMinPasswordLength() {
    return stoi(settings["minPasswordLength"]);
}

int SystemSettings::getSessionTimeout() {
    return stoi(settings["sessionTimeout"]);
}

bool SystemSettings::getRequireStrongPassword() {
    return settings["requireStrongPassword"] == "true";
}

bool SystemSettings::getEnableAuditLog() {
    return settings["enableAuditLog"] == "true";
}

string SystemSettings::getDefaultRole() {
    return settings["defaultRole"];
}

string SystemSettings::getVersion() {
    return settings["version"];
}

string SystemSettings::getLastBackup() {
    return settings["lastBackup"];
}

void SystemSettings::setMinPasswordLength(int length) {
    settings["minPasswordLength"] = to_string(length);
    saveToFile();
}

void SystemSettings::setSessionTimeout(int minutes) {
    settings["sessionTimeout"] = to_string(minutes);
    saveToFile();
}

void SystemSettings::setRequireStrongPassword(bool require) {
    settings["requireStrongPassword"] = require ? "true" : "false";
    saveToFile();
}

void SystemSettings::setEnableAuditLog(bool enable) {
    settings["enableAuditLog"] = enable ? "true" : "false";
    saveToFile();
}

void SystemSettings::setDefaultRole(const string& role) {
    settings["defaultRole"] = role;
    saveToFile();
}

void SystemSettings::saveToFile() {
    DataPersistence persistence;
    unordered_map<string, string> settingsMap;
    
    // Convert map to unordered_map
    for (const auto& pair : settings) {
        settingsMap[pair.first] = pair.second;
    }
    
    persistence.saveSettings(settingsMap);
}

map<string, string> SystemSettings::getAllSettings() {
    return settings;
}

