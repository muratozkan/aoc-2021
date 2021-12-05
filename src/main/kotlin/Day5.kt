import java.nio.file.Files
import kotlin.math.max
import kotlin.math.min
import kotlin.streams.toList

fun main() {
    Day5().start()
}

class Day5 {

    data class Pos2(val x: Int, val y: Int)
    data class LineSeg(val start: Pos2, val end: Pos2)

    fun start() {
        val inFile = Utils.inPath("day5.txt")

        val stream = Files.lines(inFile)
        val lines = stream.use {
            s -> s.map { parseLine(it) }.toList()
        }

        val hvIntersect = intersect(lines)
        println("Part 1: $hvIntersect")

        val hvdIntersect = intersect(lines, diagonals = true)
        println("Part 2: $hvdIntersect")
    }

    private fun intersect(lines: List<LineSeg>, diagonals: Boolean = false): Int {
        val map = mutableMapOf<Pos2, Int>()
        for (line in lines) {
            val (s, e) = line
            if (s.x == e.x) {
                for (y in min(s.y, e.y)..max(s.y, e.y)) {
                    val pc = Pos2(s.x, y)
                    map[pc] = map.getOrDefault(pc, 0) + 1
                }
            } else if (s.y == e.y) {
                for (x in min(s.x, e.x)..max(s.x, e.x)) {
                    val pc = Pos2(x, s.y)
                    map[pc] = map.getOrDefault(pc, 0) + 1
                }
            } else if (diagonals) {
                val m = (e.x - s.x) / (e.y - s.y)
                var y = if (m >= 0) min(s.y, e.y) else max(s.y, e.y)
                for (x in min(s.x, e.x)..max(s.x, e.x)) {
                    val pc = Pos2(x, y)
                    map[pc] = map.getOrDefault(pc, 0) + 1
                    y += m
                }
            }
        }
        return map.values.count { it > 1 }
    }

    private fun parseLine(line: String): LineSeg {
        val (sx, sy, ex, ey) = line.split(" -> ", ",")
        return LineSeg(Pos2(sx.toInt(), sy.toInt()), Pos2(ex.toInt(), ey.toInt()))
    }
}