fun main() {
    fun part1(input: List<String>): Int {
        var depth = 0;
        var hor = 0;
        for (instuct in input) {
            val splitInstruct = instuct.split(" ")
            val opcode = splitInstruct[0]
            val amount = splitInstruct[1].toInt()
            if (opcode.equals("up")) {
                depth -= amount;
                if (depth < 0) {
                    throw Error("submarines can't fly")
                }
            } else if (opcode.equals("down")) {
                depth += amount;
            } else if (opcode.equals("forward")) {
                hor += amount;
            } else {
                throw Error("Invalid opcode")
            }
        }
        return depth * hor;
    }

    fun part2(input: List<String>): Int {
        var depth = 0;
        var hor = 0;
        var aim = 0;
        for (instuct in input) {
            val splitInstruct = instuct.split(" ")
            val opcode = splitInstruct[0]
            val amount = splitInstruct[1].toInt()
            if (opcode.equals("up")) {
                aim -= amount;
                if (depth < 0) {
                    throw Error("submarines can't fly")
                }
            } else if (opcode.equals("down")) {
                aim += amount;
            } else if (opcode.equals("forward")) {
                hor += amount;
                depth += aim * amount
            } else {
                throw Error("Invalid opcode")
            }
        }
        return depth * hor;
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02.test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    prcp(part1(input))
    prcp(part2(input))
}
