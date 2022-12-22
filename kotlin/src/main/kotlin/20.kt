data class Number(
    var number: Long,
    var originalIndex: Int,
    var previous: Number? = null,
    var next: Number? = null) {

    override fun toString(): String = "$number"
}

fun solve(decryptionKey: Int, mixes: Int) : Long {
    val numbers = java.io.File("src/main/kotlin", "20.txt")
        .readLines().mapIndexed{i, it -> Number(number = it.toLong() * decryptionKey, originalIndex = i)
        }

    for (num in numbers) {
        val prevIndex = Math.floorMod(num.originalIndex - 1, numbers.size)
        val nextIndex = Math.floorMod(num.originalIndex + 1, numbers.size)
        num.previous = numbers[prevIndex]
        num.next = numbers[nextIndex]
    }

    for (m in 1..mixes) {
        for (index in numbers.indices) {
            val number = numbers.single{ it.originalIndex == index}
            val moves = number.number
            var target = number

            if (moves > 0) {
                for (m in 0 until Math.floorMod(moves, numbers.size - 1)) {
                    target = target.next!!
                }
                if (target == number) continue
                remove(number)

                val newNext = target.next

                newNext?.previous = number
                target.next = number
                number.previous = target
                number.next = newNext

            } else if (moves < 0) {
                for (m in 0 until Math.floorMod(numbers.size + moves, numbers.size - 1)) {
                    target = target.next!!
                }
                if (target == number) continue
                remove(number)

                val newPrevious = target.previous

                newPrevious?.next = number
                target.previous = number
                number.previous = newPrevious
                number.next = target
            }
        }
    }

    val zero = numbers.single{ it.number == 0L }
    var next = zero
    var sum = 0L

    for (i in 1..3000) {
        next = next.next!!
        if (i % 1000 == 0) {
            sum += next.number
        }
    }

    return sum
}

private fun remove(number: Number) {
    val previous = number.previous
    val next = number.next
    previous?.next = next
    next?.previous = previous
}

fun main() {
    println(solve(1, 1))
    println(solve(811589153, 10))
}