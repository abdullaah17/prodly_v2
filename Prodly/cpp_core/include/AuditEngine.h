#ifndef AUDIT_ENGINE_H
#define AUDIT_ENGINE_H

#include <queue>
#include <string>

class AuditEngine {
public:
    void log(const std::string& event);
};

#endif
