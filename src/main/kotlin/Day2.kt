import java.nio.file.Files
import kotlin.streams.toList

fun main(args: Array<String>) {
    val inFile = Utils.inPath("day2.txt")

    val input = Files.lines(inFile)
        .map { line -> parseLine(line) }
        .toList()

    val pos = input
        .stream()
        .reduce(Pos2(0, 0)) { p1, p2 -> p1 + p2 }

    println("Part 1: ${pos.x * pos.y}")

    val pos2 = input.stream()
        .reduce(Pos2(0, 0)) { acc, p ->
            val aim = acc.aim + p.y
            Pos2(acc.x + p.x, acc.y + p.x * aim, aim)
        }
    println("Part 2: ${pos2.x * pos2.y}")
}

fun parseLine(line: String): Pos2 {
    val (cmd, ds) = line.split(" ")
    val d = ds.toInt()
    val pos = when (cmd) {
        "forward" -> Pos2(d, 0)
        "down" -> Pos2(0, d)
        "up" -> Pos2(0, -d)
        else -> throw IllegalArgumentException(line)
    }
    return pos
}

data class Pos2(val x: Int, val y: Int, val aim: Int = 0) {

    operator fun plus(other: Pos2): Pos2 {
        return Pos2(x + other.x, y + other.y)
    }
}