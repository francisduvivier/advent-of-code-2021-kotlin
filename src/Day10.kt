fun main() {
    val scoreMap = mapOf(
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
                        score += scoreMap[char]!!
                        break;
                    }
                } else {
                    stack.add(char)
                }
            }
        }
        return score
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10.test")
    check(part1(testInput) == 26397)

    val input = readInput("Day10")
    prcp(part1(input))
    prcp(part2(input))
}
