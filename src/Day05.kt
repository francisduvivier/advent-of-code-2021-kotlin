import java.awt.geom.Line2D

fun toLine2D(inputLine: String): Line2D.Float {
    val regex = """(.+),(.+) -> (.+),(.+)""".toRegex()
    val matchResult = regex.find(inputLine)
    val (x1, y1, x2, y2) = matchResult!!.destructured
    return Line2D.Float(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat())
}

fun isDiagonal(line: Line2D.Float): Boolean {
    return line.bounds.height != 0 && line.bounds.width != 0
}

private const val epsilon = 0.05

fun main() {

    fun findCrossingTiles(lines: List<Line2D.Float>): Int {
        val maxX = lines.map { Math.max(it.x1, it.x2).toInt() }.maxOrNull()!!
        val maxY = lines.map { Math.max(it.y1, it.y2).toInt() }.maxOrNull()!!
        val matrix = Array(maxY + 1, { IntArray(maxX + 1, { 0 }) })
        var overlaps = 0
        val rowCols = rowCols(maxY + 1, maxX + 1)
        for (line in lines) {
            for ((y, x) in rowCols) {
                if (line.intersects(x - epsilon, y - epsilon, 2 * epsilon, 2 * epsilon)) {
                    matrix[y][x]++
                    if (matrix[y][x] == 2) {
                        overlaps++;
                    }
                }
            }
        }
        return overlaps
    }

    fun part1(input: List<String>): Int {
        var lines: List<Line2D.Float> = input.map { toLine2D(it) }
        lines = lines.filter { !isDiagonal(it) }
        return findCrossingTiles(lines)
    }

    fun part2(input: List<String>): Int {
        val lines: List<Line2D.Float> = input.map { toLine2D(it) }
        return findCrossingTiles(lines)
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05.test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    prcp(part1(input))
    prcp(part2(input))
}
