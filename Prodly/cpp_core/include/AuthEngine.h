#ifndef AUTH_ENGINE_H
#define AUTH_ENGINE_H

#include <string>
#include <unordered_map>

using namespace std;

struct User {
    string password;
    string role;
};

class AuthEngine {
private:
    unordered_map<string, User> users; // Hash Table

public:
    AuthEngine();
    bool signup(const string& username, const string& password, const string& role, string& message);
    bool login(const string& username, const string& password, string& role, string& message);
};

#endif
