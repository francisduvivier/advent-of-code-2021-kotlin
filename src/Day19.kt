import kotlin.math.absoluteValue

class Pos3D(val x: Int, val y: Int, val z: Int) {
    constructor(list: List<Int>) : this(list[0], list[1], list[2])

    val all = listOf(x, y, z)
    override fun toString(): String {
        return all.joinToString(",")
    }

    fun diff(other: Pos3D): Pos3D {
        return Pos3D(all.mapIndexed { index, it -> it - other.all[index] })
    }

    fun orient(orientation: Orientation): Pos3D {
        return orientation.mutatePos(this);
    }

    fun add(other: Pos3D): Pos3D {
        return Pos3D(all.mapIndexed { index, it -> it + other.all[index] })
    }

    override fun hashCode(): Int {
        return this.toString().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        try {
            val otherPos = other as Pos3D
            return otherPos.diff(this).all.all { it == 0 }
        } catch (e: Error) {
            return false
        }
    }

    fun manHattan(): Int {
        return all.map { it.absoluteValue }.sum()
    }
}

class RawScannerData(val id: Int) : ArrayList<Pos3D>() {
    override fun toString(): String {
        return "id [${id}]"
    }
}

class Orientation(val direction: Facing, val rotation: Rotation) {
    fun mutatePos(pos3D: Pos3D): Pos3D {
        return rotation.mutatePos(direction.mutatePos(pos3D))
    }
}

class MappedScanner(val rawScannerData: RawScannerData, val orientation: Orientation, val position: Pos3D) {
    val normalizedScannerData = rawScannerData.map { normalizeCoords(it, orientation, position) }
    val normalizedScannerIds = normalizedScannerData.map { it.toString() }
    override fun toString(): String {
        return "id [${rawScannerData.id}] points [${normalizedScannerIds.joinToString(";")}]"
    }
}

enum class Rotation(val xMult: Int, val yMult: Int, val zMult: Int = 1) {
    R1(1, 1), R2(1, -1), R3(-1, 1), R4(-1, -1),
    R5(1, 1, -1), R6(1, -1, -1), R7(-1, 1, -1), R8(-1, -1, -1);

    fun mutatePos(pos: Pos3D): Pos3D {
        return Pos3D(pos.x * xMult, pos.y * yMult, pos.z * zMult)
    }
}

enum class Facing(val mutator: (Pos3D) -> Pos3D) {
    F1({ Pos3D(it.x, it.y, it.z) }),
    F2({ Pos3D(it.x, it.z, it.y) }),
    F3({ Pos3D(it.z, it.x, it.y) }),
    F4({ Pos3D(it.z, it.y, it.x) }),
    F5({ Pos3D(it.y, it.x, it.z) }),
    F6({ Pos3D(it.y, it.z, it.x) });

    fun mutatePos(pos: Pos3D): Pos3D {
        return mutator(pos)
    }
}

val allOrientations = Facing.values().map { dir3d -> Rotation.values().map { Orientation(dir3d, it) } }.flatten()


private fun normalizeCoords(
    rawBeacon: Pos3D,
    orientation: Orientation,
    scannerPos: Pos3D
): Pos3D {
    val orientedCoords = rawBeacon.orient(orientation)
    val mappedCoords = orientedCoords.add(scannerPos)
    return mappedCoords
}

fun main() {

    fun getScannerPositionWith12Matches(orientedRawScanner: List<Pos3D>, mappedScanner: MappedScanner): Pos3D? {
        val scannerMap = HashMap<Pos3D, Int>()
        for (newBeacon in orientedRawScanner) {
            for (mappedBeacon in mappedScanner.normalizedScannerData) {
                val newScannerPos = mappedBeacon.diff(newBeacon)
                scannerMap[newScannerPos] = (scannerMap[newScannerPos] ?: 0) + 1
                if (scannerMap[newScannerPos]!! >= 12) {
                    return newScannerPos
                }
            }
        }
        return null
    }


    fun tryMapScanner(rawScanner: RawScannerData, mappedScanner: MappedScanner): MappedScanner? {
//        println("try map scanner [$mappedScanner]")
//        println("on scanner [$rawScanner]")
        for (orientation in allOrientations) {
            val orientedRawScanner = MappedScanner(rawScanner, orientation, Pos3D(0, 0, 0)).normalizedScannerData
            val matchedScannerPos = getScannerPositionWith12Matches(orientedRawScanner, mappedScanner)
            if (matchedScannerPos != null) {
                return MappedScanner(rawScanner, orientation, matchedScannerPos)
            }
        }
        return null
    }

    fun parseRawScannerData(input: List<String>): MutableList<RawScannerData> {
        val scanners = ArrayList<RawScannerData>()
        var id = 0
        var currScanner: RawScannerData = RawScannerData(id++)
        for (line in input.subList(1, input.size)) {
            if (line.matches(".*scanner.*".toRegex())) {
                scanners.add(currScanner)
                currScanner = RawScannerData(id++)
            } else {
                if (!line.isEmpty()) {
                    currScanner.add(Pos3D(line.split(",").map { it.toInt() }))
                }
            }
        }
        scanners.add(currScanner)
        println("Scanners parsed: size [${scanners.size}][${scanners[0].size}]")
        return scanners
    }

    fun mapScanners(scannersLeft: MutableList<RawScannerData>): MutableList<MappedScanner> {
        val mappedScanners: MutableList<MappedScanner> = ArrayList()
        val firstScanner = MappedScanner(scannersLeft[0], Orientation(Facing.F1, Rotation.R1), Pos3D(0, 0, 0))
        scannersLeft.remove(scannersLeft[0])
        mappedScanners.add(firstScanner)
        var lastMappedScanners: MutableList<MappedScanner> = arrayListOf(firstScanner)
        while (scannersLeft.size > 0) {
            val newMappedScanners: MutableList<MappedScanner> = ArrayList()
            if (lastMappedScanners.size == 0) {
                throw Error("Failed to map all scanners, left: ${scannersLeft.size}")
            }
            for (lastMappedScanner in lastMappedScanners) {
                for (rawScanner in scannersLeft.toList()) {
                    val mappedScanner = tryMapScanner(rawScanner, lastMappedScanner)
                    if (mappedScanner != null) {
                        newMappedScanners.add(mappedScanner)
                        mappedScanners.add(mappedScanner)
                        scannersLeft.remove(rawScanner)
                    }
                }
            }
            lastMappedScanners = newMappedScanners
        }
        return mappedScanners
    }

    fun part1(input: List<String>): Int {
        val scannersLeft: MutableList<RawScannerData> = parseRawScannerData(input)
        val mappedScanners = mapScanners(scannersLeft)
        val beaconSet = mappedScanners.map { it.normalizedScannerIds }.flatten().toSet()
        return beaconSet.size
    }

    fun part2(input: List<String>): Int {
        val scannersLeft: MutableList<RawScannerData> = parseRawScannerData(input)
        val beaconSet = mapScanners(scannersLeft)
        val manhattanDists =
            beaconSet.map { first -> beaconSet.map { other -> other.position.diff(first.position) } }.flatten()
                .map { it.manHattan() }
        return manhattanDists.maxOrNull()!!
    }


    val day = 19
    println("Starting Day${day}")
    val testInput = readInput("Day${day}.test")
    checkEquals(part1(testInput), 79)
    val input = readInput("Day${day}")
    prcp(part1(input))
    checkEquals(part2(testInput), 3621)
    prcp(part2(input))
}

