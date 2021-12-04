fun main() {

    fun rowOrColumnFull(boardSelection: Array<BooleanArray>, row: Int, col: Int): Boolean {
        return boardSelection[row].all { it == true } || boardSelection.map { it[col] }.all { it == true }
    }


    fun part1(input: List<String>): Int {
        val numbersDrawn = input[0].split(",").map({ it.toInt() })
        val rows = 5;
        val boardsNumbers =
            input.subList(2, input.size).filter { !it.isEmpty() }
                .map({ it.split(" +".toRegex()).map({ it.toInt() }) })
                .windowed(rows, rows)
        val columns = boardsNumbers[0][0].size
        val boards = boardsNumbers.size
        val boardsSelection =
            Array(boards) { Array(rows) { BooleanArray(columns, { false }) } }
        val boardsPointsLeft = Array(boards, { boardsNumbers[it].map { it.sum() }.sum() })
        for (newNumber in numbersDrawn) {
            for (boardIndex in 0..boards - 1) {
                for (row in 0..rows - 1) {
                    for (col in 0..columns - 1) {
                        if (boardsNumbers[boardIndex][row][col] == newNumber) {
                            boardsSelection[boardIndex][row][col] = true;
                            boardsPointsLeft[boardIndex] -= newNumber;
                            if (rowOrColumnFull(boardsSelection[boardIndex], row, col)) {
                                val result = newNumber * boardsPointsLeft[boardIndex]
                                return result;
                            }
                        }
                    }
                }
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04.test")
    check(part1(testInput) == 4512)

    val input = readInput("Day04")
    prcp(part1(input))
    prcp(part2(input))
}
