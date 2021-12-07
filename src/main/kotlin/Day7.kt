import java.nio.file.Files
import kotlin.math.*

fun main() {
    Day7().start()
}

class Day7 {
    fun start() {
        val inFile = Utils.inPath("day7.txt")

        val nums = Files.readString(inFile).split(",").map { it.toInt() }

        val part1 = alignMin(nums, this::constCost)
        println("Part 1: ${part1.second} (min: ${part1.first})")

        val part2 = alignMin(nums, this::incrCost)
        println("Part 2: ${part2.second} (min: ${part2.first})")

    }

    private fun alignMin(nums: List<Int>, cost: (sorted: List<Int>, test: Int) -> Int): Pair<Int, Int> {
        val sorted = nums.sorted().toList()

        val mean = sorted.sum() / sorted.size
        val median = median(sorted)

        val mn = min(mean + 1, median - 1)
        val mx = max(mean + 1, median - 1)

        var minT = -1
        var minS = Int.MAX_VALUE
        for (t in mn..mx) {
            val test = cost(sorted, t)
            if (test < minS) {
                minT = t
                minS = test
            }
        }

        return Pair(minT, minS)
    }

    private fun median(sorted: List<Int>): Int {
        if (sorted.size % 2 == 1)
            return sorted[sorted.size / 2]
        return (sorted[sorted.size / 2] + sorted[sorted.size / 2 +1]) / 2
    }

    private fun constCost(sorted: List<Int>, test: Int): Int {
        return sorted.sumOf { abs(test - it) }
    }

    private fun incrCost(sorted: List<Int>, test: Int): Int {
        return sorted.sumOf { i -> val t = abs(test - i); (t + 1) * t / 2 }
    }
}