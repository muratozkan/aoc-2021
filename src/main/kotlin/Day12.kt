import java.nio.file.Files

fun main() {
    Day12().start()
}

private class Graph {
    private val adjMap = mutableMapOf<String, MutableSet<String>>()

    fun addEdge(from: String, to: String) {
        adjMap.computeIfAbsent(from) { mutableSetOf() }.add(to)
        adjMap.computeIfAbsent(to) { mutableSetOf() }.add(from)
    }

    fun paths(start: String, end: String): Int {
        val allPaths = mutableListOf<List<String>>()
        dfsPaths(start, end, listOf(start), setOf(), allPaths)
        return allPaths.size
    }

    private fun dfsPaths(
        curr: String,
        end: String,
        path: List<String>,
        seen: Set<String>,
        allPaths: MutableList<List<String>>
    ) {
        if (curr == end) {
            allPaths.add(path)
            return
        }
        var nowSeen = seen
        if (curr[0].isLowerCase()) {
            nowSeen = seen + curr
        }
        for (c in adjMap.getValue(curr)) {
            if (!nowSeen.contains(c)) {
                dfsPaths(c, end, path + end, nowSeen, allPaths)
            }
        }
    }

    fun paths2(start: String, end: String): Any {
        val allPaths = mutableListOf<List<String>>()
        dfsPaths2(start, end, listOf(start), mapOf(start to 2, end to 0), allPaths)
        return allPaths.size
    }

    private fun dfsPaths2(
        curr: String,
        end: String,
        path: List<String>,
        seen: Map<String, Int>,
        allPaths: MutableList<List<String>>
    ) {
        if (curr == end) {
            allPaths.add(path)
            return
        }
        var nowSeen = seen
        if (curr[0].isLowerCase()) {
            nowSeen = seen + (curr to seen.getOrDefault(curr, 0) + 1)
        }
        val noneSeenTwice = nowSeen.values.none { it == 2 }
        for (c in adjMap.getValue(curr)) {
            val times = nowSeen.getOrDefault(c, 0)
            if (times == 0 || (noneSeenTwice && times == 1)) {
                dfsPaths2(c, end, path + end, nowSeen, allPaths)
            }
        }
    }
}


class Day12 {

    fun start() {
        val inFile = Utils.inPath("day12.txt")

        val graph = Graph()

        val stream = Files.lines(inFile)
        stream.use { s ->
            s.forEach { val ss = it.split("-"); graph.addEdge(ss[0], ss[1]) }
        }

        val part1 = graph.paths("start", "end")
        println("Part 1: $part1")

        val part2 = graph.paths2("start", "end")
        println("Part 2: $part2")
    }

}