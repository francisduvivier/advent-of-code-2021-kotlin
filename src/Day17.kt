fun main() {

    fun part1(x: IntRange, y: IntRange): Int {
        return 1
    }

    fun part2(x: IntRange, y: IntRange): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val xTestRange = 20..30
    val yTestRange = -10..-5
    checkEquals(part1(x = xTestRange, y = yTestRange), 45)
    val xRange = 257..286
    val yRange = -101..-57
    prcp(part1(x = xRange, y = yRange))
    checkEquals(part2(x = xTestRange, y = yTestRange), 0)
    prcp(part2(x = xRange, y = yRange))
}
