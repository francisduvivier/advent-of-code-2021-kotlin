typealias CostCalcFun = (toRow: Int, toCol: Int, fromRow: Int, fromCol: Int) -> Int

fun main() {

    fun findBestPathRec(
        bestCostMatrix: List<MutableList<Long>>,
        getCost: CostCalcFun,
        row: Int,
        col: Int,
        recDepth: Int = 0
    ) {
        if (bestCostMatrix.size <= row || bestCostMatrix[0].size <= col) {
            return;
        }
        val currCost = bestCostMatrix[row][col]
        for ((otherRow, otherCol) in getNeighborLocations(bestCostMatrix, row, col)) {
            val newCost = currCost + getCost(otherRow, otherCol, row, col)
            if (bestCostMatrix[otherRow][otherCol] > newCost && bestCostMatrix.last()
                    .last() > newCost + bestCostMatrix.size - 1 - row - bestCostMatrix[0].size - 1 - col
            ) {
                bestCostMatrix[otherRow][otherCol] = newCost
                findBestPathRec(bestCostMatrix, getCost, otherRow, otherCol, recDepth + 1)
            }
        }
    }

    fun processInput(
        input: List<String>,
        multiplicator: Int
    ): List<List<Int>> {
        var matrix = input.map { it.toCharArray().map { it.digitToInt() } }
        if (multiplicator > 1) {
            matrix = List(matrix.size * multiplicator, { row ->
                List(matrix[0].size * multiplicator,
                    { col ->
                        (matrix[row % matrix.size][col % matrix[0].size] + (row / matrix.size) + (col / matrix[0].size)) % 9
                    })
                    .map { if (it == 0) 9 else it }
            })
        }
        return matrix
    }

    fun findBestPath(input: List<String>, multiplicator: Int = 1): Long {
        var matrix = processInput(input, multiplicator)
        val bestCostMatrix = matrix.map { it.map { Long.MAX_VALUE }.toMutableList() }
        val getCost = { toRow: Int, toCol: Int, fromRow: Int, fromCol: Int -> matrix[toRow][toCol] }
        bestCostMatrix[0][0] = 0.toLong()
        bestCostMatrix.last()[bestCostMatrix.lastIndex] = Math.min(
            matrix[0].map { it.toLong() }.sum() + matrix.map { it[it.lastIndex].toLong() }.sum(),
            matrix[matrix.lastIndex].map { it.toLong() }.sum() + matrix.map { it[0].toLong() }.sum()
        ) - matrix[0][0]// Add minimum cost to solution to prevent stackoverflow for very long paths
        findBestPathRec(bestCostMatrix, getCost, 0, 0)
        return bestCostMatrix.last().last()
    }

    fun part1(input: List<String>): Long {
        val bestPathCost = findBestPath(input)
        return bestPathCost
    }

    fun part2(input: List<String>): Long {
        val bestPathCost = findBestPath(input, 5)
        return bestPathCost
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15.test")
    check(part1(testInput) == 40.toLong())
    println("part 1 check passed")

    val input = readInput("Day15")
//    prcp(part1(input))
    val testInput3 = listOf("8")
    val processedTestInput1 = processInput(testInput3, 5)
    check("89123" == processedTestInput1[0].joinToString(""))
    val multipliedInput = readInput("Day15.test2")
    val processedTestInput = processInput(testInput, 5)
    check(multipliedInput[0] == processedTestInput[0].joinToString(""))
    check(part1(multipliedInput) == 315.toLong())
    println("part 2, processed check passed")

    check(part2(testInput) == 315.toLong())
    println("part 2, full check passed")
    prcp(part2(input))
}