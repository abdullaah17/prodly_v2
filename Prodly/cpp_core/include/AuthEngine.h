#ifndef AUTH_ENGINE_H
#define AUTH_ENGINE_H

#include <string>
#include <unordered_map>
#include <vector>

using namespace std;

struct User {
    string password;
    string role;
    string createdAt;  // For tracking
};

class AuthEngine {
private:
    unordered_map<string, User> users;

public:
    AuthEngine();
    bool signup(const string&, const string&, const string&, string&);
    bool login(const string&, const string&, string&, string&);
    
    // User management functions
    vector<pair<string, User>> getAllUsers();
    bool deleteUser(const string& username, string& message);
    bool updateUserRole(const string& username, const string& newRole, string& message);
    bool updateUserPassword(const string& username, const string& newPassword, string& message);
    bool userExists(const string& username);
    int getUserCount();
    int getUserCountByRole(const string& role);
    
private:
    void saveUsersToFile();
};

#endif
