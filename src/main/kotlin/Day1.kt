import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.toList

fun main(args: Array<String>) {
    val inFile = Path.of(args[0])

    val numbers = Files.lines(inFile)
        .mapToInt { it.toInt() }
        .toList()

    println("Part 1: ${numIncreasing(numbers)}")

    val numsWindow = slidingWindow(numbers)
    println("Part 2: ${numIncreasing(numsWindow)}")
}

data class AccPrev(val prev: Int, val count: Int)

fun numIncreasing(nums: List<Int>): Int {
    return nums.fold(AccPrev(nums[0], 0)) { acc: AccPrev, n: Int ->
        if (n > acc.prev) AccPrev(n, acc.count + 1) else acc.copy(prev = n)
    }.count
}

fun slidingWindow(nums: List<Int>): List<Int> {
    return (0..nums.size - 3)
        .map { i -> (i..(i + 2)).sumOf { j -> nums[j] } }
}

