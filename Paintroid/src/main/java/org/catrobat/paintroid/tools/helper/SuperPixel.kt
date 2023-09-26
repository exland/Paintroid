package org.catrobat.paintroid.tools.helper

import kotlin.math.pow
import kotlin.math.sqrt


class SuperPixel(arg_x: Int, arg_y: Int, lab : DoubleArray)  {
    var x: Int = arg_x
    var y: Int = arg_y
    var l: Double = lab[0]
    var a: Double = lab[1]
    var b: Double = lab[2]
}

data class SupepixelCandidates(val xMin : Int, val xMax : Int, val yMin : Int, val yMax : Int)

class DistanceCalculation(argX: Int, argY :Int) {
    private val x: Int = argX
    private val y: Int = argY
    private val doubleToPairMap = LinkedHashMap<Double, Pair<Int, Int>>()
    var smallestKey: Double = Double.MAX_VALUE

    fun addSuperPixel(distance : Double, sup_pix : Pair<Int, Int> ){
        doubleToPairMap[distance] = sup_pix
    }
    fun getShortestDistance(): Double {

        for (entry in doubleToPairMap) {
            if (entry.key < smallestKey) {
                smallestKey = entry.key
            }
        }
        return smallestKey
    }

}
