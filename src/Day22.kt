class BoolMap3D() : HashMap<Pos3D, Boolean>();

fun main() {
    fun getRanges(line: String): List<IntRange> {
        val rangeStrings = line.split(" ")[1].split(",").map { it.split("=")[1] }
        return rangeStrings.map { it.split("..").map { it.toInt() } }.map { (start, end) -> IntRange(start, end) }
    }

    fun calcStateMap(input: List<String>, allowedRange: IntRange?): BoolMap3D {
        val state = BoolMap3D()
        for (line in input) {
            val ranges = getRanges(line)
            if (allowedRange != null && ranges.find {
                    it.first < -allowedRange.first || it.last > allowedRange.last
                } != null) {
                continue
            }
            for (x in ranges[0]) {
                for (y in ranges[1]) {
                    for (z in ranges[2]) {
                        state[Pos3D(x, y, z)] = line.startsWith("on")
                    }
                }
            }
        }
        return state
    }

    fun part1(input: List<String>): Long {
        val allowedRange: IntRange = -50..50
        val state = calcStateMap(input, allowedRange)
        return Math.pow(allowedRange.count().toDouble(), 3.0).toLong() - state.count { entry -> entry.value }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val day = 22
    println("Starting Day ${day}")
    val testInput = readInput("Day${day}.test")
    checkEquals(part1(testInput), 590784)
    val input = readInput("Day${day}")
    prcp(part1(input))
    checkEquals(part2(testInput), 0)
    prcp(part2(input))
}
