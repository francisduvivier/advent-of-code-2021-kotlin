fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val day = 0
    println("Starting Day${day}")
    val testInput = readInput("Day${day}.test")
    checkEquals(part1(testInput), 1)
    val input = readInput("Day${day}")
    prcp(part1(input))
    checkEquals(part2(testInput), 0)
    prcp(part2(input))
}
