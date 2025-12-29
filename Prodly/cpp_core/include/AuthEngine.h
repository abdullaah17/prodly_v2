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
    unordered_map<string, User> users;

public:
    AuthEngine();
    bool signup(const string&, const string&, const string&, string&);
    bool login(const string&, const string&, string&, string&);
};

#endif
