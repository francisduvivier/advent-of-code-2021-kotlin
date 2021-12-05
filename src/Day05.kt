import java.awt.geom.Line2D

fun toLine2D(inputLine: String): Line2D.Float {
    val regex = """(.+),(.+) -> (.+),(.+)""".toRegex()
    val matchResult = regex.find(inputLine)
    val (x1, y1, x2, y2) = matchResult!!.destructured
    return Line2D.Float(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat())
}

fun main() {

    fun part1(input: List<String>): Int {
        val lines: List<Line2D.Float> = input.map { toLine2D(it) }
        return input.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05.test")
    check(part1(testInput) == 1)

    val input = readInput("Day05")
    prcp(part1(input))
    prcp(part2(input))
}
