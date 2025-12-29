#ifndef LEVELING_ENGINE_H
#define LEVELING_ENGINE_H

#include <map>
#include <string>
#include <vector>
#include <queue>
#include <algorithm>

using namespace std;

struct LevelCandidate {
    string userId;
    int totalScore;
    int level;
    
    bool operator<(const LevelCandidate& other) const {
        return totalScore < other.totalScore; // Max priority queue
    }
};

class LevelingEngine {
private:
    // Level-1 DSA: Sorting for score ordering
    vector<pair<string, int>> sortedScores;
    
    // Level-2 DSA: Priority Queue for level assignment
    priority_queue<LevelCandidate> levelQueue;

public:
    LevelingEngine();
    
    // Assign level based on scores
    // Uses Sorting (Level-1) - O(n log n)
    int assignLevel(const map<string, int>& scores);
    
    // Batch level assignment using Priority Queue (Level-2)
    vector<pair<string, int>> assignLevelsBatch(const vector<map<string, int>>& allScores);
    
    // Get level distribution
    map<int, int> getLevelDistribution(const vector<int>& levels);
};

#endif
