fun main() {
    fun calcIncreasingSum(maxVal: Int): Int {
        return maxVal * (maxVal + 1) / 2
    }

    fun findVelocityForRangeSteps(range: IntRange, steps: Int): Int? {
        for (velocity in 0..range.last) {
            val endPosition = velocity * Math.min(steps, velocity) - calcIncreasingSum(Math.min(steps, velocity) - 1)
            if (range.contains(endPosition)) {
                return velocity
            }
        }
        return null
    }

    fun part1(x: IntRange, y: IntRange): Int {

        for (currVelYTry in -y.first - 1..-y.first - 1) {
            println("currVelYTry: $currVelYTry")
            for (currEndYTry in y) {
                val nbSteps = 2 * currVelYTry + 1
                val matchingX = findVelocityForRangeSteps(x, nbSteps)
                if (matchingX != null) {
                    return calcIncreasingSum(currVelYTry)
                }
            }
        }
        return -1
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
