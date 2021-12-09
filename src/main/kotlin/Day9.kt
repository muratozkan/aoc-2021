import java.nio.file.Files
import kotlin.streams.toList

fun main() {
    Day9().start()
}

class Day9 {

    data class Pos2(val r: Int, val c: Int)

    fun start() {
        val inFile = Utils.inPath("day9.txt")

        val stream = Files.lines(inFile)
        val heightMap = stream.use { s ->
            s.map { it.map { c -> c - '0' }.toList() }.toList()
        }

        val lowPoints = findLows(heightMap)
        println("Part 1: ${lowPoints.sumOf { heightMap[it.r][it.c] } + lowPoints.size}")

        val basins = findBasins(heightMap, lowPoints)
        println("Part 2: ${basins.sorted().takeLast(3).reduce { a, b -> a * b }}")
    }

    private fun findBasins(heightMap: List<List<Int>>, lowPoints: List<Pos2>): List<Int> {
        return lowPoints.map { findBasin(heightMap, it).size }
    }

    private fun findBasin(heightMap: List<List<Int>>, low: Pos2): List<Pos2> {
        val rows = heightMap.size
        val cols = heightMap[0].size

        val basin = mutableSetOf<Pos2>()
        val toCheck = mutableListOf(low)
        while (toCheck.isNotEmpty()) {
            val check = toCheck.removeFirst()
            basin.add(check)
            neighbors(check.r, check.c, rows, cols)
                .filter { heightMap[it.r][it.c] != 9 && !basin.contains(it) }
                .forEach { toCheck.add(it) }
        }
        return basin.toList()
    }

    private fun findLows(heightMap: List<List<Int>>): List<Pos2> {
        val mins = mutableListOf<Pos2>()
        val rows = heightMap.size
        val cols = heightMap[0].size
        for (c in 0 until cols) {
            for (r in 0 until rows) {
                val v = heightMap[r][c]
                val isMin = neighbors(r, c, rows, cols)
                    .map { p -> heightMap[p.r][p.c] }
                    .all { it > v }
                if (isMin) {
                    mins.add(Pos2(r, c))
                }
            }
        }
        return mins
    }

    private fun neighbors(r: Int, c: Int, rows: Int, cols: Int): List<Pos2> {
        return listOf(Pos2(r - 1, c), Pos2(r + 1, c), Pos2(r, c - 1), Pos2(r, c + 1))
            .filter { pos -> pos.r >= 0 && pos.c >= 0 && pos.r < rows && pos.c < cols }
    }

}