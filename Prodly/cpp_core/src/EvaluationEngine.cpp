#include "EvaluationEngine.h"

std::map<std::string, int> EvaluationEngine::evaluate(const std::vector<int>& scores) {
    std::map<std::string, int> result;
    result["DSA"] = scores[0];
    result["OOP"] = scores[1];
    result["DB"]  = scores[2];
    return result;
}
