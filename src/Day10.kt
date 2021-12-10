fun main() {
    val syntaxCheckerScoreMap = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )
    val matchMap = mapOf(
        ')' to '(',
        ']' to '[',
        '}' to '{',
        '>' to '<'
    )

    fun part1(input: List<String>): Int {
        var score = 0
        for (line in input) {
            val stack = ArrayList<Char>()
            for (char in line) {
                if (matchMap.containsKey(char)) {
                    if (stack.last() == matchMap[char]) {
                        stack.removeLast()
                    } else {
                        score += syntaxCheckerScoreMap[char]!!
                        break;
                    }
                } else {
                    stack.add(char)
                }
            }
        }
        return score
    }

    val autoCompleteScoreMap = mapOf(
        '(' to 1,
        '[' to 2,
        '{' to 3,
        '<' to 4
    )

    fun part2(input: List<String>): Long {
        val scores = ArrayList<Long>()
        for (line in input) {
            val stack = ArrayList<Char>()
            var corrupt = false;
            for (char in line) {
                if (matchMap.containsKey(char)) {
                    if (stack.last() == matchMap[char]) {
                        stack.removeLast()
                    } else {
                        corrupt = true
                        break;
                    }
                } else {
                    stack.add(char)
                }
            }
            if (!corrupt) {
                if (stack.isEmpty()) {
                    throw Error("empty stack")
                }
                val score =
                    stack
                        .reversed()
                        .map { autoCompleteScoreMap[it]!!.toLong() }
                        .reduce { acc, value -> acc * 5 + value }
                scores.add(score)
            }
        }
        val middleIndex = scores.size / 2
        return scores.sorted()[middleIndex]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10.test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957.toLong())

    val input = readInput("Day10")
    prcp(part1(input))
    prcp(part2(input))
}

