import kotlin.math.abs

data class Point2(var x: Int, var y: Int)
fun main() {
    val input = java.io.File("src/main/kotlin", "15.txt")
        .readText()
        .split("${System.lineSeparator()}")
        .map{it.split(" ")}.map{object{
            val sensor = Point2(it[2].split("=")[1].trim(',').toInt(), it[3].split("=")[1].trim(':').toInt())
            val beacon = Point2(it[8].split("=")[1].trim(',').toInt(), it[9].split("=")[1].toInt())
        }}.map{object {
            val sensor = it.sensor
            val beacon = it.beacon
            val manhattan = abs(sensor.x - beacon.x) + abs(sensor.y - beacon.y)
        }}


    for (item in input) {
        var offset = 0
        for (y in item.sensor.y  - item.manhattan until item.sensor.y + item.manhattan + 1) {
            val min = item.sensor.x - offset
            val max = item.sensor.x + offset

//            for (x in item.sensor.x - offset until item.sensor.x + offset + 1) {
//                if (!(x == item.sensor.x && y == item.sensor.y) && !(x == item.beacon.x && y == item.beacon.y) ){
//                    if (y == 10 && !xs.contains(x)) {
//                        c++
//                        xs.add(x)
//                    }
//                }
//            }
            if (y < item.sensor.y) offset++
            else offset--
        }
    }
}