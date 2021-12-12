fun main() {
    class Cave(val name: String, val maxVisits: Int, val end: Boolean = false) {
    }

    fun isEndNode(currentNode: Cave) = currentNode.end
    fun findAllPathsRec(
        nodeMap: Map<Cave, List<Cave>>,
        currentNode: Cave,
        blockedSet: Map<Cave, Int>,
        usedDouble: Boolean = false
    ): Int {
        if (isEndNode(currentNode)) {
            return 1
        }
        var bestPath: List<Cave>? = null
        var nbPaths = 0
        for (connectedCave in nodeMap[currentNode]!!) {
            var newUsedDouble = usedDouble
            if (connectedCave.name == "start") {
                continue
            }
            if ((blockedSet[connectedCave] ?: 0) >= connectedCave.maxVisits) {
                if (usedDouble) {
                    continue;
                } else {
                    newUsedDouble = true;
                }
            }
            var newBlockedSet = blockedSet
            if (connectedCave.maxVisits < Int.MAX_VALUE) {
                newBlockedSet = blockedSet.toMutableMap()
                newBlockedSet.set(connectedCave, (blockedSet[connectedCave] ?: 0) + 1)
                newBlockedSet = newBlockedSet.toMap()
            }
            val newNbPaths = findAllPathsRec(
                nodeMap,
                connectedCave,
                newBlockedSet,
                newUsedDouble
            )
            nbPaths += newNbPaths
        }
        return nbPaths
    }

    fun addConnection(connections: HashMap<Cave, ArrayList<Cave>>, cave1: Cave, cave2: Cave) {
        var list = connections[cave1]
        if (list == null) {
            list = ArrayList();
            connections[cave1] = list
        }
        list.add(cave2)
    }

    fun toCave(connections: HashMap<Cave, ArrayList<Cave>>, name: String): Cave {
        var maxVisits = Int.MAX_VALUE;
        if (name[0].isLowerCase()) {
            maxVisits = 1
        }
        return connections.keys.find { it.name == name } ?: Cave(name, maxVisits, name == "end")
    }

    fun createNodeMap(input: List<String>): Map<Cave, ArrayList<Cave>> {
        val connections = HashMap<Cave, ArrayList<Cave>>()
        for (line in input) {
            val (first, second) = line.split("-")
            val cave1 = toCave(connections, first)
            val cave2 = toCave(connections, second)
            addConnection(connections, cave1, cave2)
            addConnection(connections, cave2, cave1)
        }
        return connections
    }

    fun part1(input: List<String>): Int {
        val nodeMap = createNodeMap(input)
        val startCave = nodeMap.keys.find { it.name == "start" }!!
        val endCave = nodeMap.keys.find { it.name == "end" }!!
        val nbPaths = findAllPathsRec(nodeMap, startCave, mapOf(startCave to 1))
        return nbPaths
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:

    check(part1(readInput("Day12.test")) == 36)
    check(part1(readInput("Day12.test2")) == 103)
    check(part1(readInput("Day12.test3")) == 3509)
//    check(part2(readInput("Day12.test3")) == 226)
    val input = readInput("Day12")
    prcp(part1(input))
    prcp(part2(input))
}
