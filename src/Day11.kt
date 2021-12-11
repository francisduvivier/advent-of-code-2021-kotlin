fun main() {
    var totalFlashes = 0.toLong()
    fun increase8Neighbors(board: Array<IntArray>, row: Int, col: Int) {
        if (board[row][col] != 10) {
            return
        }
        board[row][col] = 0
        totalFlashes++
        val all8NeighborLocs = get8NeighborLocations(board, row, col)
        for ((nRow, nCol) in all8NeighborLocs) {
            val nb = board[nRow][nCol]
            if ((nb % 10) != 0) { // 9 will be handled in other loop, 0 is already fired
                board[nRow][nCol]++
            }
        }
    }

    fun part1(input: List<String>): Long {
        val board = input.map { line -> line.toCharArray().map { it.toString().toInt() }.toIntArray() }.toTypedArray()
        val rowCols = rowCols(board)
        val nbStebs = 100
        for (step in 1..nbStebs) {
            for ((row, col) in rowCols) {
                board[row][col]++
            }
            do {
                val prevVal = totalFlashes
                for ((row, col) in rowCols) {
                    increase8Neighbors(board, row, col)
                }
            } while (prevVal != totalFlashes)
        }
        val result = totalFlashes
        totalFlashes = 0;
        return result
    }

    fun part2(input: List<String>): Long {
        val board = input.map { line -> line.toCharArray().map { it.toString().toInt() }.toIntArray() }.toTypedArray()
        val rowCols = rowCols(board)
        val nbStebs = 10000
        for (step in 1..nbStebs) {
            for ((row, col) in rowCols) {
                board[row][col]++
            }
            do {
                val prevVal = totalFlashes
                for ((row, col) in rowCols) {
                    increase8Neighbors(board, row, col)
                }
            } while (prevVal != totalFlashes)
            if (board.map { it.sum() }.sum() == 0) {
                return step.toLong()
            }
        }
        totalFlashes = 0;
        throw Error("did not find sync point")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11.test")
    check(part1(testInput) == 1656.toLong())
    check(part2(testInput) == 195.toLong())

    val input = readInput("Day11")
    prcp(part1(input))
    prcp(part2(input))
}