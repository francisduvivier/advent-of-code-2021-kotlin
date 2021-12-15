typealias CostCalcFun = (toRow: Int, toCol: Int, fromRow: Int, fromCol: Int) -> Int

fun main() {

    fun findBestPathRec(
        bestCostMatrix: List<MutableList<Long>>,
        getCost: CostCalcFun,
        row: Int,
        col: Int,
    ) {
        if (bestCostMatrix.size <= row || bestCostMatrix[0].size <= col) {
            return;
        }
        val currCost = bestCostMatrix[row][col]
        for ((otherRow, otherCol) in getNeighborLocations(bestCostMatrix, row, col)) {
            val newCost = currCost + getCost(otherRow, otherCol, row, col)
            if (bestCostMatrix[otherRow][otherCol] > newCost) {
                bestCostMatrix[otherRow][otherCol] = newCost
                findBestPathRec(bestCostMatrix, getCost, otherRow, otherCol)
            }
        }
    }

    fun findBestPath(input: List<String>): Long {
        val matrix = input.map { it.toCharArray().map { it.digitToInt() } }
        val bestCostMatrix = matrix.map { it.map { Long.MAX_VALUE }.toMutableList() }
        val getCost = { toRow: Int, toCol: Int, fromRow: Int, fromCol: Int -> matrix[toRow][toCol] }
        bestCostMatrix[0][0] = matrix[0][0].toLong()
        findBestPathRec(bestCostMatrix, getCost, 0, 0)
        return bestCostMatrix.last().last()
    }

    fun part1(input: List<String>): Long {
        val bestPathCost = findBestPath(input)
        return bestPathCost
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15.test")
    check(part1(testInput) == 40.toLong())

    val input = readInput("Day15")
    prcp(part1(input))

    prcp(part2(input))
}