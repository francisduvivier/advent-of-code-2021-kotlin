typealias SN = PairOrNumber

fun main() {
    fun part1(input: List<String>): Long {
        var result = addSnailLines(input.subList(0, 1))
        for (line in input.subList(1, input.size)) {
            result = SN(result, parseSnails(line))
            result.reduce()
        }
        return result.sum()
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
//    checkEquals(addSnailLines(testInput).reduce().toString(), testReduceResult)
    checkEquals(part1(testInput), 4140)

    val input = readInput("Day18")
    prcp(part1(input))
    checkEquals(part2(testInput), 0)
    prcp(part2(input))
}

open class PairOrNumber(
    var left: SN? = null,
    var right: SN? = null,
    var number: Int? = null,
    val parent: SnailBuilder? = null,
) {
    constructor(numberOnly: Int) : this(number = numberOnly)

    constructor(firstOnly: Int, secondOnly: Int) : this(SN(firstOnly), SN(secondOnly))

    constructor(first: Int, second: SN) : this(SN(first), second)

    constructor(first: SN, second: Int) : this(first, SN(second))

    override fun toString(): String {
        if (number != null) {
            return number.toString()
        } else {
            return "[${left.toString()},${right.toString()}]"
        }
    }

    fun trySplit(): Boolean {
        println("trySplit: " + this.toString())
        if (number != null && number!! >= 10) {
            println("splitting" + this.toString())
            this.left = SN(number!! / 2)
            this.right = SN(Math.ceil(number!!.toDouble() / 2).toInt())
            this.number = null
            return true;
        } else if (number == null) {
            return this.left!!.trySplit() || this.right!!.trySplit()
        }
        return false
    }

    fun sum(): Long {
        if (number != null) {
            return number!!.toLong()
        } else {
            return 3 * left!!.sum() + 2 * right!!.sum()
        }
    }

    fun reduce(): SN {
        while (this.tryExplode(0) || this.trySplit()) {
        }
        return this
    }

    private fun tryExplode(nbParents: Int): Boolean {
        println("tryExplode " + this.toString())
        if (this.number != null) {
            return false
        }
        if (nbParents == 4 && number == null) {
            check(right!!.number != null)
            check(left!!.number != null)
            val firstRight = this.getFirstRight()
            if (firstRight?.number != null) {
                firstRight.number = firstRight.number!! + this.right!!.number!!
            }
            val firstLeft = this.getFirstLeft()
            if (firstLeft?.number != null) {
                firstLeft.number = firstLeft.number!! + this.right!!.number!!
            }
            number = 0
            left = null
            right = null
//            println("exploding " + this.toString())
            return true
        }
        return this.left!!.tryExplode(nbParents + 1) || this.right!!.tryExplode(nbParents + 1)
    }

    fun getFirstLeft(): SN? {
        if (parent?.right == this) {
            return parent.getFirstLeft()
        } else {
            return parent?.rightMost()
        }
    }

    fun rightMost(): SN? {
        if (number != null) {
            return this;
        }
        return right?.rightMost()
    }

    fun getFirstRight(): SN? {
        if (parent?.left == this) {
            return parent.getFirstRight()
        } else {
            return parent?.leftMost()
        }
    }

    fun leftMost(): SN? {
        if (number != null) {
            return this;
        }
        return left?.leftMost()
    }


    private fun addFirstRight(right: SN) {
        TODO("Not yet implemented")
    }

    private fun rightNumber(): Int {
        TODO("Not yet implemented")
    }

}

fun addSnailLines(testInput: List<String>): SN {
    var addedSnails = parseSnails(testInput[0])
    for (line in testInput.subList(1, testInput.size)) {
        addedSnails = SN(addedSnails, parseSnails(line))
    }
    return addedSnails
}


class SnailBuilder(parent: SnailBuilder?, var currFirst: Boolean = true) : SN(parent = parent) {
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
            this.left = newBuilder
            currFirst = false
        } else {
            this.right = newBuilder
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