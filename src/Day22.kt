class BoolMap3D() : HashMap<Pos3D, Boolean>() {
    fun countNbOn(): Int {
        TODO("Not yet implemented")
    }
};

fun main() {
    fun getRanges(line: String): List<IntRange> {
        val rangeStrings = line.split(" ")[1].split(",").map { it.split("=")[1] }
        return rangeStrings.map { it.split("..").map { it.toInt() } }.map { (start, end) -> IntRange(start, end) }
    }

    fun calcStateMap(input: List<String>, allowedRange: IntRange?): BoolMap3D {
        val state = BoolMap3D()
        for (line in input) {
            val ranges = getRanges(line)
            if (allowedRange != null && ranges.find {
                    it.first < allowedRange.first || it.last > allowedRange.last
                } != null) {
                continue
            }
            for (x in ranges[0]) {
                for (y in ranges[1]) {
                    for (z in ranges[2]) {
                        state[Pos3D(x, y, z)] = line.startsWith("on")
                    }
                }
            }
        }
        return state
    }

    fun part1(input: List<String>): Long {
        val allowedRange: IntRange = -50..50
        val state = calcStateMap(input, allowedRange)
        return state.count { entry -> entry.value }.toLong()
    }


    fun hasOverLap(a: IntRange, b: IntRange): Boolean {
        return b.first <= a.first && a.first <= b.last || b.first <= a.last && a.last <= b.last
    }

    fun hasOverLap(a: List<IntRange>, b: List<IntRange>): Boolean {
        return a.mapIndexed { index, value -> hasOverLap(value, b[index]) }.all { it }
    }

    fun deleteRangeFrom(alreadyAdded: Range3D, newRange: Range3D): List<Range3D> {
        if (!hasOverLap(alreadyAdded, newRange)) {
            return listOf(newRange)
        }

        return TODO("IMPL")
    }

    fun calcRangeStateMap(input: List<String>): RangeStateMap {
        val addedRanges = RangeStateMap()
        var i = 0;
        for (line in input) {
            println("line [${i++}][${line}]")
            val isOn = line.startsWith("on")
            val newRangeFromLine = getRanges(line)
            if (addedRanges.isEmpty()) {
                if (isOn) {
                    addedRanges.add(newRangeFromLine)
                }
                continue
            }
            if (isOn) {
                val newRanges = arrayListOf(newRangeFromLine)
                for (alreadyAdded in addedRanges.toList()) {
                    for (newRange in newRanges.toList()) {
                        newRanges.remove(newRange)
                        newRanges.addAll(deleteRangeFrom(alreadyAdded, newRange))
                    }
                }
                addedRanges.addAll(newRanges)
                println("addedRanges size [${addedRanges.size}]")
            } else {
                addedRanges.toList().forEach { alreadyAdded ->
                    run {
                        addedRanges.remove(alreadyAdded)
                        addedRanges.addAll(deleteRangeFrom(newRangeFromLine, alreadyAdded))
                    }
                }
            }
        }
        return addedRanges
    }

    fun part2(input: List<String>): Long {
        val state = calcRangeStateMap(input)
        return state.countNbOn()
    }

    // test if implementation meets criteria from the description, like:
    val day = 22
    println("Starting Day ${day}")
    val testInput = readInput("Day${day}.test")
    checkEquals(part1(testInput), 590784)
    val input = readInput("Day${day}")
    prcp(part1(input))
    val testInput2 = readInput("Day${day}.test2")
    checkEquals(part2(testInput2), 2758514936282235)
    prcp(part2(input))
}

typealias Range3D = List<IntRange>

class RangeStateMap : ArrayList<Range3D>() {
    fun countNbOn(): Long {
        return map { r3d -> r3d.map { it.count().toLong() }.reduce { acc, curr -> acc * curr } }.sum()
    }

}
