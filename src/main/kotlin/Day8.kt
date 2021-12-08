import java.nio.file.Files
import kotlin.streams.toList

fun main() {
    Day8().start()
}

class Day8 {
    fun start() {
        val inFile = Utils.inPath("day8.txt")

        val stream = Files.lines(inFile)
        val lines = stream.use { s ->
            s.map { parseLine(it) }.toList()
        }

        val sizes = setOf(2, 3, 4, 7)
        val part1 = lines
            .sumOf { it.second.count { o -> sizes.contains(o.length) } }

        println("Part 1: $part1")

        val part2 = lines.sumOf { line ->
            val map = guess(line.first)
            val num = line.second.joinToString("") { map[it.toSet()].toString() }.toInt()
            num
        }

        println("Part 2: $part2")
    }

    private fun parseLine(line: String): Pair<List<String>, List<String>> {
        val (exs, outs) = line.split(" | ")
        return Pair(exs.split(" "), outs.split(" "))
    }

    private val basics = mapOf(Pair(0, 1), Pair(1, 7), Pair(2, 4), Pair(9, 8))

    private fun guess(first: List<String>): Map<Set<Char>, Int> {
        val digits = Array(10) { setOf<Char>() }
        val strToDigit = mutableMapOf<Set<Char>, Int>()

        val sorted = first.map { it.toSet() }.sortedBy { it.size }
        basics.forEach { (i, n) -> val s = sorted[i]; strToDigit[s] = n; digits[n] = s.toSet() }

        // len: 6
        for (i in 6..8) {
            val set = sorted[i]
            if (set.containsAll(digits[4])) {
                strToDigit[set] = 9
                digits[9] = set
            } else if (set.containsAll(digits[1])) {
                strToDigit[set] = 0
                digits[0] = set
            } else {
                strToDigit[set] = 6
                digits[6] = set
            }
        }

        // len: 5
        for (i in 3..5) {
            val set = sorted[i]
            if (set.containsAll(digits[1])) {
                strToDigit[set] = 3
                digits[3] = set
            } else if (digits[9] == set + digits[1]) {
                strToDigit[set] = 5
                digits[5] = set
            } else {
                strToDigit[set] = 2
                digits[2] = set
            }
        }
        return strToDigit
    }

}