import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.io.File
import java.lang.Exception
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

fun <T> checkEquals(actual: T, expected: T) {
    try {
        val result = actual?.equals(expected)
        println("Checking actual [$actual] equals [$expected]: [${if (result == true) "PASS" else "FAIL"}]")
        check(result == true)
    } catch (e: Exception) {
        println("Checking for expected [$expected] failed ")
        throw e
    }
}

fun indexes(array: Array<*>) = Array(array.size) { it }
fun indexes(array: Iterable<*>) = Array(array.count()) { it }
fun indexes(array: CharSequence) = Array(array.length) { it }

fun Iterable<Long>.product(): Long {
    return reduce { acc, value -> acc * value }
}

fun Iterable<Long>.min(): Long {
    return minOrNull()!!
}

fun Iterable<Long>.max(): Long {
    return maxOrNull()!!
}
