import java.lang.Math.max
import java.lang.Math.min

class BoolMap3D : HashMap<Pos3D, Boolean>()

enum class CubeType {
    ON, OFF, OVERLAP
}

fun main() {
    fun parseCube(line: String): Cube {
        val rangeStrings = line.split(" ")[1].split(",").map { it.split("=")[1] }
        val rangeList =
            rangeStrings.map { it.split("..").map { it.toInt() } }.map { (start, end) -> IntRange(start, end) }
        return Cube(rangeList, if (line.startsWith("on")) CubeType.ON else CubeType.OFF);
    }

    fun calcStateMap(input: List<String>, allowedRange: IntRange?): BoolMap3D {
        val state = BoolMap3D()
        for (line in input) {
            val ranges = parseCube(line)
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

    fun calcNbOnNaive(input: List<String>): Long {
        val allowedRange: IntRange = -50..50
        val state = calcStateMap(input, allowedRange)
        return state.count { entry -> entry.value }.toLong()
    }

    fun part1(input: List<String>): Long {
        return calcNbOnNaive(input)
    }

    fun getTotalOverlap(cubesLeft: List<Cube>, currCube: Cube): Long {
        var totalOverlap = 0L;
        val countedOverlaps = CubeList()
        for (higherCube in cubesLeft.subList(1, cubesLeft.size)) {
            countedOverlaps.add(higherCube.intersect(currCube))
            totalOverlap += 1
        }
        return totalOverlap
    }

    fun countNbOn(input: List<String>): Long {
        val allCubes = input.map { parseCube(it) }
        val lastCube = allCubes.last()
        val nonOverlapping = CubeList(lastCube)
        val masks = CubeList(lastCube)
        for (currCube in allCubes.subList(0, allCubes.size - 1).reversed()) {
            if (currCube.type == CubeType.ON) {
                var nonMaskedCubes = CubeList(currCube)
                for (mask in masks) {
                    for (nonMasked in nonMaskedCubes.toList()) {
                        nonMaskedCubes.remove(nonMasked)
                        nonMaskedCubes.addAll(nonMasked.subtract(nonMasked))
                    }
                }
                nonOverlapping.addAll(nonMaskedCubes)
            }
            masks.add(currCube)
        }
        return nonOverlapping.sumOf { it.volume() }
    }

    fun part2(input: List<String>): Long {
        return countNbOn(input)
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


class Cube(rangeList: List<IntRange>, val type: CubeType) : ArrayList<IntRange>(rangeList) {

    val x = this[0]
    val y = this[1]
    val z = this[2]
    private val volume = map { it.count().toLong() }.reduce { acc, curr -> acc * curr }

    fun volume(): Long {
        return volume
    }

    constructor(
        xRange: IntRange,
        yRange: IntRange,
        zRange: IntRange,
        type: CubeType
    ) : this(listOf(xRange, yRange, zRange), type)

    override fun toString(): String {
        return this.joinToString(" ; ")
    }

    fun subtract(slicer: Cube): CubeList {
        val intersection = intersect(slicer)
        if (intersection.volume() == 0L) {
            return CubeList(this)
        }
        if (intersection.volume() == this.volume()) {
            return CubeList()
        }
        return TODO()
    }

    fun intersect(other: Cube): Cube {
        val startX = max(this.x.first, other.x.first)
        val startY = max(this.y.first, other.y.first)
        val startZ = max(this.z.first, other.z.first)
        return Cube(
            startX..min(this.x.last, other.x.last),
            startY..min(this.y.last, other.y.last),
            startZ..min(this.z.last, other.z.last),
            CubeType.OVERLAP
        )
    }
}

class CubeList : ArrayList<Cube> {
    constructor(cube: Cube) : super(listOf(cube))
    constructor()
}
