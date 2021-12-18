typealias SN = PairOrNumber

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18.test")
    checkEquals(SN(SN(SN(6, 6), SN(7, 6)), 9).toString(), "[[[6,6],[7,6]],9]")
    checkEquals(parseSnails("[[[6,6],[7,6]],9]").toString(), "[[[6,6],[7,6]],9]")
    val testReduceResult = "[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]"
    checkEquals(parseSnails(testReduceResult).toString(), testReduceResult)
    checkEquals(addSnailLines(listOf("[1,2]", "[[3,4],5]")).toString(), "[[1,2],[[3,4],5]]")
    checkEquals(parseSnails(testReduceResult).sum(), 4140)
    checkEquals(reduceSnails(addSnailLines(testInput)).toString(), testReduceResult)
    checkEquals(part1(testInput), 4140)

    val input = readInput("Day18")
    prcp(part1(input))
    checkEquals(part2(testInput), 0)
    prcp(part2(input))
}

fun reduceSnails(addSnailLines: SN): Any {
    TODO("Not yet implemented")
}

open class PairOrNumber(var first: SN? = null, var second: SN? = null, var number: Int? = null) {
    constructor(numberOnly: Int) : this(number = numberOnly)

    constructor(firstOnly: Int, secondOnly: Int) : this(SN(firstOnly), SN(secondOnly))

    constructor(first: Int, second: SN) : this(SN(first), second)

    constructor(first: SN, second: Int) : this(first, SN(second))

    override fun toString(): String {
        if (number != null) {
            return number.toString()
        } else {
            return "[${first.toString()},${second.toString()}]"
        }
    }

    fun sum(): Long {
        if (number != null) {
            return number!!.toLong()
        } else {
            return 3 * first!!.sum() + 2 * second!!.sum()
        }
    }

}

fun addSnailLines(testInput: List<String>): SN {
    var addedSnails = parseSnails(testInput[0])
    for (line in testInput.subList(1, testInput.size)) {
        addedSnails = SN(addedSnails, parseSnails(line))
    }
    return addedSnails
}


class SnailBuilder(val parent: SnailBuilder?, var currFirst: Boolean = true) : SN() {
    private var done: Boolean = false

    fun recurse(): SnailBuilder {
        val newBuilder = SnailBuilder(this)
        setChild(newBuilder)
        return newBuilder;
    }

    private fun setChild(newBuilder: SnailBuilder) {
        if (done) {
            throw Error("trying to change a finished snailbuilder")
        }
        if (currFirst) {
            this.first = newBuilder
            currFirst = false
        } else {
            this.second = newBuilder
            this.done = true;
        }
    }

    fun setNumber(char: Char) {
        val child = SnailBuilder(this)
        child.number = char.digitToInt()
        setChild(child)
    }

}

fun parseSnails(s: String): SN {
    val outerPair = SnailBuilder(null)
    var currPair = outerPair
    for (char in s.substring(1, s.length - 1).toCharArray()) {
        when (char) {
            '[' -> {
                currPair = currPair.recurse()
            }
            ']' -> currPair = currPair.parent!!
            ',' -> {} //do nothing
            else -> {
                currPair.setNumber(char)
            }
        }
    }
    check(currPair == outerPair)
    return outerPair
}