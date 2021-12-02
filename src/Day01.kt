fun main() {
    fun part1(input: List<String>): Int {
        var count = 0;
        for (i in 1..input.size - 1) {
            if (input[i].toInt() > input[i - 1].toInt()) {
                count++
            }
        }
        return count;
    }

    fun part2(input: List<String>): Int {
        var count = 0;
        for (i in 3..input.size - 1) {
            if (input[i].toInt() + input[i - 1].toInt() + input[i - 2].toInt() > input[i - 1].toInt() + input[i - 2].toInt() + input[i - 3].toInt()) {
                count++
            }
        }
        return count;
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01.test")
    check(part1(testInput) == 7)

    val input = readInput("Day01")
    prcp(part1(input))
    prcp(part2(input))
}
