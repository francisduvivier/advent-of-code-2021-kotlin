fun main() {
    fun compare1s(input: List<String>, i: Int): Int {
        val nbLines = input.size
        val nb1s = input.map({ it.split("")[i + 1].toInt() }).sum()
        val diff = 2 * nb1s - nbLines;
        return diff
    }

    fun part1(input: List<String>): Int {
        var gammaBits = ""
        var epsBits = ""
        val nbBits = input[0].length
        for (i in 0..nbBits - 1) {
            val diff = compare1s(input, i)
            gammaBits += if (diff > 0) "1" else "0";
            epsBits += if (diff > 0) "0" else "1";
        }
        return (gammaBits.toInt(2) * epsBits.toInt(2))
    }

    fun part2(input: List<String>): Int {
        var oxygenCandidates: List<String> = ArrayList(input);
        var co2scrubCandidates: List<String> = ArrayList(input);

        val nbBits = input[0].length
        for (i in 0..nbBits - 1) {
            if (oxygenCandidates.size <= 1 && co2scrubCandidates.size <= 1) {
                break;
            }
            val diffO = compare1s(oxygenCandidates, i);
            val diffC = compare1s(co2scrubCandidates, i);

            val mostCommon = if (diffO >= 0) "1" else "0"
            val leastCommon = if (diffC >= 0) "0" else "1"
            if (oxygenCandidates.size > 1) {
                oxygenCandidates = oxygenCandidates.filter({ it.get(i).toString().equals(mostCommon) })
            }
            if (co2scrubCandidates.size > 1) {
                co2scrubCandidates = co2scrubCandidates.filter({ it.get(i).toString().equals(leastCommon) })
            }
        }
        val oxleft = oxygenCandidates[0].toInt(2)
        return (oxleft * co2scrubCandidates[0].toInt(2))
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03.test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    prcp(part1(input))
    prcp(part2(input))
}
