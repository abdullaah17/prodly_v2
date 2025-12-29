#ifndef EVALUATION_ENGINE_H
#define EVALUATION_ENGINE_H

#include <vector>
#include <unordered_map>
#include <string>
#include <queue>
#include <algorithm>

using namespace std;

struct SkillScore {
    string skill;
    int score;
    bool operator<(const SkillScore& other) const {
        return score < other.score; // Min heap
    }
};

class EvaluationEngine {
private:
    // Level-1 DSA: Hash Table for skill-score mapping
    unordered_map<string, int> skillScores;
    
    // Level-2 DSA: Heap (Priority Queue) for top skills
    priority_queue<SkillScore> topSkillsHeap;

public:
    EvaluationEngine();
    
    // Evaluate scores and return skill breakdown
    // Uses Hash Table (Level-1) for O(1) lookups
    unordered_map<string, int> evaluate(const vector<int>& scores);
    
    // Get top N skills using Heap (Level-2) - O(k log n)
    vector<pair<string, int>> getTopSkills(int n);
    
    // Get average score
    double getAverageScore();
};

#endif
