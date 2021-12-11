fun main() {
    fun increase8Neighbors(board: Array<IntArray>, row: Int, col: Int): Int {
        if (board[row][col] != 10) {
            return 0
        }
        board[row][col] = 0
        val all8NeighborLocs = get8NeighborLocations(board, row, col)
        for ((nRow, nCol) in all8NeighborLocs) {
            val nb = board[nRow][nCol]
            if ((nb % 10) != 0) { // 9 will be handled in other loop, 0 is already fired
                board[nRow][nCol]++
            }
        }
        return 1
    }

    fun part1(input: List<String>): Int {
        val board = input.map { line -> line.toCharArray().map { it.toString().toInt() }.toIntArray() }.toTypedArray()
        val rowCols = rowCols(board)
        val nbStebs = 100
        var totalFlashes = 0;
        for (step in 1..nbStebs) {
            for ((row, col) in rowCols) {
                board[row][col]++
            }
            do {
                val prevVal = totalFlashes
                for ((row, col) in rowCols) {
                    totalFlashes += increase8Neighbors(board, row, col)
                }
            } while (prevVal != totalFlashes)
        }
        return totalFlashes
    }

    fun part2(input: List<String>): Int {
        val board = input.map { line -> line.toCharArray().map { it.toString().toInt() }.toIntArray() }.toTypedArray()
        val rowCols = rowCols(board)
        val nbStebs = 100000
        for (step in 1..nbStebs) {
            for ((row, col) in rowCols) {
                board[row][col]++
            }
            do {
                var flashes = 0
                for ((row, col) in rowCols) {
                    flashes += increase8Neighbors(board, row, col)
                }
            } while (flashes != 0)
            if (board.map { it.sum() }.sum() == 0) {
                return step
            }
        }
        throw Error("did not find sync point after " + nbStebs + "tries")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11.test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    prcp(part1(input))
    prcp(part2(input))
}