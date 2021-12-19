typealias Pos3D = Triple<Int, Int, Int>

typealias RawScannerData = List<Pos3D>

class ScannerData(val rawScannerData: RawScannerData, val direction: Dir3D, val rotation: Dir2D, val position: Pos3D) {
    public val normalizedScannerData = rawScannerData.map { normalizeCoords(it, direction, rotation, position) }

    private fun normalizeCoords(
        rawScannerData: Triple<Int, Int, Int>,
        direction: Dir3D,
        rotation: Dir2D,
        position: Pos3D
    ): Pos3D {
        TODO("Not yet implemented")
    }

}

enum class Dir2D {
    UP, DOWN, LEFT, RIGHT
}

enum class Dir3D {
    UP, DOWN, LEFT, RIGHT, BACK, FRONT
}

fun main() {

    fun tryMapScanner(rawScanner: List<Pos3D>, mappedScanner: ScannerData): ScannerData? {
        TODO("Not yet implemented")
    }

    fun part1(input: List<String>): Int {

        val scannersLeft: MutableList<RawScannerData> = ArrayList()
        val mappedScanners: MutableList<ScannerData> = ArrayList()
        var lastMappedScanners: MutableList<ScannerData> = ArrayList()
        while (scannersLeft.size > 0) {
            val newMappedScanners: MutableList<ScannerData> = ArrayList()
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
        val beaconSet = mappedScanners.map { it.normalizedScannerData.joinToString(",") }.toSet()
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

