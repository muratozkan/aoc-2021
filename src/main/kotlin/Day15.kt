import java.nio.file.Files
import java.util.*
import kotlin.streams.toList
import kotlin.math.min

fun main() {
    Day15().start()
}

class Day15 {
    fun start() {
        val inFile = Utils.inPath("day15.txt")

        val reader = Files.newBufferedReader(inFile)
        val maze = reader.use {
            val mz = mutableListOf<List<Int>>()
            while (reader.ready()) {
                val row = reader.readLine().chars()
                    .map { it.toChar() - '0' }.toList()
                mz.add(row)
            }
            mz
        }

        val part1 = lowestRiskPath(maze)
        println("Part 1: $part1")

        val part2 = minCost(expandMaze(maze))
        println("Part 2: $part2")
    }

    private fun lowestRiskPath(maze: List<List<Int>>): Long {
        val rows = maze.size
        val cols = maze[0].size

        val costs = Array(rows) { Array(cols) { Long.MAX_VALUE } }

        costs[0][0] = 0
        for (i in 1 until rows) {
            costs[i][0] = costs[i - 1][0] + maze[i][0]
        }

        for (i in 1 until cols) {
            costs[0][i] = costs[0][i - 1] + maze[0][i]
        }

        for (r in 1 until rows) {
            for (c in 1 until cols) {
                costs[r][c] = maze[r][c] + min(costs[r - 1][c], costs[r][c - 1])
            }
        }

        return costs[rows - 1][cols - 1]
    }

    data class Cell(val r: Int, val c: Int, val v: Long)

    private val dirs = arrayOf(arrayOf(-1, 0), arrayOf(0, -1), arrayOf(0, 1), arrayOf(1, 0))

    private fun minCost(maze: List<List<Int>>): Long {
        val rows = maze.size
        val cols = maze[0].size

        val costs = Array(rows) { Array(cols) { Long.MAX_VALUE } }
        val visited = Array(rows) { Array(cols) { false } }
        costs[0][0] = 0L

        val pq = PriorityQueue<Cell>(compareBy { it.v })
        pq.add(Cell(0, 0, 0))

        while (pq.isNotEmpty()) {
            val (r, c, v) = pq.remove()
            if (visited[r][c]) {
                continue
            }
            visited[r][c] = true
            for ((dr, dc) in dirs) {
                val nr = r + dr
                val nc = c + dc
                if (inBounds(nr, nc, rows, cols) && !visited[nr][nc]) {
                    costs[nr][nc] = min(costs[nr][nc], costs[r][c] + maze[nr][nc])
                    pq.add(Cell(nr, nc, costs[nr][nc]))
                }
            }
        }
        return costs[rows - 1][cols - 1]
    }

    private fun inBounds(nr: Int, nc: Int, rows: Int, cols: Int): Boolean {
        return nr >=0 && nr < rows && nc >= 0 && nc < cols
    }

    private fun expandMaze(maze: List<List<Int>>): List<List<Int>> {
        val rows = maze.size
        val cols = maze[0].size

        val expanded = MutableList(rows * 5) { MutableList(cols * 5) { 0 } }

        for (rr in 0 until 5) {
            for (cr in 0 until 5) {
                for (r in 0 until rows) {
                    for (c in 0 until cols) {
                        val v = maze[r][c] + rr + cr
                        expanded[rr * rows + r][cr * cols + c] = v - if (v > 9) 9 else 0
                    }
                }
            }
        }

        return expanded
    }
 }