#ifndef ROLE_GATE_ENGINE_H
#define ROLE_GATE_ENGINE_H

#include <string>

class RoleGateEngine {
public:
    bool isEligible(int level, const std::string& role);
};

#endif
