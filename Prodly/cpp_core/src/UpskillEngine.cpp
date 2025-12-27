#include "../include/UpskillEngine.h"

bool UpskillEngine::reEvaluate(const std::queue<std::string>& completed) {
	return completed.size() >= 3;
}
