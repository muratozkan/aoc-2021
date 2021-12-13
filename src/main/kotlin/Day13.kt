import kotlin.io.path.bufferedReader

fun main() {
    Day13().start()
}

class Day13 {

    data class Pos2(val x: Int, val y: Int)
    data class Fold(val axis: String, val at: Int)

    fun start() {
        val inFile = Utils.inPath("day13.txt")

        val reader = inFile.bufferedReader()
        val (dots, folds) = reader.use {
            val ds = mutableListOf<Pos2>()
            val fs = mutableListOf<Fold>()
            while (it.ready()) {
                val line = it.readLine()
                if (line.contains(",")) {
                    val (xs, ys) = line.split(",")
                    ds.add(Pos2(xs.toInt(), ys.toInt()))
                } else if (line.startsWith("fold along")) {
                    val (cs, vs) = line.replace("fold along ", "").split("=")
                    fs.add(Fold(cs, vs.toInt()))
                }
            }
            ds to fs
        }

        val part1 = firstFold(dots, folds).size
        println("Part 1: $part1")

        val part2 = allFolds(dots, folds)
        print(part2)
    }

    private fun allFolds(dots: List<Pos2>, folds: List<Fold>): Set<Pos2> {
        var set = dots.toSet()
        for (f in folds)
            set = set.map { fold(it, f) }.toSet()
        return set
    }

    private fun firstFold(dots: List<Pos2>, folds: List<Fold>): Set<Pos2> {
        return dots.map { fold(it, folds[0]) }.toSet()
    }

    private fun fold(p: Pos2, fold: Fold): Pos2 {
        return if (fold.axis == "x") {
            if (p.x < fold.at) p else Pos2(2 * fold.at - p.x, p.y)
        } else {
            if (p.y < fold.at) p else Pos2(p.x, 2 * fold.at - p.y)
        }
    }

    private fun print(dots: Set<Pos2>) {
        val mx = dots.maxOf { it.x }
        val my = dots.maxOf { it.y }

        for (y in 0..my) {
            for (x in 0..mx) {
                if (dots.contains(Pos2(x, y)))
                    print("#")
                else print(".")
            }
            println()
        }
        println("Total: ${dots.size}")
        println()
    }
}