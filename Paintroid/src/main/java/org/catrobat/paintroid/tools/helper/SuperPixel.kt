package org.catrobat.paintroid.tools.helper


class SuperPixel(arg_x: Int, arg_y: Int, lab : DoubleArray)  {
    var x: Int = arg_x
    var y: Int = arg_y
    var l: Double = lab.get(0)
    var a: Double = lab.get(1)
    var b: Double = lab.get(2)
}