import java.nio.file.Files
import kotlin.streams.toList

fun main() {
    val inFile = Utils.inPath("day3.txt")
    val cols = 12

    val input = Files.lines(inFile)
        .toList()

    val part1 = powerConsumption(input, cols)
    println("Part 1: $part1")

    val lifeSupport = ratings(input, cols) { ones, total -> if (ones >= total - ones) '1' else '0'}
    val co2Scrubber = ratings(input, cols) { ones, total -> if (ones >= total - ones) '0' else '1'}
    println("Part 2: $lifeSupport x $co2Scrubber = ${lifeSupport * co2Scrubber}")
}

fun powerConsumption(list: List<String>, cols: Int): Int {
    val onesAcc = list.fold(Array(cols) { 0 })
    { acc, line -> line.forEachIndexed { i, c -> acc[i] += if (c == '1') 1 else 0 }; acc }
    val half = list.size / 2
    val gamma = onesAcc.joinToString(separator = "") { if (it > half) "1" else "0" }.toInt(2)
    val eps = onesAcc.joinToString(separator = "") { if (it > half) "0" else "1" }.toInt(2)
    return gamma * eps
}

fun ratings(list: List<String>, cols: Int, filter: (ones: Int, total: Int) -> Char): Int {
    val includes = list.indices.toMutableSet()
    for (i in 0 until cols) {
        val ones = list
            .filterIndexed { idx, _ -> includes.contains(idx) }
            .fold(0) { acc, line -> val c = if (line[i] == '1') 1 else 0; acc + c }
        val toFilter = filter(ones, includes.size)
        list.forEachIndexed { idx, line ->
            if (line[i] != toFilter) {
                includes.remove(idx)
            }
        }
        if (includes.size == 1) {
            return list[includes.first()].toInt(2)
        }
    }
    return -1
}
