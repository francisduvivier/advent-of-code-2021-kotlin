fun main() {
    class Cave(val name: String, val big: Boolean, val end: Boolean = false) {
    }

    fun isEndNode(currentNode: Cave) = currentNode.end
    fun findAllPathsRec(
        nodeMap: Map<Cave, List<Cave>>,
        currentPath: List<Cave>,
        blockedSet: Set<Cave>
    ): Pair<List<Cave>?, Int> {
        val currentNode = currentPath.last()
        if (isEndNode(currentNode)) {
            return Pair(currentPath, 1)
        }
        var bestPath: List<Cave>? = null
        var nbPaths = 0
        for (connectedCave in nodeMap[currentNode]!!) {
            if (blockedSet.contains(connectedCave)) {
                continue;
            }
            val newPath = currentPath.toMutableList()
            newPath.add(connectedCave)
            var newBlockedSet = blockedSet
            if (!connectedCave.big) {
                newBlockedSet = blockedSet.toMutableSet()
                newBlockedSet.add(connectedCave)
                newBlockedSet = newBlockedSet.toSet()
            }
            val (newBestPath, newNbPaths) = findAllPathsRec(
                nodeMap,
                newPath.toList(),
                newBlockedSet
            )
            nbPaths += newNbPaths
            if (newBestPath != null && newBestPath.size < (bestPath?.size ?: Int.MAX_VALUE)) {
                bestPath = newBestPath
            }
        }
        return Pair(bestPath, nbPaths)
    }

    fun addConnection(connections: HashMap<Cave, ArrayList<Cave>>, cave1: Cave, cave2: Cave) {
        var list = connections[cave1]
        if (list == null) {
            list = ArrayList();
            connections[cave1] = list
        }
        list.add(cave2)
    }

    fun createNodeMap(input: List<String>): Map<Cave, ArrayList<Cave>> {
        val connections = HashMap<Cave, ArrayList<Cave>>()
        for (line in input) {
            val (first, second) = line.split("-")
            val cave1 = Cave(first, first[0].isUpperCase(), first == "end")
            val cave2 = Cave(first, first[0].isUpperCase(), first == "end")
            addConnection(connections, cave1, cave2)
            addConnection(connections, cave2, cave1)
        }
        return connections
    }

    fun part1(input: List<String>): Int {
        val nodeMap = createNodeMap(input)
        val startCave = nodeMap.keys.find { it.name == "start" }!!
        val startList = List(1) { startCave }
        var (_, nbPaths) = findAllPathsRec(nodeMap, startList, startList.toSet())
        return nbPaths
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:

    check(part1(readInput("Day12.test")) == 10)
    check(part1(readInput("Day12.test2")) == 19)
    check(part1(readInput("Day12.test3")) == 226)
    val input = readInput("Day12")
    prcp(part1(input))
    prcp(part2(input))
}
