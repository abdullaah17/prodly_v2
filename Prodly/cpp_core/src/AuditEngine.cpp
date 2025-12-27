#include "../include/AuditEngine.h"
#include <fstream>

void AuditEngine::log(const std::string& event) {
	std::ofstream out("integration/output/audit.txt", std::ios::app);
	out << event << "\n";
	out.close();
}
