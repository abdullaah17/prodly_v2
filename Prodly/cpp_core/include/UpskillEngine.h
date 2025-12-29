#ifndef UPSKILL_ENGINE_H
#define UPSKILL_ENGINE_H

#include <queue>
#include <string>
#include <vector>
#include <stack>
#include <unordered_map>
#include <unordered_set>

using namespace std;

struct RecommendationNode {
    string skillId;
    string skillName;
    int priority;
    vector<string> prerequisites;
    vector<string> nextSkills;
};

class UpskillEngine {
private:
    // Level-1 DSA: Stack for learning path tracking
    stack<string> learningPath;
    
    // Level-2 DSA: Trie for skill search + Recommendation Graph
    struct TrieNode {
        unordered_map<char, TrieNode*> children;
        bool isEnd;
        string skillId;
        TrieNode() : isEnd(false), skillId("") {}
    };
    TrieNode* root;
    
    // Recommendation Graph
    unordered_map<string, RecommendationNode> skillGraph;
    unordered_map<string, unordered_set<string>> completedSkills;

public:
    UpskillEngine();
    ~UpskillEngine();
    
    // Check if re-evaluation is needed using Stack (Level-1)
    bool reEvaluate(const queue<string>& completed);
    
    // Get next recommendations using Trie search (Level-2)
    vector<string> searchSkills(const string& prefix);
    
    // Get personalized recommendations using Graph traversal (Level-2)
    vector<RecommendationNode> getRecommendations(const string& userId, const vector<string>& completed);
    
    // Add skill to Trie
    void addSkill(const string& skillId, const string& skillName);
    
    // Build recommendation graph
    void buildGraph(const vector<RecommendationNode>& skills);
    
    // Get learning path from stack
    vector<string> getCurrentPath();
};

#endif
