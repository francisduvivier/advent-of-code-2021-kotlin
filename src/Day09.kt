fun main() {
    fun exploreValleyRec(matrix: Array<IntArray>, row: Int, col: Int, selector: (Int) -> Boolean): List<Int> {
        val value = matrix[row][col]
        if (value == -1 || !selector(value)) {
            return emptyList()
        }
        matrix[row][col] = -1
        val neighbors = ArrayList<Int>()
        neighbors.add(value)
        for ((neighborRow, neighborCol) in getNeighborLocations(matrix, row, col)) {
            neighbors.addAll(exploreValleyRec(matrix, neighborRow, neighborCol, selector))
        }
        return neighbors
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
        val matrix =
            Array(input.size, { row -> IntArray(input[row].length, { col -> input[row][col].toString().toInt() }) })
        val groups = ArrayList<List<Int>>();
        for (row in 0..matrix.size - 1) {
            for (col in 0..matrix[row].size - 1) {
                val valley = exploreValleyRec(matrix, row, col, { it != 9 })
                if (valley.size != 0) {
                    groups.add(valley)
                }
            }
        }

        return groups.map { it.size }.sortedDescending().subList(0, 3).reduce { acc, next -> acc * next }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09.test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    prcp(part1(input))
    prcp(part2(input))
}
