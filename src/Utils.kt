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
