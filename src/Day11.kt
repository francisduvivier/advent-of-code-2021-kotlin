fun main() {
    fun part1(input: List<String>): Long {
        return input.size.toLong()
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11.test")
    check(part1(testInput) == 1.toLong())

    val input = readInput("Day11")
    prcp(part1(input))
    prcp(part2(input))
}
