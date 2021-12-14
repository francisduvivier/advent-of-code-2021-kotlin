typealias CountMap = HashMap<Char, Long>

typealias CountForStepsMap = HashMap<Int, CountMap>

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

    fun toCharCountMap(resultString: String): CountMap {
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

    fun addCharCounts(charCounts: CountMap?, partCharMap: CountMap?): CountMap {
        val resultMap = CountMap(charCounts ?: CountMap())
        partCharMap?.forEach { (key, value) ->
            resultMap[key] = (resultMap[key] ?: 0) + value
        }
        return resultMap
    }

    fun doReplacementsRec(
        replacementMap: Map<String, Char>,
        pairToCountForStepsMap: HashMap<String, CountForStepsMap>,
        pair: String,
        nbReplacements: Int
    ): CountMap? {
        val cached = pairToCountForStepsMap[pair]?.get(nbReplacements)
        if (cached != null) {
            return cached
        }
        if (replacementMap[pair] == null || nbReplacements == 0) {
            return null
        }
        val insert = replacementMap[pair]!!
        pairToCountForStepsMap[pair] = pairToCountForStepsMap[pair] ?: CountForStepsMap()

        val resultCount = addCharCounts(
            doReplacementsRec(replacementMap, pairToCountForStepsMap, pair[0] + insert.toString(), nbReplacements - 1),
            doReplacementsRec(replacementMap, pairToCountForStepsMap, insert.toString() + pair[1], nbReplacements - 1)
        )
        resultCount[insert] = (resultCount[insert] ?: 0.toLong()) + 1;
        pairToCountForStepsMap[pair]!![nbReplacements] = resultCount
        return resultCount
    }

    fun solvePolymerization(input: List<String>, nbReplacements: Int): Long {
        val startString = input[0]
        val replacements =
            input.subList(2, input.size).map { it.split(" -> ") }.groupBy({ it[0] }, { it[1] })
                .mapValues { (_, value) -> value[0][0] }
        val pairToCountForStepsMap = HashMap<String, CountForStepsMap>()
        val pairs = startString.windowed(2, 1)
        var charCounts = toCharCountMap(startString)
        for (pair in pairs) {
            val partCharMap = doReplacementsRec(replacements, pairToCountForStepsMap, pair, nbReplacements)
            charCounts = addCharCounts(charCounts, partCharMap)
        }
        val sortedCounts = charCounts.values.sorted()
        return -sortedCounts.first() + sortedCounts.last()
    }

    fun part2(input: List<String>): Long {
        return solvePolymerization(input, 40)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14.test")
    check(part1(testInput) == 1588.toLong())
    check(solvePolymerization(testInput, 10) == 1588.toLong())
    val input = readInput("Day14")
    prcp(part1(input))
    check(part2(testInput) == 2188189693529)
    prcp(part2(input))
}
