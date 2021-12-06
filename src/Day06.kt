fun main() {

    fun iterateLife(lanternTtlCounters: LongArray, cycles: Int) {
        for (cycle in 0..cycles - 1) {
            val countersCopy = lanternTtlCounters.clone()
            for (ttl in 0..lanternTtlCounters.size - 1) {
                lanternTtlCounters[ttl] = countersCopy[(ttl + 1) % (lanternTtlCounters.size)]
            }

            lanternTtlCounters[6] += countersCopy[0]
        }
    }


    fun calculatePopulation(input: List<String>, cycles: Int): Long {
        val lanternTtls = input[0].split(",").map { it.toInt() }.toMutableList()
        val lanternTtlCounters = LongArray(9, { index -> lanternTtls.count { ttl -> ttl == index }.toLong() })
        iterateLife(lanternTtlCounters, cycles)
        return lanternTtlCounters.sum()
    }

    fun part1(input: List<String>): Int {
        return calculatePopulation(input, 80).toInt()
    }

    fun part2(input: List<String>): Long {
        return calculatePopulation(input, 256)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06.test")
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26984457539)

    val input = readInput("Day06")
    prcp(part1(input))
    prcp(part2(input))
}
