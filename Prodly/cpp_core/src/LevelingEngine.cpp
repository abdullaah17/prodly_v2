#include "../include/LevelingEngine.h"
#include <numeric>
#include <algorithm>

LevelingEngine::LevelingEngine() {
    sortedScores.clear();
}

// Level-1 DSA: Sorting - O(n log n)
int LevelingEngine::assignLevel(const map<string, int>& scores) {
    if (scores.empty()) return 1;
    
    // Calculate total score
    int total = 0;
    for (const auto& pair : scores) {
        total += pair.second;
    }
    int avg = total / scores.size();
    
    // Level assignment based on average
    if (avg < 40) return 1;
    if (avg < 60) return 2;
    if (avg < 75) return 3;
    if (avg < 90) return 4;
    return 5;
}

// Level-2 DSA: Priority Queue - O(n log n) for batch processing
vector<pair<string, int>> LevelingEngine::assignLevelsBatch(const vector<map<string, int>>& allScores) {
    priority_queue<LevelCandidate> pq;
    
    // Build priority queue
    for (size_t i = 0; i < allScores.size(); i++) {
        LevelCandidate candidate;
        candidate.userId = "user" + to_string(i);
        
        int total = 0;
        for (const auto& pair : allScores[i]) {
            total += pair.second;
        }
        candidate.totalScore = total;
        candidate.level = assignLevel(allScores[i]);
        
        pq.push(candidate); // O(log n) insertion
    }
    
    // Extract results
    vector<pair<string, int>> result;
    while (!pq.empty()) {
        LevelCandidate top = pq.top();
        pq.pop();
        result.push_back({top.userId, top.level});
    }
    
    return result;
}

map<int, int> LevelingEngine::getLevelDistribution(const vector<int>& levels) {
    map<int, int> distribution;
    for (int level : levels) {
        distribution[level]++;
    }
    return distribution;
}
