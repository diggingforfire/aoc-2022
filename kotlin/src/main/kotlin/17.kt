fun solve(maxBricks: ULong, visualize: Boolean, showProgress: Boolean) : Int {
    val pattern = java.io.File("src/main/kotlin", "17.txt").readText().toCharArray()

    val bricks = getBricks()
    var currentBrick: Array<CharArray>
    var jet: Char

    var cave = mutableListOf (
        charArrayOf('.', '.', '.', '.', '.', '.', '.'),
        charArrayOf('.', '.', '.', '.', '.', '.', '.'),
        charArrayOf('.', '.', '.', '.', '.', '.', '.'),
        charArrayOf('.', '.', '.', '.', '.', '.', '.'),
    )

    var jetCounter = 0
    for (round in 0UL until maxBricks) {
        if (showProgress) {
            if (round % (maxBricks / 100UL) == 0UL) {
                println("${(round.toFloat()/maxBricks.toFloat()) * 100}%")
            }
        }

        for (i in cave.indices) {
            if (cave[i].all { it == '#' }) {
                println("Floor detected at row ${i+1} - round $round")
                //println(getHighest(cave))

                //cave = cave.drop(i + 1).toMutableList()
                return 0
            }
        }

        currentBrick = bricks[(((round % bricks.size.toUInt()).toInt()))]

        var hasLanded = false
        var offsetX = 2
        var offsetY = 3 + getHighest(cave)

        while (!hasLanded) {
            // spawn
            createBrick(offsetY, currentBrick, offsetX, cave, false)

            // push
            jet = pattern[(jetCounter % pattern.size.toLong()).toInt()]

            if (jet == '>' && offsetX + currentBrick[0].size + 1 <= 7 && !willCollideHorizontally(1, offsetY, currentBrick.size, cave))  {
                offsetX++
            } else if (jet == '<' && offsetX - 1 >= 0 && !willCollideHorizontally(-1, offsetY, currentBrick.size, cave)) {
                offsetX--
            }

            clearBrick(offsetY, currentBrick, cave)
            createBrick(offsetY, currentBrick, offsetX, cave, false)

            hasLanded = offsetY == 0 || willCollideVertically(offsetY, currentBrick.size, cave)

            clearBrick(offsetY, currentBrick, cave)
            if (!hasLanded) offsetY--
            createBrick(offsetY, currentBrick, offsetX, cave, hasLanded)

            if (visualize) visualize(cave)

            jetCounter++
        }
    }

    return getHighest(cave)
}



fun createBrick(
    offsetY: Int,
    currentBrick: Array<CharArray>,
    offsetX: Int,
    cave: MutableList<CharArray>,
    setInStone: Boolean
)  {
    var y = offsetY
    var row: CharArray
    for (line in currentBrick.reversed()) {
        var x = offsetX
        row = getRow(y++, cave)
        for (char in line) {
            if (row[x] == '.') {
                if (setInStone) {
                    row[x] = if (char == '@') '#' else '.'
                } else {
                    row[x] = char
                }
            }
            x++
        }
    }
}

fun clearBrick(
    offsetY: Int,
    currentBrick: Array<CharArray>,
    cave: MutableList<CharArray>
) {
    for (y in offsetY until offsetY + currentBrick.size) {
        val rowToClear = getRow(y, cave)
        for (yyy in rowToClear.indices) if (rowToClear[yyy] == '@') rowToClear[yyy] = '.'
    }
}

fun getHighest(cave: List<CharArray>) : Int {
    for (y in cave.size - 1 downTo 0) {
        if (cave[y].any{it == '#'}) return y + 1
    }
    return 0
}

fun visualize(cave: List<CharArray>) {
    println()

    for (chars in cave.reversed()) {
        print("|")
        for (char in chars) print(char)
        print("|")
        println()
    }
    println("+-------+")
}

fun getBricks() : Array<Array<CharArray>> {
    val brick1 = arrayOf (charArrayOf('@', '@', '@', '@'))
    val brick2 = arrayOf(
        charArrayOf('.', '@', '.'),
        charArrayOf('@', '@', '@'),
        charArrayOf('.', '@', '.')
    )
    val brick3 = arrayOf(
        charArrayOf('.', '.', '@'),
        charArrayOf('.', '.', '@'),
        charArrayOf('@', '@', '@')
    )
    val brick4 = arrayOf(
        charArrayOf('@'),
        charArrayOf('@'),
        charArrayOf('@'),
        charArrayOf('@')
    )

    val brick5 = arrayOf(
        charArrayOf('@', '@'),
        charArrayOf('@', '@')
    )

    return arrayOf(brick1, brick2, brick3, brick4, brick5)
}

fun getRow(offsetY: Int, cave: MutableList<CharArray>): CharArray {
    if (offsetY >= cave.size) {
        cave.add(charArrayOf('.', '.', '.', '.', '.', '.', '.'))
        cave.add(charArrayOf('.', '.', '.', '.', '.', '.', '.'))
        cave.add(charArrayOf('.', '.', '.', '.', '.', '.', '.'))
        cave.add(charArrayOf('.', '.', '.', '.', '.', '.', '.'))
    }
    return cave[offsetY]
}

fun willCollideHorizontally(offsetX: Int, offsetY: Int, currentBrickSize: Int, cave: MutableList<CharArray>) : Boolean {
    for (y in offsetY until offsetY + currentBrickSize) {
        val roww = getRow(y, cave)
        for (yyy in roww.indices) {
            if (roww[yyy] == '@' && cave[y][yyy + offsetX] == '#' ) {
                return true
            }
        }
    }
    return false
}
fun willCollideVertically(offsetY: Int, currentBrickSize: Int, cave: MutableList<CharArray>): Boolean {
    for (y in offsetY until offsetY + currentBrickSize) {
        val roww = getRow(y, cave)
        for (yyy in roww.indices) {
            if (roww[yyy] == '@' && cave[y-1][yyy] == '#' ) {
                return true
            }
        }
    }
    return false
}
fun part1() {
    println(solve(2022UL, false, true))
}
fun part2() {
    println(solve(1000000000000UL, false, true))
}

fun main() {
    //part1()
    part2()
}