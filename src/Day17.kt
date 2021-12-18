import kotlin.math.abs

fun main() {
    fun calcIncreasingSum(maxVal: Int): Int {
        return maxVal * (maxVal + 1) / 2
    }

    fun findXVelocityForRangeSteps(range: IntRange, steps: Int): Int? {
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
                val matchingX = findXVelocityForRangeSteps(x, nbSteps)
                if (matchingX != null) {
                    return calcIncreasingSum(currVelYTry)
                }
            }
        }
        return -1
    }

    fun findYMatchesForRangeSteps(range: IntRange, steps: Int): Set<Int> {
        var matches = HashSet<Int>()
        var negativeSteps = steps
        for (velocity in range.first..-range.first) {
            var absVel = abs(velocity)
            if (velocity >= 0) {
                negativeSteps -= 2 * absVel + 1
            }
            if (negativeSteps < 0) {
                continue
            }
            var endPosition = -(absVel * negativeSteps + calcIncreasingSum(negativeSteps - 1))

            if (range.contains(endPosition)) {
                matches.add(velocity)
            }
        }
        return matches
    }

    fun findXMatchesForRangeSteps(range: IntRange, steps: Int): Set<Int> {
        var matches = HashSet<Int>()
        for (velocity in 0..range.last * range.last + 1) {
            val endPosition = velocity * Math.min(steps, velocity) - calcIncreasingSum(Math.min(steps, velocity) - 1)
            if (range.contains(endPosition)) {
                matches.add(endPosition);
            }
        }
        return matches
    }

    fun part2(x: IntRange, y: IntRange): Int {
        val allMatches = HashSet<String>()
        for (nbSteps in 1..100 * -y.first) {
            val matchingYs = findYMatchesForRangeSteps(y, nbSteps)
            if (matchingYs.isEmpty()) {
                continue
            }
            val matchingXs = findXMatchesForRangeSteps(x, nbSteps)
            if (matchingXs.isEmpty()) {
                continue
            }
            allMatches.addAll(matchingYs.map { y -> matchingXs.map { x -> "x${x}y${y}" } }.flatten())
        }
        return allMatches.size
    }

    // test if implementation meets criteria from the description, like:
    val xTestRange = 20..30
    val yTestRange = -10..-5
    checkEquals(part1(x = xTestRange, y = yTestRange), 45)
    val xRange = 257..286
    val yRange = -101..-57
    prcp(part1(x = xRange, y = yRange))
//    checkEquals(part2(x = xTestRange, y = yTestRange), 112)
    prcp(part2(x = xRange, y = yRange))
}
