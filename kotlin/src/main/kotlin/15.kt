import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Point(var x: Int, var y: Int)
data class Triangle(var p1: Point, var p2: Point, var p3: Point)

// ===================================
// https://stackoverflow.com/a/2049593
fun sign (p1: Point, p2: Point, p3: Point) : Float {
    return (p1.x.toFloat() - p3.x.toFloat()) * (p2.y.toFloat() - p3.y.toFloat()) - (p2.x.toFloat() - p3.x.toFloat()) * (p1.y.toFloat() - p3.y.toFloat())
}

fun pointInTriangle(point: Point, triangle: Triangle) : Boolean {
    val d1 = sign(point, triangle.p1, triangle.p2)
    val d2 = sign(point, triangle.p2, triangle.p3)
    val d3 = sign(point, triangle.p3, triangle.p1)

    val hasNeg = (d1 < 0) || (d2 < 0) || (d3 < 0)
    val hasPos = (d1 > 0) || (d2 > 0) || (d3 > 0)

    return !(hasNeg && hasPos)
}
// ===================================

fun part1WithTriangles() : Int {
    val sensorsAndBacons = java.io.File("src/main/kotlin", "15.txt")
        .readText()
        .split("${System.lineSeparator()}")
        .map{it.split(" ")}.map{object{
            val sensor = Point(it[2].split("=")[1].trim(',').toInt(), it[3].split("=")[1].trim(':').toInt())
            val bacon = Point(it[8].split("=")[1].trim(',').toInt(), it[9].split("=")[1].toInt())
        }}.map{object {
            val sensor = it.sensor
            val bacon = it.bacon
            val manhattan = abs(sensor.x - bacon.x) + abs(sensor.y - bacon.y)
        }}

    val triangles = mutableListOf<Triangle>()

    var totalMinX = 0
    var totalMaxX = 0
    var totalminY = 0
    var totalMaxY = 0

    for (snb in sensorsAndBacons) {
        val minX = snb.sensor.x - snb.manhattan
        val maxX = snb.sensor.x + snb.manhattan
        val minY = snb.sensor.y - snb.manhattan
        val maxY = snb.sensor.y + snb.manhattan

        if (minX < totalMinX) totalMinX = minX
        if (maxX > totalMaxX) totalMaxX = maxX
        if (minY < totalminY) totalminY = minY
        if (maxY > totalMaxY) totalMaxY = maxY

        val left = Point(minX, snb.sensor.y)
        val top = Point(snb.sensor.x, minY)
        val right = Point(maxX, snb.sensor.y)
        val bottom = Point(snb.sensor.x, maxY)

        val t1 = Triangle(top, left, bottom)
        val t2 = Triangle(top, right, bottom)

        triangles.addAll(listOf(t1, t2))
    }

    var noBacons = 0
    val y = 2000000
    val p = Point(0, y)
    val allPoints = sensorsAndBacons.map{it.sensor} + sensorsAndBacons.map{it.bacon}

    for (x in totalMinX until totalMaxX + 1) {
        p.x = x

        for (triangle in triangles) {
            if (pointInTriangle(p, triangle) && !allPoints.contains(p)) {
                noBacons++
                break
            }
        }
    }

    return noBacons
}

fun part2(rows: Int, threads: Int) {
    val sensorsAndBacons = java.io.File("src/main/kotlin", "15.txt")
        .readText()
        .split("${System.lineSeparator()}")
        .map{it.split(" ")}.map{object{
            val sensor = Point(it[2].split("=")[1].trim(',').toInt(), it[3].split("=")[1].trim(':').toInt())
            val bacon = Point(it[8].split("=")[1].trim(',').toInt(), it[9].split("=")[1].toInt())
        }}.map{object {
            val sensor = it.sensor
            val bacon = it.bacon
            val manhattan = abs(sensor.x - bacon.x) + abs(sensor.y - bacon.y)
            val minY = it.sensor.y - manhattan
            val maxY = it.sensor.y + manhattan
            var fromX = it.sensor.x - manhattan
            var toX = it.sensor.x + manhattan
        }}

    val chunkSize = rows / threads
    for (i in 0 until threads) {
        thread {
            for (y in i * chunkSize until (i+1) * chunkSize) {
                val arr = BooleanArray(rows)
                for (snb in sensorsAndBacons) {
                    if (y in snb.minY..snb.maxY) {
                        val dY = abs(snb.sensor.y - y)
                        val range = max(snb.fromX + dY, 0)..min(snb.toX - dY, rows - 1)
                        for (x in range) arr[x] = true
                    }
                }

                val sbs = sensorsAndBacons.map{it.sensor} + sensorsAndBacons.map{it.bacon}
                val snbsToRemove = sbs.filter{it.y == y}.distinctBy { it.x }.map{ it.x}

                for (x in snbsToRemove) {
                    if (x in 0 until rows) {
                        arr[x] = true
                    }
                }

                val theX = arr.indexOfFirst { !it }
                if (theX != -1 ) {
                    print(theX.toULong() * rows.toULong() + y.toULong())
                    return@thread
                }
            }
        }
    }
}

fun main() {
    println(part1WithTriangles())
    part2(4000000, 20)
}