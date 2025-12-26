#ifndef LEVELING_ENGINE_H
#define LEVELING_ENGINE_H

#include <map>
#include <string>

class LevelingEngine {
public:
    int assignLevel(const std::map<std::string,int>& scores);
};

#endif
