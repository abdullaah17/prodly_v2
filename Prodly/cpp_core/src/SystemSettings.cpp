#include "../include/SystemSettings.h"
#include <sstream>

SystemSettings::SystemSettings() {
    // Initialize default settings
    settings["minPasswordLength"] = "8";
    settings["sessionTimeout"] = "30";
    settings["requireStrongPassword"] = "false";
    settings["enableAuditLog"] = "true";
    settings["defaultRole"] = "employee";
    settings["version"] = "1.0.0";
    settings["lastBackup"] = "Never";
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
}

void SystemSettings::setSessionTimeout(int minutes) {
    settings["sessionTimeout"] = to_string(minutes);
}

void SystemSettings::setRequireStrongPassword(bool require) {
    settings["requireStrongPassword"] = require ? "true" : "false";
}

void SystemSettings::setEnableAuditLog(bool enable) {
    settings["enableAuditLog"] = enable ? "true" : "false";
}

void SystemSettings::setDefaultRole(const string& role) {
    settings["defaultRole"] = role;
}

map<string, string> SystemSettings::getAllSettings() {
    return settings;
}

