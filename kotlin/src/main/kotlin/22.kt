fun main() {
    val input = java.io.File("src/main/kotlin", "22.txt")
        .readText().split("${System.lineSeparator()}${System.lineSeparator()}")

    val map = input[0].split(System.lineSeparator())
    val path = input[1]

    println("hi")
}