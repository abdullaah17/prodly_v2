#include "../include/AuditEngine.h"
#include <iostream>

void AuditEngine::recordEvent(const char* e) { std::cout << "Audit: " << e << std::endl; }
