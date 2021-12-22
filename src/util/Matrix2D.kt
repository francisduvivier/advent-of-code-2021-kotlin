fun get8NeighborLocations(matrix: Array<IntArray>, row: Int, col: Int): List<Pair<Int, Int>> {
    val nLocs = getNeighborLocations(matrix, row, col).toMutableList()
    if (row > 0 && col > 0) {
        nLocs.add(Pair(row - 1, col - 1))
    }
    if (row < matrix.size - 1 && col < matrix[row].size - 1) {
        nLocs.add(Pair(row + 1, col + 1))
    }
    if (col > 0 && row < matrix.size - 1) {
        nLocs.add(Pair(row + 1, col - 1))
    }
    if (col < matrix[row].size - 1 && row > 0) {
        nLocs.add(Pair(row - 1, col + 1))
    }
    return nLocs
}

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

fun getNeighborLocations(matrix: List<List<*>>, row: Int, col: Int): List<Pair<Int, Int>> {
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

fun rowCols(matrix: Array<IntArray>): List<Pair<Int, Int>> {
    return rowCols(matrix.count(), matrix.first().count())
}

fun rowCols(matrix: Array<Array<*>>): List<Pair<Int, Int>> {
    return rowCols(matrix.count(), matrix.first().count())
}

fun rowCols(rows: Int, cols: Int): List<Pair<Int, Int>> {
    return Array(rows) { it }.flatMap { rowI -> Array(cols) { it }.map { Pair(rowI, it) } }
}


fun rowCols(matrix: List<String>): List<Pair<Int, Int>> {
    return rowCols(matrix.count(), matrix.first().count())
}

fun rowCols(matrix: Iterable<Iterable<*>>): List<Pair<Int, Int>> {
    return rowCols(matrix.count(), matrix.first().count())
}
