fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size * 2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day_.test")
    check(part1(testInput) == 1)

    val input = readInput("day_")
    prcp(part1(input))
    prcp(part2(input))
}
