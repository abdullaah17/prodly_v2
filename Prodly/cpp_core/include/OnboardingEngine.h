#ifndef ONBOARDING_ENGINE_H
#define ONBOARDING_ENGINE_H

#include <queue>
#include <string>

class OnboardingEngine {
public:
    std::queue<std::string> generateTasks(bool eligible);
};

#endif
