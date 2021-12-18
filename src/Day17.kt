import kotlin.math.abs

fun main() {
    fun calcIncreasingSum(maxVal: Int): Int {
        return maxVal * (maxVal + 1) / 2
    }


    fun findAllMatchingVelocities(
        xRange: IntRange,
        yRange: IntRange
    ): HashMap<String, Pair<Int, Int>> {
        val allMatches = HashMap<String, Pair<Int, Int>>()
        for (testX in 0..xRange.last) {
            for (testY in yRange.first..-yRange.first) {
                var currXVel = testX
                var currYVel = testY
                var currXPos = 0
                var currYPos = 0
                for (steps in 1..3 * -yRange.first) {
                    currXPos += currXVel
                    currYPos += currYVel
                    if (currXVel > 0) {
                        currXVel -= 1
                    }
                    currYVel -= 1
                    if (currXPos in xRange && currYPos in yRange) {
                        allMatches.set("${testX},${testY}", Pair(testX, testY))
                        continue
                    } else if (currXPos > xRange.last || currYPos < yRange.first) {
                        continue
                    }
                }
            }
        }
        return allMatches
    }

    fun part1(x: IntRange, y: IntRange): Int {
        return findAllMatchingVelocities(x, y).values.map { calcIncreasingSum(it.second) }.maxOrNull()!!
    }

    fun part2(xRange: IntRange, yRange: IntRange): Int {
        val allMatches = findAllMatchingVelocities(xRange, yRange)
        return allMatches.size
    }

    // test if implementation meets criteria from the description, like:
    val xTestRange = 20..30
    val yTestRange = -10..-5
    checkEquals(part1(x = xTestRange, y = yTestRange), 45)
    val xRange = 257..286
    val yRange = -101..-57
    prcp(part1(x = xRange, y = yRange))
    checkEquals(part2(xRange = xTestRange, yRange = yTestRange), 112)
    prcp(part2(xRange = xRange, yRange = yRange))
}
