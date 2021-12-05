import java.awt.geom.Line2D

fun toLine2D(inputLine: String): Line2D.Float {
    val regex = """(.+),(.+) -> (.+),(.+)""".toRegex()
    val matchResult = regex.find(inputLine)
    val (x1, y1, x2, y2) = matchResult!!.destructured
    return Line2D.Float(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat())
}

fun isDiagonal(line: Line2D.Float): Boolean {
    return line.bounds.height == 0 || line.bounds.width == 0
}

fun main() {

    fun part1(input: List<String>): Int {
        val lines: List<Line2D.Float> = input.map { toLine2D(it) }
        val nonDiagonalLines = lines.filter { !isDiagonal(it) }
        val maxX = lines.map { Math.max(it.x1, it.x2).toInt() }.maxOrNull()!!
        val maxY = lines.map { Math.max(it.y1, it.y2).toInt() }.maxOrNull()!!
        val matrix = Array(maxY, { IntArray(maxX, { 0 }) })
        var overlaps = 0
        for (line in nonDiagonalLines) {
            for (y in 0..maxY - 1) {
                for (x in 0..maxX - 1) {
                    if (line.contains(x + 0.5, y + 0.5, 1.0, 1.0)) {
                        matrix[y][x]++
                        if (matrix[y][x] == 1) {
                            overlaps++;
                        }
                    }
                }
            }
        }
        return overlaps
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05.test")
    check(part1(testInput) == 5)

    val input = readInput("Day05")
    prcp(part1(input))
    prcp(part2(input))
}
