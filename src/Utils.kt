import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest


/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> = File("input", "$name.txt").readLines().map { it.trim() }

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)


fun prcp(result: Long) {
    prcp(result.toString())
}

fun prcp(result: Int) {
    prcp(result.toString())
}

/**
 * Prints and copies the result
 */
fun prcp(resultString: String) {
    if (resultString.equals("0")) {
        println("0 Result given, not copying")
        return
    }
    val toolkit: Toolkit = Toolkit.getDefaultToolkit()
    val clipboard: Clipboard = toolkit.getSystemClipboard()
    val strSel = StringSelection(resultString)
    clipboard.setContents(strSel, null)
    println("Result(copied): " + resultString);
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

fun getNeighbors(matrix: Array<IntArray>, row: Int, col: Int): List<Int> {
    return getNeighborLocations(matrix, row, col).map { (row, col) -> matrix[row][col] }
}

fun indexes(array: Array<*>) = Array(array.size) { it }
fun indexes(array: Iterable<*>) = Array(array.count()) { it }
fun indexes(array: CharSequence) = Array(array.length) { it }


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
