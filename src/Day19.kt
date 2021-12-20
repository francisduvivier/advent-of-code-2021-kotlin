class Pos3D(val x: Int, val y: Int, val z: Int) {
    constructor(list: List<Int>) : this(list[0], list[1], list[2])

    val all = listOf(x, y, z)
    override fun toString(): String {
        return all.joinToString(",")
    }

    fun diff(other: Pos3D): Pos3D {
        return Pos3D(all.mapIndexed { it, index -> it - other.all[index] })
    }

    fun orient(orientation: Orientation): Pos3D {
        return orientation.mutatePos(this);
    }

    fun add(other: Pos3D): Pos3D {
        return Pos3D(all.mapIndexed { it, index -> it + other.all[index] })
    }
}

typealias RawScannerData = List<Pos3D>

class Orientation(val direction: Dir3D, val rotation: Dir2D) {
    fun mutatePos(pos3D: Pos3D): Pos3D {
        return rotation.mutatePos(direction.mutatePos(pos3D))
    }
}

class MappedScanner(val rawScannerData: RawScannerData, val orientation: Orientation, val position: Pos3D) {
    val normalizedScannerData = rawScannerData.map { normalizeCoords(it, orientation, position) }
    val normalizedScannerIds = normalizedScannerData.map { it.toString() }
}

enum class Dir2D(val xMult: Int, val yMult: Int) {
    UP(1, 1), DOWN(1, -1), LEFT(-1, 1), RIGHT(-1, -1);

    fun mutatePos(pos: Pos3D): Pos3D {
        return Pos3D(pos.x * xMult, pos.y * yMult, pos.z)
    }
}

enum class Dir3D(val mutator: (Pos3D) -> Pos3D) {
    UP({ Pos3D(it.x, it.y, it.z) }),
    DOWN({ Pos3D(it.x, it.z, it.y) }),
    LEFT({ Pos3D(it.z, it.x, it.y) }),
    RIGHT({ Pos3D(it.z, it.y, it.x) }),
    BACK({ Pos3D(it.y, it.x, it.z) }),
    FRONT({ Pos3D(it.y, it.z, it.x) });

    fun mutatePos(pos: Pos3D): Pos3D {
        return mutator(pos)
    }
}

val allOrientations = Dir3D.values().map { dir3d -> Dir2D.values().map { Orientation(dir3d, it) } }.flatten()


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

    fun getPossibleScannerPositions(orientedRawScanner: List<Pos3D>, mappedScanner: MappedScanner): List<Pos3D> {
        val scannerPos = ArrayList<Pos3D>()
        for (newBeacon in orientedRawScanner) {
            for (mappedBeacon in mappedScanner.normalizedScannerData) {
                scannerPos.add(mappedBeacon.diff(newBeacon))
            }
        }
        return scannerPos
    }

    fun findNbSame(newMappedScanner: MappedScanner, mappedScanner: MappedScanner): Int {
        val intersection = mappedScanner.normalizedScannerIds.intersect(newMappedScanner.normalizedScannerIds)
        println("intersection: ${intersection.joinToString(";")}")
        return intersection.size
    }

    fun tryMapScanner(rawScanner: List<Pos3D>, mappedScanner: MappedScanner): MappedScanner? {
        for (orientation in allOrientations) {
            for (scannerPosition in getPossibleScannerPositions(rawScanner, mappedScanner)) {
                val newMappedScanner = MappedScanner(rawScanner, orientation, scannerPosition)
                val nbMatches: Int = findNbSame(newMappedScanner, mappedScanner)
                if (nbMatches > 12) {
                    return newMappedScanner
                }
            }
        }
        return null
    }

    fun parseRawScannerData(input: List<String>): MutableList<RawScannerData> {
        val scanners = ArrayList<RawScannerData>()
        var currScanner: ArrayList<Pos3D> = ArrayList()
        for (line in input.subList(1, input.size)) {
            if (line.matches(".*scanner.*".toRegex())) {
                scanners.add(currScanner)
                currScanner = ArrayList()
            } else {
                if (!line.isEmpty()) {
                    currScanner.add(Pos3D(line.split(",").map { it.toInt() }))
                }
            }
        }
        scanners.add(currScanner)
        return scanners
    }

    fun part1(input: List<String>): Int {
        val scannersLeft: MutableList<RawScannerData> = parseRawScannerData(input)
        val mappedScanners: MutableList<MappedScanner> = ArrayList()
        var lastMappedScanners: MutableList<MappedScanner> = ArrayList()
        while (scannersLeft.size > 0) {
            val newMappedScanners: MutableList<MappedScanner> = ArrayList()
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
        val beaconSet = mappedScanners.map { it.normalizedScannerIds }.flatten().toSet()
        return beaconSet.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19.test")
    checkEquals(part1(testInput), 79)
    val input = readInput("Day19")
    prcp(part1(input))
    checkEquals(part2(testInput), 0)
    prcp(part2(input))
}

