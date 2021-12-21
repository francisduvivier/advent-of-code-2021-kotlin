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
    UP(1, 1), DOWN(1, -1), LEFT(-1, 1), RIGHT(-1, -1),
    UP2(1, 1, -1), DOWN2(1, -1, -1), LEFT2(-1, 1, -1), RIGHT2(-1, -1, -1);

    fun mutatePos(pos: Pos3D): Pos3D {
        return Pos3D(pos.x * xMult, pos.y * yMult, pos.z * zMult)
    }
}

enum class Facing(val mutator: (Pos3D) -> Pos3D) {
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
        val intersection =
            mappedScanner.normalizedScannerIds.toSet().intersect(newMappedScanner.normalizedScannerIds.toSet())

        if (intersection.size == 0) {
            throw Error(
                """Invalid state, the way this is set up should always result in at least 1 overlap,
                |${newMappedScanner}
                |${mappedScanner}
            """.trimMargin()
            )
        } else if (intersection.size > 1) {
            println("intersection: size[${intersection.size}]: ${intersection.joinToString(";")}")
        }
        return intersection.size
    }

    fun tryMapScanner(rawScanner: RawScannerData, mappedScanner: MappedScanner): MappedScanner? {
        println("try map scanner [$mappedScanner]")
        println("on scanner [$rawScanner]")
        for (orientation in allOrientations) {
            val orientedRawScanner = MappedScanner(rawScanner, orientation, Pos3D(0, 0, 0)).normalizedScannerData
            for (scannerPosition in getPossibleScannerPositions(orientedRawScanner, mappedScanner)) {
                val newMappedScanner = MappedScanner(rawScanner, orientation, scannerPosition)
                val nbMatches: Int = findNbSame(newMappedScanner, mappedScanner)
                if (nbMatches >= 12) {
                    return newMappedScanner
                }
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

    fun part1(input: List<String>): Int {
        val scannersLeft: MutableList<RawScannerData> = parseRawScannerData(input)
        val mappedScanners: MutableList<MappedScanner> = ArrayList()
        val firstScanner = MappedScanner(scannersLeft[0], Orientation(Facing.UP, Rotation.UP), Pos3D(0, 0, 0))
        scannersLeft.remove(scannersLeft[0])
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
        val beaconSet = mappedScanners.map { it.normalizedScannerIds }.flatten().toSet()
        return beaconSet.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }


    val day = 19
    println("Starting Day${day}")
    val testInput = readInput("Day${day}.test")
    checkEquals(part1(testInput), 79)
    val input = readInput("Day${day}")
    prcp(part1(input))
    checkEquals(part2(testInput), 0)
    prcp(part2(input))
}

