package org.catrobat.paintroid.tools.helper

import android.graphics.Color


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
    private var doubleToPairMap  = Pair(-1, -1)
    var smallestDist: Double = Double.MAX_VALUE
    var collor :  Int = -1

    fun shortestDist(distance: Double, sup_pix: Pair<Int, Int>, rgb: Int){
        if (smallestDist > distance)
        {
            doubleToPairMap = sup_pix
            smallestDist =  distance
            collor = rgb
        }
    }

}
