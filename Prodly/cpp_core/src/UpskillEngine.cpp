#include "../include/UpskillEngine.h"
#include <algorithm>
#include <functional>
#include <queue>
#include <stack>

UpskillEngine::UpskillEngine() {
    root = new TrieNode();
    skillGraph.clear();
    completedSkills.clear();
}

UpskillEngine::~UpskillEngine() {
    // Cleanup Trie (simplified - full cleanup would require recursive deletion)
    delete root;
}

// Level-1 DSA: Stack operations - O(1) push/pop
bool UpskillEngine::reEvaluate(const queue<string>& completed) {
    // Convert queue to stack for path tracking
    stack<string> tempStack;
    queue<string> tempQueue = completed;
    
    while (!tempQueue.empty()) {
        tempStack.push(tempQueue.front());
        tempQueue.pop();
    }
    
    // Check if enough skills completed (threshold: 3)
    if (tempStack.size() >= 3) {
        // Update learning path
        while (!tempStack.empty()) {
            learningPath.push(tempStack.top());
            tempStack.pop();
        }
        return true;
    }
    
    return false;
}

// Level-2 DSA: Trie search - O(m) where m is prefix length
vector<string> UpskillEngine::searchSkills(const string& prefix) {
    vector<string> results;
    TrieNode* current = root;
    
    // Navigate to prefix node
    for (char c : prefix) {
        if (current->children.find(c) == current->children.end()) {
            return results; // Prefix not found
        }
        current = current->children[c];
    }
    
    // DFS from current node to find all words with this prefix
    function<void(TrieNode*, string)> dfs = [&](TrieNode* node, string word) {
        if (node->isEnd && !node->skillId.empty()) {
            results.push_back(node->skillId);
        }
        for (const auto& pair : node->children) {
            dfs(pair.second, word + pair.first);
        }
    };
    
    dfs(current, prefix);
    return results;
}

// Level-2 DSA: Graph traversal for recommendations - O(V + E)
vector<RecommendationNode> UpskillEngine::getRecommendations(
    const string& userId, 
    const vector<string>& completed) {
    
    unordered_set<string> completedSet(completed.begin(), completed.end());
    vector<RecommendationNode> recommendations;
    
    // BFS from completed skills to find next recommendations
    queue<string> q;
    unordered_set<string> visited;
    
    // Start from completed skills
    for (const string& skillId : completed) {
        if (skillGraph.find(skillId) != skillGraph.end()) {
            q.push(skillId);
            visited.insert(skillId);
        }
    }
    
    while (!q.empty() && recommendations.size() < 10) {
        string current = q.front();
        q.pop();
        
        if (skillGraph.find(current) != skillGraph.end()) {
            RecommendationNode& node = skillGraph[current];
            
            // Check if all prerequisites are met
            bool canRecommend = true;
            for (const string& prereq : node.prerequisites) {
                if (completedSet.find(prereq) == completedSet.end()) {
                    canRecommend = false;
                    break;
                }
            }
            
            // If not completed and prerequisites met, recommend
            if (canRecommend && completedSet.find(node.skillId) == completedSet.end()) {
                recommendations.push_back(node);
            }
            
            // Add next skills to queue
            for (const string& next : node.nextSkills) {
                if (visited.find(next) == visited.end()) {
                    visited.insert(next);
                    q.push(next);
                }
            }
        }
    }
    
    // Sort by priority
    sort(recommendations.begin(), recommendations.end(), 
         [](const RecommendationNode& a, const RecommendationNode& b) {
             return a.priority > b.priority;
         });
    
    return recommendations;
}

void UpskillEngine::addSkill(const string& skillId, const string& skillName) {
    TrieNode* current = root;
    
    // Insert into Trie
    for (char c : skillName) {
        if (current->children.find(c) == current->children.end()) {
            current->children[c] = new TrieNode();
        }
        current = current->children[c];
    }
    
    current->isEnd = true;
    current->skillId = skillId;
}

void UpskillEngine::buildGraph(const vector<RecommendationNode>& skills) {
    for (const RecommendationNode& skill : skills) {
        skillGraph[skill.skillId] = skill;
    }
}

vector<string> UpskillEngine::getCurrentPath() {
    vector<string> path;
    stack<string> temp = learningPath;
    
    while (!temp.empty()) {
        path.push_back(temp.top());
        temp.pop();
    }
    
    reverse(path.begin(), path.end());
    return path;
}
