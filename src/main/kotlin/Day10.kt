import java.nio.file.Files
import kotlin.streams.toList

fun main() {
    Day10().start()
}

class Day10 {
    fun start() {
        val inFile = Utils.inPath("day10.txt")

        val stream = Files.lines(inFile)
        val lines = stream.use { s ->
            s.toList()
        }

        val part1 = totalIncorrect(lines)
        println("Part 1: $part1")

        val part2 = autocomplete(lines)
        println("Part 2: $part2")
    }

    private val scores = mapOf(Pair(')', 3), Pair(']', 57), Pair('}', 1197), Pair('>', 25137))

    private fun totalIncorrect(lines: List<String>): Int {
        val counts = HashMap<Char, Int>()
        lines.mapNotNull { findIncorrect(it) }.forEach { counts[it] = counts.getOrDefault(it, 0) + 1}
        return counts.entries.sumOf { scores[it.key]!! * it.value }
    }

    private val completeScore = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
    private fun autocomplete(lines: List<String>): Long {
        val allScores = lines.mapNotNull { complete(it) }.map { lineScore(it) }.sorted()
        return allScores[allScores.size / 2]
    }

    private fun lineScore(ac: String): Long {
        var score = 0L
        for (c in ac) {
            score *= 5L
            score += completeScore[c]!!
        }
        return score
    }

    private val opens = setOf('(', '{', '[', '<')
    private val matchClosing = mapOf(Pair('{', '}'), Pair('(', ')'), Pair('<', '>'), Pair('[', ']'))

    private fun complete(line: String): String? {
        val stack = ArrayDeque<Char>()
        for (c in line) {
            if (opens.contains(c)) {
                stack.addFirst(c)
            } else {
                // should be closing
                val open = stack.removeFirst()
                if (c != matchClosing[open]) {
                    return null
                }
            }
        }
        return stack.map { matchClosing[it] }.joinToString(separator = "")
    }

    private fun findIncorrect(line: String): Char? {
        var illegal: Char? = null
        val stack = ArrayDeque<Char>()
        for (c in line) {
            if (opens.contains(c)) {
                stack.addFirst(c)
            } else {
                // should be closing
                val open = stack.removeFirst()
                if (c != matchClosing[open]) {
                    illegal = c
                    break
                }
            }
        }
        return illegal
    }
}