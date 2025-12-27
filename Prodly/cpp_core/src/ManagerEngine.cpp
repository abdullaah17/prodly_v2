#include "../include/ManagerEngine.h"

int ManagerEngine::findRiskEmployee(const std::vector<int>& levels) {
	int minIndex = 0;
	for (int i = 1; i < levels.size(); i++)
		if (levels[i] < levels[minIndex])
			minIndex = i;
	return minIndex;
}
