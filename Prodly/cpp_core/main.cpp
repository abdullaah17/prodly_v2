#include <fstream>
#include <vector>
#include "EvaluationEngine.h"
#include "LevelingEngine.h"
#include "RoleGateEngine.h"

int main() {
    std::ifstream in("integration/input/input.txt");
    int a,b,c;
    in >> a >> b >> c;
    in.close();

    EvaluationEngine eval;
    auto scores = eval.evaluate({a,b,c});

    LevelingEngine leveler;
    int level = leveler.assignLevel(scores);

    std::ofstream out("integration/output/result.txt");
    out << level;
    out.close();

    return 0;
}
