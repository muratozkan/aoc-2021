import java.nio.file.Files
import kotlin.streams.toList

fun main() {
    Day11().start()
}

class Day11 {

    data class Pos2(val r: Int, val c: Int)

    fun start() {
        val inFile = Utils.inPath("day11.txt")

        val stream = Files.lines(inFile)
        val energies = stream.use { s ->
            s.map { it.map { c -> c - '0' }.toList() }.toList()
        }

        val part1 = iterate(energies)
        println("Part 1: $part1")

        val part2 = syncFlashAt(energies)
        println("Part 2: $part2")

    }

    private fun iterateOne(levels: Array<Array<Int>>): Int {
        val rows = levels.size
        val cols = levels[0].size

        val queue = ArrayDeque<Pos2>()
        var fc = 0

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                levels[r][c] += 1
                if (levels[r][c] > 9) {
                    levels[r][c] = 0
                    fc += 1
                    queue.addLast(Pos2(r, c))
                }
            }
        }

        while (!queue.isEmpty()) {
            val pos = queue.removeFirst()
            neighbors(pos, rows, cols)
                .filterNot { levels[it.r][it.c] == 0 }
                .forEach { fp ->
                    levels[fp.r][fp.c] += 1
                    if (levels[fp.r][fp.c] > 9) {
                        levels[fp.r][fp.c] = 0
                        fc += 1
                        queue.addLast(fp)
                    }
                }
        }

        return fc
    }

    private fun syncFlashAt(energies: List<List<Int>>): Int {
        val levels = copy(energies)
        var step = 0
        var flashes: Int
        do {
            flashes = iterateOne(levels)
            step += 1
        } while (flashes != 100)
        return step
    }

    private fun copy(energies: List<List<Int>>): Array<Array<Int>> {
        val rows = energies.size
        val cols = energies[0].size

        val levels = Array(rows) {
            Array(cols) { 0 }
        }
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                levels[r][c] = energies[r][c]
            }
        }
        return levels
    }

    private fun iterate(energies: List<List<Int>>, times: Int = 100): Int {
        val levels = copy(energies)

        var flashCount = 0
        for (i in 0 until times) {
            flashCount += iterateOne(levels)
        }
        return flashCount
    }

    private fun neighbors(p: Pos2, rows: Int, cols: Int): List<Pos2> {
        val (r, c) = p
        return listOf(
            Pos2(r - 1, c - 1), Pos2(r - 1, c), Pos2(r - 1, c + 1),
            Pos2(r, c - 1), Pos2(r, c + 1),
            Pos2(r + 1, c - 1), Pos2(r + 1, c), Pos2(r + 1, c + 1)
        ).filter { pos -> pos.r >= 0 && pos.c >= 0 && pos.r < rows && pos.c < cols }
    }
}