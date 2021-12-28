fun String.fromHexToBits(): String {
    return map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("")
}

class BITStream(val inputString: String) {
    var pointer = 0
    fun read(nbChars: Int = 1): String {
        val substring = inputString.substring(pointer, pointer + nbChars)
        pointer += nbChars
        return substring
    }

    fun readInt(nbChars: Int): Int {
        return read(nbChars).toInt(2)
    }
}

open class Packet(val version: Int, val typeID: Int, val children: ArrayList<Packet> = ArrayList()) {
}

class LiteralPacket(version: Int, val data: Long) : Packet(version, 4) {
}

class OperatorPacket(version: Int, typeID: Int, children: ArrayList<Packet>) : Packet(version, typeID, children) {

}

fun main() {
    fun parseLiteral(literalPacket: BITStream): Long {
        var bitsString = ""
        while (true) {
            val chunk = literalPacket.read(5)
            val last = chunk[0] == '0'
            bitsString += chunk.substring(1)
            if (last) {
                break;
            }
        }
        return bitsString.toLong(2);
    }

    fun parseHeader(inputBits: BITStream): Pair<Int, Int> {
        val version = inputBits.readInt(3)
        val typeID = inputBits.readInt(3)
        return Pair(version, typeID)
    }

    fun parseLiteralPacket(version: Int, input: BITStream): LiteralPacket {
        return LiteralPacket(version, parseLiteral(input))
    }

    fun parsePacketRec(inputBits: BITStream): Packet {
        var currPointer = 0
        val (version, typeID) = parseHeader(inputBits)
        currPointer += 6
        when (typeID) {
            4 -> parseLiteralPacket(version, inputBits)
            else -> run {
                val lenTypeID = inputBits.read()
                when (lenTypeID) {
                    "0" -> run {
                        val nbSubBits = inputBits.readInt(15)
                        val mark = inputBits.pointer
                        val children = ArrayList<Packet>()
                        while (inputBits.pointer - mark < nbSubBits) {
                            children.add(parsePacketRec(inputBits))
                        }
                        if (inputBits.pointer - mark != nbSubBits) {
                            TODO("This should not happen")
                        }
                        return OperatorPacket(version, typeID, children)
                    }
                    "1" -> run {
                        val nbSubPackets = inputBits.readInt(11)
                        val children = ArrayList<Packet>()
                        while (children.size < nbSubPackets) {
                            children.add(parsePacketRec(inputBits))
                        }
                        return OperatorPacket(version, typeID, children)
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

    fun part1(input: String): Int {
        val inputBits = input.fromHexToBits()
        val topPacket = parsePacketRec(BITStream(inputBits))
        return addVersionNumberRec(topPacket)
    }

    fun part2(input: String): Long {
        return 0
    }

    fun part2(input: List<String>): Long {
        return part2(input[0])
    }
    // test if implementation meets criteria from the description, like:
    val literalPacketBitsTest = "110100101111111000101000"
    val literalTestHex = "D2FE28"
    checkEquals(literalTestHex.fromHexToBits(), literalPacketBitsTest)
    checkEquals(parseLiteral(BITStream(literalPacketBitsTest.substring(6))), 2021)
    checkEquals(part1((literalTestHex)), 6)
    checkEquals(part1("8A004A801A8002F478"), 16)
    checkEquals(part1("620080001611562C8802118E34"), 12)
    checkEquals(part1("C0015000016115A2E0802F182340"), 23)
    checkEquals(part1("A0016C880162017C3686B18A3D4780"), 31)

    val input = readInput("Day16")
    prcp(part1(input[0]))
    checkEquals(part2("04005AC33890"), 54)
    checkEquals(part2("C200B40A82"), 3)
    checkEquals(part2("04005AC33890"), 4)
    checkEquals(part2("880086C3E88112"), 7)
    checkEquals(part2("CE00C43D881120"), 9)
    checkEquals(part2("D8005AC2A8F0"), 5)
    checkEquals(part2("F600BC2D8F"), 5)
    checkEquals(part2("9C005AC2F8F0"), 5)
    checkEquals(part2("9C0141080250320F1802104A08"), 1)
    prcp(part2(input[0]))
}


