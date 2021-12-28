fun String.fromHexToBits(): String {
    return map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("")
}

open class Packet(val version: Int, val typeID: Int, val children: ArrayList<Packet> = ArrayList()) {
}

class LiteralPacket(version: Int, typeID: Int, val data: Int) : Packet(version, typeID) {
}

//typeID 4 = literal val
fun main() {
    checkEquals("jos".byteInputStream().readNBytes(2).joinToString("") { it.toInt().toChar().toString() }, "jo")
    fun parseLiteral(literalPacket: String): Int {
        var bitsString = ""
        for (chunk in literalPacket.substring(6).chunked(5)) {
            val last = chunk[0] == '0'
            bitsString += chunk.substring(1)
            if (last) {
                break;
            }
        }
        return bitsString.toInt(2);
    }

    fun parseHeader(inputBits: String): Pair<Int, Int> {
        val version = inputBits.substring(0, 3).toInt(2)
        val typeID = inputBits.substring(3, 6).toInt(2)
        return Pair(version, typeID)
    }

    fun parseLiteralPacket(version: Int, typeID: Int, input: String) {
//        val is = input.byteInputStream().readNBytes(5).toString()

    }

    fun parsePacketRec(inputBits: String): Packet {
        var currPointer = 0
        val (version, typeID) = parseHeader(inputBits)
        currPointer += 6
        when (typeID) {
            4 -> parseLiteralPacket(version, typeID, inputBits)
            else -> run {
                val lenTypeID = inputBits[currPointer++]
                when (lenTypeID) {
                    '0' -> run {
                        var readAmount = 15
                        val nbSubBits = inputBits.substring(currPointer until currPointer + readAmount).toInt(2)
                        currPointer += readAmount
                        val subPackets = inputBits.substring(currPointer until currPointer + nbSubBits)
                        currPointer += nbSubBits
//                        val childrenBitsChunks: List<String> = splitChildren(subPackets)
//                        return OperatorPacket(version, typeID, childrenBitsChunks.map { parsePacketRec(it) })
                    }
                    '1' -> run {
                        var readAmount = 11
                        val nbSubPackets = inputBits.substring(currPointer until currPointer + readAmount).toInt(2)
                        currPointer += readAmount
//                        val childrenBitsChunks: List<String> =
//                            splitChildren(inputBits.substring(currPointer), nbSubPackets)
//                        return OperatorPacket(version, typeID, childrenBitsChunks.map { parsePacketRec(it) })
                    }
                    else -> TODO("This should not happen")
                }
            }
        }
        return Packet(version, typeID)
//        TODO("Not yet implemented")
    }

    fun addVersionNumberRec(topPacket: Packet): Int {
        val children: List<Packet> = topPacket.children
        var sum = topPacket.version
        return sum + children.map { addVersionNumberRec(it) }.sum()
    }

    fun part1(input: List<String>): Int {
        val inputBits = input[0].fromHexToBits()
        val topPacket = parsePacketRec(inputBits)
        return addVersionNumberRec(topPacket)
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val literalPacketBitsTest = "110100101111111000101000"
    val literalTestHex = "D2FE28"
    checkEquals(literalTestHex.fromHexToBits(), literalPacketBitsTest)
    checkEquals(parseLiteral(literalPacketBitsTest), 2021)
    checkEquals(part1(listOf(literalTestHex)), 6)
    checkEquals(part1(listOf("8A004A801A8002F478")), 16)
    checkEquals(part1(listOf("620080001611562C8802118E34")), 12)
    checkEquals(part1(listOf("C0015000016115A2E0802F182340")), 23)
    checkEquals(part1(listOf("A0016C880162017C3686B18A3D4780")), 31)

    val input = readInput("Day16")
    prcp(part1(input))
    prcp(part2(input))
}

class OperatorPacket(version: Int, typeID: Int, children: ArrayList<Packet>) : Packet(version, typeID, children) {

}
