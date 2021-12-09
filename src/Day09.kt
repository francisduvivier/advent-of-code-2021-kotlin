fun main() {
    fun getNeighborLocations(matrix: Array<IntArray>, row: Int, col: Int): List<Pair<Int, Int>> {
        val nLocs = ArrayList<Pair<Int, Int>>()
        if (row > 0) {
            nLocs.add(Pair(row - 1, col))
        }

        if (row < matrix.size - 1) {
            nLocs.add(Pair(row + 1, col))
        }
        if (col > 0) {
            nLocs.add(Pair(row, col - 1))
        }
        if (col < matrix[row].size - 1) {
            nLocs.add(Pair(row, col + 1))
        }
        return nLocs
    }

    fun getNeighbors(matrix: Array<IntArray>, row: Int, col: Int): List<Int> {
        return getNeighborLocations(matrix, row, col).map { (row, col) -> matrix[row][col] }
    }

    fun part1(input: List<String>): Int {
        val matrix =
            Array(input.size, { row -> IntArray(input[row].length, { col -> input[row][col].toString().toInt() }) })
        val throts = ArrayList<Int>();
        for (row in 0..matrix.size - 1) {
            for (col in 0..matrix[row].size - 1) {
                val neighbours: List<Int> = getNeighbors(matrix, row, col)
                if (neighbours.find { it <= matrix[row][col] } == null) {
                    throts.add(matrix[row][col])
                }
            }
        }
        return throts.sum() + throts.size;
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09.test")
    check(part1(testInput) == 15)
//    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    prcp(part1(input))
    prcp(part2(input))
}
