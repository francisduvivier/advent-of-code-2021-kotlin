fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16.test")

    checkEquals(part1(listOf("8A004A801A8002F478")), 16)
    checkEquals(part1(listOf("620080001611562C8802118E34")), 12)
    checkEquals(part1(listOf("C0015000016115A2E0802F182340")), 23)
    checkEquals(part1(listOf("A0016C880162017C3686B18A3D4780")), 31)

    val input = readInput("Day16")
    prcp(part1(input))
    prcp(part2(input))
}
