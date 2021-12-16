import java.nio.file.Files

fun main() {
    Day16().start()
}

class Day16 {
    fun start() {
        val packet = Files.readString(Utils.inPath("day16.txt"))

        // val packet = "D2FE28"   // literal
        // val packet = "38006F45291200" // operator w 2 literals
        // val packet = "EE00D40C823060"
        // val packet = "A0016C880162017C3686B18A3D4780" // go for it: 31

        val binString = packet.hexToPaddedBin()
        val parsed = parsePacket(0, binString.length, binString)

        val part1 = version(parsed)
        println("Part 1: $part1")

        val part2 = evaluate(parsed)
        println("Part 2: $part2")
    }

    private fun version(packet: Packet): Int {
        return when (packet) {
            is Literal -> packet.ver
            is Operator ->  packet.ver + packet.packets.sumOf { version(it) }
        }
    }

    private fun evaluate(packet: Packet): Long {
        return when (packet) {
            is Literal -> packet.value
            is Operator -> when (packet.op) {
                Op.SUM -> packet.packets.sumOf { evaluate(it) }
                Op.PRODUCT -> packet.packets.map { evaluate(it) }.reduce{ acc, i -> acc * i }
                Op.MIN -> packet.packets.minOf { evaluate(it) }
                Op.MAX -> packet.packets.maxOf { evaluate(it) }
                Op.GT -> if (evaluate(packet.packets[0]) > evaluate(packet.packets[1])) 1 else 0
                Op.LT -> if (evaluate(packet.packets[0]) < evaluate(packet.packets[1])) 1 else 0
                Op.EQ -> if (evaluate(packet.packets[0]) == evaluate(packet.packets[1])) 1 else 0
                else -> 0L
            }
        }

    }

    private fun String.hexToPaddedBin(): String {
        return map {
            it.toString().toInt(16).toString(2)
        }.joinToString(separator = "") { "0".repeat(4 - it.length) + it }
    }

    enum class Op {
        VAL,
        SUM,
        PRODUCT,
        MIN,
        MAX,
        GT,
        LT,
        EQ
    }

    sealed interface Packet {
        val ver: Int
        val size: Int
        val op: Op
    }

    data class Literal(override val ver: Int,  override val size: Int, override val op: Op, val value: Long) : Packet
    data class Operator(override val ver: Int, override val size: Int, override val op: Op, val packets: List<Packet>) : Packet

    private val types = arrayOf(
        Op.SUM,
        Op.PRODUCT,
        Op.MIN,
        Op.MAX,
        Op.VAL,
        Op.GT,
        Op.LT,
        Op.EQ
    )

    private fun parsePacket(s: Int, e: Int, str: String): Packet {
        val ver = str.substring(s, s + 3).toInt(2)
        val type = types[str.substring(s + 3, s + 6).toInt(2)]
        return when(type) {
            Op.VAL -> parseLiteral(s + 6, ver, str)
            else -> parseOperator(s + 6, e, type, ver, str)
        }
    }

    private fun parseOperator(s: Int, e: Int, type: Op, ver: Int, str: String): Operator {
        val lenLimit = str[s] == '0'
        return if (lenLimit) {
            val limit = str.substring(s + 1, s + 16).toInt(2)
            var curr = 0
            val list = mutableListOf<Packet>()
            while (curr < limit) {
                val p = parsePacket(s + 16 + curr, limit, str)
                list.add(p)
                curr += p.size
            }
            Operator(ver, 22 + limit, type, list) // type (3) + ver (3) + lt (1) + len (15) = 22
        } else {
            val limit = str.substring(s + 1, s + 12).toInt(2)
            var curr = 0
            val list = mutableListOf<Packet>()
            while (list.size < limit) {
                val p = parsePacket(s + 12 + curr, e, str)
                list.add(p)
                curr += p.size
            }
            Operator(ver, 18 + list.sumOf { it.size }, type, list)  // type (3) + ver (3) + lt (1) + len (11) = 18
        }
    }

    private fun parseLiteral(s: Int, ver: Int, str: String): Literal {
        var i = s
        var more: Boolean
        val buffer = StringBuffer()
        do {
            more = str[i] == '1'
            buffer.append(str.substring(i + 1, i + 5))
            i += 5
        } while (more)
        return Literal(ver, i - s + 6, Op.VAL, buffer.toString().toLong(2))
    }
}