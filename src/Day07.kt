import kotlin.math.roundToInt

fun main() {
    fun part1(input: List<String>): Int {
        val outputs = input[0].split(",").map { it.toInt() }.toIntArray()
        outputs.sort();
        var minSum = -1

        val lowest = outputs.first()
        for (i in lowest..outputs.last()) {
            val summy = outputs.map { Math.abs(it - i) }.sum()
            if (minSum == -1 || summy < minSum) {
                minSum = summy;
            }
        }
        return minSum
    }

    fun part2(input: List<String>): Int {

        val outputs = input[0].split(",").map { it.toInt() }.toIntArray()
        outputs.sort();
        var minSum = -1

        val lowest = outputs.first()
        for (i in lowest..outputs.last()) {
            val summy = outputs.map { ((Math.abs(it - i)) * (Math.abs(it - i) + 1) / 2) }.sum()
            if (minSum == -1 || summy < minSum) {
                minSum = summy;
            }
        }
        return minSum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07.test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    prcp(part1(input))
    prcp(part2(input))
}
