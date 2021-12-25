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

    fun isUsed(allowedRange: IntRange?, ranges: Cube) = allowedRange == null || ranges.find {
        it.first < allowedRange.first || it.last > allowedRange.last
    } == null

    fun getNbOnNaive(cubeList: List<Cube>): Long {
        val state = BoolMap3D()
        for (ranges in cubeList) {
            for (x in ranges[0]) {
                for (y in ranges[1]) {
                    for (z in ranges[2]) {
                        state[Pos3D(x, y, z)] = ranges.type == CubeType.ON
                    }
                }
            }
        }
        return state.count { entry -> entry.value }.toLong()
    }

    fun getTotalVolume(allCubes: List<Cube>): Long {
        val lastCube = allCubes.last()
        val nonOverlapping = CubeList(lastCube)
        val masks = CubeList(lastCube)
        for (currCube in allCubes.subList(0, allCubes.size - 1).reversed()) {
            if (currCube.type == CubeType.ON) {
                val nonMaskedCubes = CubeList(currCube)
                for (mask in masks) {
                    for (nonMasked in nonMaskedCubes.toList()) {
                        nonMaskedCubes.remove(nonMasked)
                        nonMaskedCubes.addAll(nonMasked.subtract(mask))
                    }
                }
                nonOverlapping.addAll(nonMaskedCubes)
            }
            masks.add(currCube)
        }
        return nonOverlapping.sumOf { it.volume() }
    }

    fun part1(input: List<String>): Long {
        val allowedRange: IntRange = -50..50
        val cubeList = input.map { parseCube(it) }.filter { isUsed(allowedRange, it) }
        return getTotalVolume(cubeList)
    }

    fun part2(input: List<String>): Long {
        val allCubes = input.map { parseCube(it) }
        return getTotalVolume(allCubes)
    }

// test if implementation meets criteria from the description, like:
    val day = 22
    println("Starting Day ${day}")
    val testInput = readInput("Day${day}.test")
    checkEquals(part1(testInput), 590784)
    val input = readInput("Day${day}")
    prcp(part1(input))
    val testInput2 = readInput("Day${day}.test2")
//    checkEquals(part2(testInput2), 2758514936282235)
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
        val its = intersect(slicer)
        if (its.volume() == 0L) {
            return CubeList(this)
        }
        if (its.volume() == this.volume()) {
            return CubeList()
        }
        val filteredCubeList = CubeList(
            listOf(
                Cube(x, y.first..its.y.first - 1, z, type), //BOTTOM
                Cube(x, its.y.last + 1..y.last, z, type), //TOP
                Cube(x.first..its.x.first - 1, its.y, z, type), //LEFT
                Cube(its.x.last + 1..x.last, its.y, z, type), //RIGHT
                Cube(its.x, its.y, z.first..its.z.first - 1, type), //FRONT
                Cube(its.x, its.y, its.z.last + 1..z.last, type), //BACK
            ).filter { it.volume() > 0L }
        )
        check(filteredCubeList.size > 0)
        return filteredCubeList
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
    constructor(cubes: List<Cube>) : super(cubes)
    constructor(cube: Cube) : this(listOf(cube))
    constructor()
}
