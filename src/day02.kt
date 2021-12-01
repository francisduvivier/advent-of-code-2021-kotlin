fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02.test")
    check(part1(testInput) == 1)

    val input = readInput("day02")
    prcp(part1(input))
    prcp(part2(input))
}
