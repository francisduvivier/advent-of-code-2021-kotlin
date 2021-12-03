fun main() {
    fun part1(input: List<String>): Int {
        var gammaBits = ""
        var epsBits = ""
        val nbBits = input[0].length
        val nbLines = input.size
        for (i in 0..nbBits - 1) {
            val nb1s = input.map({ it.split("")[i + 1].toInt() }).sum()
            gammaBits += if (nb1s > nbLines - nb1s) "1" else "0";
            epsBits += if (nb1s > nbLines - nb1s) "0" else "1";
        }
        return (gammaBits.toInt(2) * epsBits.toInt(2))
    }

    fun part2(input: List<String>): Int {
        var oxgenBits = "";
        var co2scrubBits = ""

        return (oxgenBits.toInt(2) * co2scrubBits.toInt(2))
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03.test")
    check(part1(testInput) == 198)

    val input = readInput("Day03")
    prcp(part1(input))
    prcp(part2(input))
}
