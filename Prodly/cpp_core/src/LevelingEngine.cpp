#include "../include/LevelingEngine.h"

int LevelingEngine::assignLevel(const std::map<std::string,int>& scores) {
	int total = 0;
	for (auto &s : scores) total += s.second;
	int avg = total / scores.size();

	if (avg < 40) return 1;
	if (avg < 60) return 2;
	if (avg < 75) return 3;
	if (avg < 90) return 4;
	return 5;
}
