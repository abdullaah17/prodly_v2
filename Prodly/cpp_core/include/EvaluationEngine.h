#ifndef EVALUATION_ENGINE_H
#define EVALUATION_ENGINE_H

#include <vector>
#include <map>
#include <string>

class EvaluationEngine {
public:
    std::map<std::string, int> evaluate(const std::vector<int>& scores);
};

#endif
