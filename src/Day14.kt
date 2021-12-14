fun main() {
    fun doReplacements(
        nbReplacements: Int,
        replacements: List<List<String>>,
        currentString: String
    ): String {
        var resultString: String = currentString
        for (i in 0 until nbReplacements) {
            println("step " + i)
            for ((match, insert) in replacements) {
                do {
                    val startLength = resultString.length
                    resultString = resultString.replace(match, "${match[0]}_${insert}_${match[1]}")
                } while (resultString.length > startLength)
            }
            resultString = resultString.replace("_", "");
        }
        return resultString
    }

    fun toCharCountMap(resultString: String): Map<Char, Long> {
        val charMap = HashMap<Char, Long>()
        for (char in resultString) {
            charMap[char] = (charMap[char] ?: 0) + 1
        }
        return charMap
    }

    fun part1(input: List<String>): Long {
        var currentString = input[0]
        val replacements = input.subList(2, input.size).map { it.split(" -> ") }
        val nbReplacements = 10
        val resultString = doReplacements(nbReplacements, replacements, currentString)
        val charCounts = toCharCountMap(resultString)
        val sortedCounts = charCounts.values.sorted()
        return -sortedCounts.first() + sortedCounts.last()
    }

    fun part2(input: List<String>): Long {
        var currentString = input[0]
        val replacements = input.subList(2, input.size).map { it.split(" -> ") }
        val nbReplacements = 40
        val resultString = doReplacements(nbReplacements, replacements, currentString)
        val charCounts = toCharCountMap(resultString)
        val sortedCounts = charCounts.values.sorted()
        return -sortedCounts.first() + sortedCounts.last()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14.test")
    check(part1(testInput) == 1588.toLong())
    val input = readInput("Day14")
    prcp(part1(input))
    check(part2(testInput) == 2188189693529)
    prcp(part2(input))
}
