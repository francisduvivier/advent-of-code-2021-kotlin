typealias SN = PairOrNumber

fun main() {
    fun part1(input: List<String>): Long {
        var result = parseSnails(input[0])

        for (line in input.subList(1, input.size)) {
            val newParent = SN()
            result.parent = newParent
            val newRight = parseSnails(line)
            newRight.parent = newParent
            newParent.left = result
            newParent.right = newRight
            result = newParent
            result.reduce()
        }
        return result.sum()
    }

    fun part2(input: List<String>): Long {
        var max = 0L
        for (first in input) {
            for (second in input) {
                if (first == second) {
                    continue
                }
                val newVal = part1(listOf(first, second))
                if (newVal > max) {
                    max = newVal
                }
            }
        }
        return max
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18.test")
    checkEquals(SN(SN(SN(6, 6), SN(7, 6)), 9).toString(), "[[[6,6],[7,6]],9]")
    checkEquals(parseSnails("[[[6,6],[7,6]],9]").toString(), "[[[6,6],[7,6]],9]")
    val testReduceResult = "[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]"
    checkEquals(parseSnails(testReduceResult).toString(), testReduceResult)
    checkEquals(parseSnails(testReduceResult).sum(), 4140)
    val snails = parseSnails("[[[[[9,8],1],2],3],4]")
    checkEquals(snails.tryExplode(), true)
    checkEquals(snails.toString(), "[[[[0,9],2],3],4]")
//    checkEquals(addSnailLines(testInput).reduce().toString(), testReduceResult)

    val snails2 = parseSnails("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]")
    checkEquals(snails2.tryExplode(), true)
    checkEquals(snails2.toString(), "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
    val snails3 = parseSnails("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
    checkEquals(snails3.tryExplode(), true)
    checkEquals(snails3.toString(), "[[3,[2,[8,0]]],[9,[5,[7,0]]]]")
    println("---------------------------------------------------")
    checkEquals(
        part1(listOf("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]")),
        parseSnails("[[[[3,0],[5,3]],[4,4]],[5,5]]").sum()
    )
    checkEquals(
        part1(listOf("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]", "[6,6]")),
        parseSnails("[[[[5,0],[7,4]],[5,5]],[6,6]]").sum()
    )
    checkEquals(part1(testInput), 4140)

    val input = readInput("Day18")
    prcp(part1(input))
    checkEquals(part2(testInput), 3993)
    prcp(part2(input))
}

open class PairOrNumber(
    var left: SN? = null,
    var right: SN? = null,
    var number: Int? = null,
    var parent: SN? = null,
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
//        println("trySplit: " + this.toString())
        if (number != null && number!! >= 10) {
//            println("splitting" + this.toString())
            this.left = SN(number = number!! / 2, parent = this)
            this.right = SN(number = (number!! + 1) / 2, parent = this)
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
        while (this.tryExplode() || this.trySplit()) {
        }
        return this
    }

    fun tryExplode(nbParents: Int = 0): Boolean {
//        println("tryExplode " + this.toString())
        if (this.number != null) {
            return false
        }
        if (nbParents == 4 && number == null) {
//            println("exploding " + this.toString())
            check(right!!.number != null)
            check(left!!.number != null)
            val firstRight = this.getFirstRight()
            if (firstRight?.number != null) {
                firstRight.number = firstRight.number!! + this.right!!.number!!
            }
            val firstLeft = this.getFirstLeft()
            if (firstLeft?.number != null) {
                firstLeft.number = firstLeft.number!! + this.left!!.number!!
            }
            number = 0
            left = null
            right = null
            return true
        }
        return this.left!!.tryExplode(nbParents + 1) || this.right!!.tryExplode(nbParents + 1)
    }

    fun getFirstLeft(): SN? {
        if (parent?.right == this) {
            return parent?.left?.rightMost()
        } else {
            return parent?.getFirstLeft()
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
            return parent?.right?.leftMost()
        } else {
            return parent?.getFirstRight()
        }
    }

    fun leftMost(): SN? {
        if (number != null) {
            return this;
        }
        return left?.leftMost()
    }
}

class SnailBuilder(parent: SnailBuilder?, var currFirst: Boolean = true) : SN(parent = parent) {
    private var done: Boolean = false

    fun recurse(): SnailBuilder {
        val newBuilder = SnailBuilder(parent = this)
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
        val child = SnailBuilder(parent = this)
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
            ']' -> currPair = (currPair.parent as SnailBuilder?)!!
            ',' -> {} //do nothing
            else -> currPair.setNumber(char)
        }
    }
    check(currPair == outerPair)
    return outerPair
}