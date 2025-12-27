#include "../include/RoleGateEngine.h"

bool RoleGateEngine::isEligible(int level, const std::string& role) {
	if (role == "Backend") return level >= 3;
	if (role == "Frontend") return level >= 2;
	if (role == "Data") return level >= 4;
	return false;
}
