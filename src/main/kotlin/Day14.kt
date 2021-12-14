import java.nio.file.Files

fun main() {
    Day14().start()
}

class Day14 {
    fun start() {
        val inFile = Utils.inPath("day14.txt")

        val reader = Files.newBufferedReader(inFile)
        val (poly, map) = reader.use {
            val p = reader.readLine()!!
            reader.readLine()
            val m = mutableMapOf<String, Char>()
            while (reader.ready()) {
                val sl = reader.readLine().split(" -> ")
                m[sl[0]] = sl[1][0]
            }
            p to m.toMap()
        }

        val eval1 = evalN(poly, map)
        val res1 = walk(poly, map)
        println("Part 1: BruteForce=${diffMostAndLeast(eval1)}, DP=$res1")

        val part2 = walk(poly, map, 40)
        println("Part 2: $part2")
    }

    private fun walk(poly: String, charMap: Map<String, Char>, steps: Int = 10): Any {
        val cacheByStep = Array(steps + 1) { mutableMapOf<String, Map<Char, Long>>() }

        fun walkDfs(p: String, step: Int): Map<Char, Long> {
            if (step == 0) {
                return histogram(p)
            }
            val cache = cacheByStep[step]
            if (cache.containsKey(p)) {
                // println("Cache: ($step,$p) -> ${cache[p]}")
                return cache.getValue(p)
            }
            val mid = charMap.getValue(p)

            val map = mutableMapOf<Char, Long>()
            for (pg in listOf("" + p[0] + mid, "" + mid + p[1])) {
                val childMap = walkDfs(pg, step - 1)
                for ((ch, n) in childMap.entries) {
                    map[ch] = map.getOrDefault(ch, 0) + n
                }
            }
            // remove mid element
            map[mid] = map[mid]!! - 1L

            // println("Compute: ($step,$p) -> $map")
            cache[p] = map.toMap()
            return map
        }

        val counts = mutableMapOf<Char, Long>()
        for (ci in 0 until poly.length - 1) {
            val pair = poly.subSequence(ci, ci + 2)
            val pairMap = walkDfs(pair.toString(), steps)
            for ((ch, n) in pairMap.entries) {
                counts[ch] = counts.getOrDefault(ch, 0) + n
            }
            if (ci > 0) {
                counts[poly[ci]] = counts[poly[ci]]!! - 1L
            }
        }
        // println(counts)
        return counts.entries.maxOf { it.value } - counts.entries.minOf { it.value }
    }

    // Brute force it for part 1
    private fun evalN(poly: String, map: Map<String, Char>): String {
        var prev = poly
        for (i in 0 until 10) {
            val next = StringBuffer(2 * prev.length - 1)
            for (ci in 0 until prev.length - 1) {
                val pair = prev.subSequence(ci, ci + 2)
                next.append(prev[ci])
                next.append(map[pair])
            }
            next.append(prev.last())
            prev = next.toString()
        }
        return prev
    }

    private fun histogram(str: String): Map<Char, Long> {
        val hist = HashMap<Char, Long>()
        for (c in str) {
            hist[c] = hist.getOrDefault(c, 0) + 1
        }
        return hist
    }

    private fun diffMostAndLeast(str: String): Long {
        val hist = histogram(str)
        // println(hist)
        return hist.entries.maxOf { it.value } - hist.entries.minOf { it.value }
    }
}
