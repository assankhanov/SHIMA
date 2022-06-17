package com.example.hinvipskotlin

import kotlin.Float.Companion.MAX_VALUE
import kotlin.Float.Companion.MIN_VALUE
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


// Taken from RosettaCode



data class Edge(val s: PointF, val e: PointF) {
    operator fun invoke(p: PointF) : Boolean = when {
        s.y > e.y -> Edge(e, s).invoke(p)
        p.y == s.y || p.y == e.y -> invoke(PointF(p.x, p.y + epsilon))
        p.y > e.y || p.y < s.y || p.x > max(s.x, e.x) -> false
        p.x < min(s.x, e.x) -> true
        else -> {
            val blue = if (abs(s.x - p.x) > MIN_VALUE) (p.y - s.y) / (p.x - s.x) else MAX_VALUE
            val red = if (abs(s.x - e.x) > MIN_VALUE) (e.y - s.y) / (e.x - s.x) else MAX_VALUE
            blue >= red
        }
    }
    val epsilon = 0.00001f
}


class Figure(val name: String, val edges: Array<Edge>) {

    operator fun contains(p: PointF) = edges.count({ it(p) }) % 2 != 0
}



/**
 *
 */
data class PointF(val x: Float, val y: Float) {
    override fun toString() = "{$x, $y}"
}



/**LineF
 *
 * Create a Line characterised by two points
 *
 */
class LineF(val s: PointF, val e: PointF)

/**
 *
 */
fun findIntersection(l1: LineF, l2: LineF): PointF {
    val a1 = l1.e.y - l1.s.y
    val b1 = l1.s.x - l1.e.x
    val c1 = a1 * l1.s.x + b1 * l1.s.y

    val a2 = l2.e.y - l2.s.y
    val b2 = l2.s.x - l2.e.x
    val c2 = a2 * l2.s.x + b2 * l2.s.y

    val delta = a1 * b2 - a2 * b1
    // If lines are parallel, intersection point will contain infinite values
    return PointF((b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta)
}

fun main(args: Array<String>) {
    var l1 = LineF(PointF(4f, 0f), PointF(6f, 10f))
    var l2 = LineF(PointF(0f, 3f), PointF(10f, 7f))
    println(findIntersection(l1, l2))
    l1 = LineF(PointF(0f, 0f), PointF(1f, 1f))
    l2 = LineF(PointF(1f, 2f), PointF(4f, 5f))
    println(findIntersection(l1, l2))
}