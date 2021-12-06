fun main() {
    fun iterateOnce(lanterns: MutableList<Int>) {
        for (lanternI in 0..lanterns.size - 1) {
            lanterns[lanternI]--;
            if (lanterns[lanternI] == -1) {
                lanterns[lanternI] = 6
                lanterns.add(8)
            }
        }
    }

    fun iterateLife(startLanterns: MutableList<Int>, cycles: Int) {
        for (i in 0..cycles - 1) {
            iterateOnce(startLanterns)
        }
    }

    fun part1(input: List<String>): Int {
        val lanternTtls = input[0].split(",").map { it.toInt() }.toMutableList()
        iterateLife(lanternTtls, 80)
        return lanternTtls.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06.test")
    check(part1(testInput) == 5934)

    val input = readInput("Day06")
    prcp(part1(input))
    prcp(part2(input))
}
