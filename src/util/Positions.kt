import kotlin.math.absoluteValue

class Pos3D(val x: Int, val y: Int, val z: Int) {
    constructor(list: List<Int>) : this(list[0], list[1], list[2])

    val all = listOf(x, y, z)
    override fun toString(): String {
        return all.joinToString(",")
    }

    fun diff(other: Pos3D): Pos3D {
        return Pos3D(all.mapIndexed { index, it -> it - other.all[index] })
    }

    fun orient(orientation: Orientation): Pos3D {
        return orientation.mutatePos(this);
    }

    fun add(other: Pos3D): Pos3D {
        return Pos3D(all.mapIndexed { index, it -> it + other.all[index] })
    }

    override fun hashCode(): Int {
        return this.toString().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        try {
            val otherPos = other as Pos3D
            return otherPos.diff(this).all.all { it == 0 }
        } catch (e: Error) {
            return false
        }
    }

    fun manHattan(): Int {
        return all.map { it.absoluteValue }.sum()
    }
}
