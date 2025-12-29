#include "../include/EvaluationEngine.h"
#include <numeric>
#include <algorithm>

EvaluationEngine::EvaluationEngine() {
    // Initialize with default skills
    skillScores["DSA"] = 0;
    skillScores["OOP"] = 0;
    skillScores["DB"] = 0;
    skillScores["System Design"] = 0;
    skillScores["Testing"] = 0;
}

// Level-1 DSA: Hash Table - O(1) average case for insertions
unordered_map<string, int> EvaluationEngine::evaluate(const vector<int>& scores) {
    vector<string> skills = {"DSA", "OOP", "DB", "System Design", "Testing"};
    
    skillScores.clear();
    for (size_t i = 0; i < skills.size() && i < scores.size(); i++) {
        skillScores[skills[i]] = scores[i]; // Hash table insertion O(1)
    }
    
    // Build heap for top skills (Level-2 DSA)
    priority_queue<SkillScore> tempHeap;
    for (const auto& pair : skillScores) {
        SkillScore ss;
        ss.skill = pair.first;
        ss.score = pair.second;
        tempHeap.push(ss);
    }
    topSkillsHeap = tempHeap;
    
    return skillScores;
}

// Level-2 DSA: Heap (Priority Queue) - O(k log n) for top k
vector<pair<string, int>> EvaluationEngine::getTopSkills(int n) {
    vector<pair<string, int>> result;
    priority_queue<SkillScore> temp = topSkillsHeap;
    
    int count = 0;
    while (!temp.empty() && count < n) {
        SkillScore top = temp.top();
        temp.pop();
        result.push_back({top.skill, top.score});
        count++;
    }
    
    return result;
}

double EvaluationEngine::getAverageScore() {
    if (skillScores.empty()) return 0.0;
    
    int sum = 0;
    for (const auto& pair : skillScores) {
        sum += pair.second;
    }
    return (double)sum / skillScores.size();
}
