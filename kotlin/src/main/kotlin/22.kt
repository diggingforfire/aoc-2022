fun main() {
    val input = java.io.File("src/main/kotlin", "22.txt")
        .readText().split("${System.lineSeparator()}${System.lineSeparator()}")

    val map = input[0].split(System.lineSeparator()).map { it.split("").filter { it != "" }.toMutableList()}
    val maxWidth = map.maxOf { it.size }

    for (row in map) {
        val n = maxWidth - row.size - 1
        if (n > 0) {
            row.addAll(" ".repeat(n).split(" "))
        }
    }

    val path = "(\\d+)|([RL])".toRegex().findAll(input[1]).map { it.groupValues[0] }.toList()
    var direction = Direction.RIGHT
    val directionValues = enumValues<Direction>()

    var x = map[0].indexOfFirst { it != " " }
    var y = 0

    map[y][x] = "o"

    for (instruction in path) {
        when (instruction) {
            "L" -> direction = directionValues[Math.floorMod(direction.ordinal - 1, directionValues.size)]
            "R" -> direction = directionValues[Math.floorMod(direction.ordinal + 1, directionValues.size)]
            else -> {
                var offsetX = 0
                var offsetY = 0

                when (direction) {
                    Direction.UP -> offsetY = -1
                    Direction.RIGHT -> offsetX = 1
                    Direction.DOWN -> offsetY = 1
                    Direction.LEFT -> offsetX = -1
                }

                for (m in 1..instruction.toInt()) {
                    var newY = y + offsetY
                    var newX = x + offsetX

                    if (newX >= 0 && newX < map[0].size &&
                        newY >= 0 && newY < map.size &&
                        map[newY][newX] != "#") {

                        if (map[newY][newX] == "" || map[newY][newX] == " ") {
                            if (offsetY != 0) {
                                var tmp = map.map{it[x]}
                                if (offsetY < 0) {
                                    tmp = tmp.reversed()
                                }

                                newY = tmp.takeWhile { it != "#" }.indexOfFirst { it == "." }

                                if (offsetY < 0) {
                                    newY = tmp.size - newY - 1
                                }
                            }
                            if (offsetX != 0) {
                                var tmp = map[y]
                                if (offsetX < 0) {
                                    tmp = tmp.reversed().toMutableList()
                                }

                                newX = tmp.takeWhile { it != "#" }.indexOfFirst { it == "." }

                                if (offsetX < 0) {
                                    newX = tmp.size - newX - 1
                                }
                            }
                        }

                        if (newX == -1 || newY == -1 || map[newY][newX] == "#") continue

                        map[y][x] = "."

                        x = newX
                        y = newY

                        map[y][x] = "o"
                        //debug(map)
                    }
                }
            }
        }
    }


    println( (1000 * (y+1)) + (4 * (x+1)) + direction.facingValue())
}

enum class Direction {
    UP { override fun facingValue() = 3 },
    RIGHT { override fun facingValue() = 0 },
    DOWN { override fun facingValue() = 1 },
    LEFT { override fun facingValue() = 2 };
    abstract fun facingValue(): Int
}
fun debug(map: List<List<String>>) {
    for (row in map) {
        for (c in row) {
            print(c)
        }
        println()
    }
    println()
}