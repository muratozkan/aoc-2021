import kotlin.io.path.bufferedReader

fun main() {
    Day4().start()
}

class Day4 {

    data class Pos2(val row: Int, val col: Int)

    class Board(private val map: Map<Int, Pos2>) {

        private var remaining = HashMap(map)
        private var rowCounts = Array(5) { 5 }
        private var colCounts = Array(5) { 5 }

        fun register(num: Int) {
            val pos = remaining.remove(num)
            if (pos != null) {
                rowCounts[pos.row] -= 1
                colCounts[pos.col] -= 1
            }
        }

        fun complete(): Boolean {
            return rowCounts.any { it == 0 } || colCounts.any { it == 0}
        }

        fun remainingSum(): Int {
            return remaining.keys.sum()
        }

        fun reset() {
            remaining = HashMap(map)
            rowCounts = Array(5) { 5 }
            colCounts = Array(5) { 5 }
        }
    }

    data class Winner(val board: Board, val num: Int) {
        fun score(): Int {
            return board.remainingSum() * num
        }
    }

    fun start() {
        val inFile = Utils.inPath("day4.txt")

        val reader = inFile.bufferedReader()
        reader.use {
            val nums = reader.readLine().split(",").map { it.toInt() }.toList()
            val boards = mutableListOf<Board>()

            while (reader.ready()) {
                reader.readLine()

                val board = mutableMapOf<Int, Pos2>()
                for (r in 0..4) {
                    val rs = reader.readLine().split(" ")
                    rs.filter { it.isNotBlank() }.forEachIndexed { id, ns -> board[ns.toInt()] = Pos2(r, id) }
                }
                boards.add(Board(board))
            }

            val part1 = playBingo(nums, boards)
            println("Part 1: ${part1?.score()}")

            boards.forEach { it.reset() }

            val part2 = lastCardStanding(nums, boards)
            println("Part 2: ${part2?.score()}")
        }
    }

    private fun playBingo(nums: List<Int>, boards: List<Board>): Winner? {
        for (n in nums) {
            for (b in boards) {
                b.register(n)
                if (b.complete())
                    return Winner(b, n)
            }
        }
        return null
    }

    private fun lastCardStanding(nums: List<Int>, boards: List<Board>): Winner? {
        val toCheck = boards.indices.toMutableSet()
        val toRemove = mutableSetOf<Int>()
        for (n in nums) {
            for (bi in toCheck) {
                val b = boards[bi]
                b.register(n)
                if (b.complete())
                    toRemove.add(bi)
            }
            toCheck.removeAll(toRemove)
            if (toCheck.isEmpty()) {
                return Winner(boards[toRemove.first()], n)
            }
            toRemove.clear()
        }
        return null
    }
}
