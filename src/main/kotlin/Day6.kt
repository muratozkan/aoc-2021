import java.nio.file.Files

fun main() {
    Day6().start()
}

class Day6 {
    fun start() {
        val inFile = Utils.inPath("day6.txt")

        val initial = Files.readString(inFile).split(",").map { it.toInt() }

        val part1 = iterate(initial, 80)
        println("Part 1: $part1")

        val part2 = iterate(initial, 256)
        println("Part 2: $part2")
    }

    private fun iterate(initial: List<Int>, days: Int): Long {
        val counts = Array(9) { 0L }
        initial.forEach { counts[it] += 1L }
        var toAdd = 0L
        for (d in 0  ..  days) {
            counts[8] = toAdd
            counts[6] += toAdd

            toAdd = counts[0]
            for (ci in 1 until counts.size) {
                counts[ci - 1] = counts[ci]
            }
            counts[8] = 0
        }
        return counts.sumOf { it.toLong() } + toAdd
    }
}