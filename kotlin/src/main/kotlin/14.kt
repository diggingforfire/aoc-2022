data class Point(var x: Int, var y: Int)
fun main() {
    fun getFullPath(from: Point, to: Point) : List<Point>  {
        val list = mutableListOf(from, to)

        val xs = kotlin.math.abs(from.x - to.x)
        val ys = kotlin.math.abs(from.y - to.y)

        if (xs > 0) {
            for (x in kotlin.math.min(from.x, to.x) + 1 until kotlin.math.max(from.x, to.x) ) {
                list.add(Point(x, from.y))
            }
        }
        else if (ys > 0) {
            for (y in kotlin.math.min(from.y, to.y) + 1 until kotlin.math.max(from.y, to.y) ) {
                list.add(Point(from.x, y))
            }
        }
        return list
    }

    fun getPaths() : List<List<List<Point>>> {
        return java.io.File("src/main/kotlin", "14.txt")
            .readText()
            .split("${System.lineSeparator()}")
            .map{it.split(" -> ").map{it.split(",")}.map{
                Point(it[0].toInt(), it[1].toInt())}}
            .map { it.windowed(2, 1) }
            .map { it.map { getFullPath(it[0], it[1]) } }
    }

    fun solve(condition: (sand: Point, maxY: Int) -> Boolean): Int {
        var paths = getPaths().flatMap { it.flatMap { it } }.toSet()

        val maxY = paths.maxOf { it.y }
        val gridSize = 5000
        val grid = Array(maxY+2) { BooleanArray(gridSize)}

        val start = gridSize / 2
        for (point in paths) grid[point.y][start + point.x] = true

        var i = 0
        while (true) {
            val sand = Point(500, 0)

            var falling = true;

            while (falling) {
                if (sand.y + 1 < maxY + 2 && ! grid[sand.y + 1][start + sand.x]) {
                    sand.y++
                } else if (sand.y + 1 < maxY + 2  && !grid[sand.y + 1][start + sand.x - 1]) {
                    sand.x -= 1
                    sand.y++
                } else if (sand.y + 1 < maxY + 2  && !grid[sand.y + 1][start + sand.x + 1]) {
                    sand.x += 1
                    sand.y++
                } else {
                    falling = false
                    i++
                }

                if (condition(sand, maxY)) {
                    return i
                }
            }

            grid[sand.y][start + sand.x] = true

        }
    }

    fun part1(): Int {
        return solve { sand, maxY -> sand.y == maxY }
    }

    fun part2(): Int {
        return solve { sand, _ -> sand.x == 500 && sand.y == 0}
    }

    println(part1())
    println(part2())
}