fun String.fromHexToBits(): String {
    return map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("")
}

open class Packet(val version: Int, val typeID: Int) {
    var children: ArrayList<Packet> = ArrayList()
}

class LiteralPacket(version: Int, typeID: Int, val data: Int) : Packet(version, typeID) {
}

//typeID 4 = literal val
fun main() {
    fun parseLiteral(literalPacket: String): Int {

        return 0;
    }

    fun parsePacketRec(inputBits: String): Packet {
        val version = inputBits.substring(0, 3).toInt(2)
        val typeID = inputBits.substring(3, 6).toInt(2)
        when (typeID) {
            4 -> run {
                val data = parseLiteral(inputBits)
                return LiteralPacket(version, typeID, data)
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
