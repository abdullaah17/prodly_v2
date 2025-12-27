#include "../include/OnboardingEngine.h"

std::queue<std::string> OnboardingEngine::generateTasks(bool eligible) {
	std::queue<std::string> tasks;
	if (eligible) {
		tasks.push("Company Intro");
		tasks.push("Tool Setup");
	} else {
		tasks.push("DSA Practice");
		tasks.push("OOP Revision");
		tasks.push("Re-test");
	}
	return tasks;
}
