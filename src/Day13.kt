fun main() {
    fun getFoldedMatrix(
        points: MutableList<MutableList<Int>>,
        folds: List<Pair<String, Int>>
    ): Array<BooleanArray> {
        for (fold in folds) {
            for (point in points) {
                if (fold.first == "x") {
                    val pointIndex = 0
                    if (point[pointIndex] > fold.second) {
                        if (fold.second - (point[pointIndex] - fold.second) < 0) {
                            throw Error("NOK")
                        }
                        point[pointIndex] = fold.second - (point[pointIndex] - fold.second)
                    } else if (point[pointIndex] == fold.second) {
                        point[0] = -1
                    }
                } else {
                    val pointIndex = 1
                    if (point[pointIndex] > fold.second) {
                        if (fold.second - (point[pointIndex] - fold.second) < 0) {
                            throw Error("NOK")
                        }
                        point[pointIndex] = fold.second - (point[pointIndex] - fold.second)
                    } else if (point[pointIndex] == fold.second) {
                        point[0] = -1
                    }
                }
            }
            points.removeAll { it[0] < 0 }
        }
        var maxX = points.map { it[0] }.maxOrNull()!!
        var maxY = points.map { it[1] }.maxOrNull()!!
        val foldedMatrix = Array(maxX + 1) { BooleanArray(maxY + 1) { false } }
        for (point in points) {
            if (point[0] >= 0) {
                foldedMatrix[point[0]][point[1]] = true
            }
        }
        return foldedMatrix
    }

    fun part1(input: List<String>): Int {
        val indexOfEmptyLine = input.indexOfFirst { it.isEmpty() }
        val points =
            input.subList(0, indexOfEmptyLine).map { it.split(",").map { it.toInt() }.toMutableList() }.toMutableList()
        val folds = input.subList(indexOfEmptyLine + 1, input.size).map { it.split("=") }
            .map { (a, b) -> Pair(a.split(" ").last(), b.toInt()) }.subList(0, 1)
        val foldedMatrix = getFoldedMatrix(points, folds)
        return foldedMatrix.map { line -> line.count { it } }.sum()
    }

    fun part2(input: List<String>) {
        val indexOfEmptyLine = input.indexOfFirst { it.isEmpty() }
        val points =
            input.subList(0, indexOfEmptyLine).map { it.split(",").map { it.toInt() }.toMutableList() }.toMutableList()
        val folds = input.subList(indexOfEmptyLine + 1, input.size).map { it.split("=") }
            .map { (a, b) -> Pair(a.split(" ").last(), b.toInt()) }
        val foldedMatrix = getFoldedMatrix(points, folds)
        foldedMatrix.map { println(it.map { if (it) "#" else "." }.joinToString(" ")) }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13.test")
    check(part1(testInput) == 17)

    val input = readInput("Day13")
    prcp(part1(input))
    (part2(input))
}