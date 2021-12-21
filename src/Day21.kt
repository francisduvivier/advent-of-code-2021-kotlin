typealias WinCounts = List<Long>

fun main() {
    class Player(var pos: Int, var score: Int = 0, var turn: Int = 0);

    fun part1(p1Start: Int, p2Start: Int): Int {
        val players = listOf(Player(p1Start - 1), Player(p2Start - 1))
        var currDie = 0;
        var nbRolls = 0;
        var finished = false;
        val nbSides = 100
        while (!finished) {
            for (player in players) {
                var newVal = 0
                for (i in 0..2) {
                    newVal += currDie + 1
                    currDie = (currDie + 1) % nbSides
                    nbRolls++
                }
                player.pos = (player.pos + newVal) % 10
                player.score += player.pos + 1
                player.turn++
                if (player.score >= 1000) {
                    finished = true;
                    break;
                }
            }
        }
        val loser = players.sortedBy { it.score }[0]
        return nbRolls * loser.score
    }

    val dieOptions = listOf(1, 2, 3)
    fun getNextStates(state: GameState): List<GameState> {
        val newStates = ArrayList<GameState>()
        for (dieOption in dieOptions) {
            var newTurnIndex = state.turnIndex
            var newDiceIndex = state.diceIndex + 1
            var newScores = state.scores
            var newScoreAddition = state.currScoreAddition + dieOption
            val newBoardPlaces = state.boardPlaces.toMutableList()
            newBoardPlaces[newTurnIndex] = (newBoardPlaces[newTurnIndex] + newScoreAddition) % 10
            if (newDiceIndex == 3) {
                newScores = newScores.toMutableList()
                newScores[newTurnIndex] += newBoardPlaces[newTurnIndex] + 1
                newScoreAddition = 0
                newDiceIndex = 0
                newTurnIndex = (newTurnIndex + 1) % state.scores.size
            }
            newStates.add(GameState(newTurnIndex, newDiceIndex, newScores, newScoreAddition, newBoardPlaces))
        }
        return newStates
    }

    fun rollDieRec(state: GameState, winMap: java.util.HashMap<GameState, List<Long>>) {
        if (winMap[state] != null) {
            return
        } else if (state.scores.find { it >= 21 } != null) {
            winMap[state] = state.scores.map { if (it >= 21) 1 else 0 }
            return
        } else {
            val followStates = getNextStates(state)
            followStates.forEach { rollDieRec(it, winMap) }
            followStates.map { winMap[it] }
            val summedWinCounts =
                followStates.map { winMap[it] }.requireNoNulls()
                    .reduce { acc, other -> listOf(acc[0] + other[0], acc[1] + other[1]) }
            winMap[state] = summedWinCounts
        }
    }

    fun part2(p1Start: Int, p2Start: Int): Long {
        val winMap = HashMap<GameState, WinCounts>()
        val startState = GameState(0, 0, listOf(0, 0), 0, arrayListOf(p1Start - 1, p2Start - 1))
        rollDieRec(startState, winMap)
        return winMap[startState]!!.maxOrNull()!!
    }

    // test if implementation meets criteria from the description, like:
    val day = 21
    println("Starting Day${day}")
    checkEquals(part1(4, 8), 739785)
    prcp(part1(1, 6))
    checkEquals(part2(4, 8), 444356092776315)
//    prcp(part2(input))
}

class GameState(
    var turnIndex: Int, var diceIndex: Int, val scores: List<Int>, val currScoreAddition: Int,
    val boardPlaces: List<Int>
) {

    override fun toString(): String {
        return "t$turnIndex;$diceIndex;${scores.joinToString(",")};${boardPlaces.joinToString(",")};$currScoreAddition+"
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }
}
