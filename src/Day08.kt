fun main() {
    fun part1(input: List<String>): Int {
        val output = input.map {
            it.split(" | ")[1].split(" ")
                .filter { out -> arrayOf(2, 3, 4, 7).indexOf(out.length) != -1 }
                .count()

        }.sum();
        return output;
    }

    val segmentsMap = arrayOf("abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdfeg", "acf", "abcdefg", "abcdfg")


    fun toChars(it: String) = it.toCharArray().toList()

    fun part2(input: List<String>): Int {
        val foundNumbers = Array<Int?>(10, { null })
        var outputSum = 0;
        for (inputLine in input) {
            val neededNumbers = inputLine.split(" | ")[1].split(" ")
            val remainingNumbers = inputLine.split(" | ")[0].split(" ").toMutableList()
            val number4 = remainingNumbers.find({ it.length == 4 })!!
            remainingNumbers.remove(number4)
            val number7 = remainingNumbers.find({ it.length == 3 })!!
            remainingNumbers.remove(number7)
            val number8 = remainingNumbers.find({ it.length == 7 })!!
            remainingNumbers.remove(number8)
            val number1 = remainingNumbers.find({ it.length == 2 })!!
            remainingNumbers.remove(number1)
            val number9 = remainingNumbers.find({ it.length == 6 && toChars(it).containsAll(toChars(number4)) })!!
            remainingNumbers.remove(number9)
            val number0 = remainingNumbers.find({ it.length == 6 && toChars(it).containsAll(toChars(number1)) })!!
            remainingNumbers.remove(number0)
            val number6 = remainingNumbers.find({ it.length == 6 })!!
            remainingNumbers.remove(number6)
            val number5 = remainingNumbers.find({ it.length == 5 && toChars(number6).containsAll(toChars(it)) })
            remainingNumbers.remove(number5)
            val number3 = remainingNumbers.find({ it.length == 5 && toChars(number9).containsAll(toChars(it)) })
            remainingNumbers.remove(number3)
            val number2 = remainingNumbers.get(0);
            remainingNumbers.remove(number2);
            val numbers =
                arrayOf(number0, number1, number2, number3, number4, number5, number6, number7, number8, number9)

            val numberTranslations =
                neededNumbers.map { needed -> numbers.indexOf(numbers.find { toChars(needed).containsAll(toChars(it!!)) && needed.length == it.length }) }
            outputSum += numberTranslations.map { it.toString() }.joinToString("").toInt()
        }
        return outputSum
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08.test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    prcp(part1(input))
    prcp(part2(input))
}
