package org.catrobat.paintroid.tools.helper

import android.R.attr
import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.ColorUtils.RGBToLAB
import kotlin.math.floor
import kotlin.math.sqrt


class PixelPixelAlgorithm(
    private val inputPix: Bitmap,
    private val color: Int,
    private val width: Int,
    private val height: Int,
) {

    val superPixelArray  =  Array(width) { x ->
        Array(height) { y ->
            // Just as an example, initialize with (x, y) coordinates and some default l, a, b values.
            SuperPixel(x, y, doubleArrayOf(-10.0, -10.0, -10.0))
        }
    }
    var inputImg =  inputPix

    var outputImg : Bitmap = inputImg.copy(inputImg.config, true)
    val iterations : Int =  130
    val N = inputImg.width * inputImg.height
    val K =  width * height
    val pixelsize : Double =  (N/K).toDouble()
    val superpixelInterval : Double  = sqrt(pixelsize)
    val superPixelheight =  inputImg.height / height
    val superPixelwidth =  inputImg.width / width


    fun getMean(input :Bitmap): Triple<Int, Int, Int> {
        var Red :Int = 0
        var Green : Int = 0
        var Blue : Int = 0
        for (x in 0 until input.width) {
           for(y in 0 until  input.height) {
               val c: Int = input.getPixel(x,y)
               Red += Color.red(c)
               Green+= Color.green(c)
               Blue += Color.blue(c)
           }
       }
        val pixelNum = input.width * input.height
       // return Triple((Red.toFloat() / pixelNum), (Green.toFloat() / pixelNum), (Blue.toFloat()/pixelNum))
        return Triple(Math.round(Red.toFloat() / pixelNum), Math.round(Green.toFloat() / pixelNum), Math.round(Blue.toFloat()/pixelNum))
     //   return Triple(floor(Red.toFloat() / pixelNum), floor(Green.toFloat() / pixelNum), floor(Blue.toFloat()/pixelNum))


    }
    fun getOuput(): Bitmap {

        return outputImg
    }

    fun innitSLIC(bitmap : Bitmap): Array<Array<SuperPixel>> {
        var xpos = (superPixelwidth/2)
        var ypos =  (superPixelheight / 2 )
        for (x  in 0  until width ) {
            for(y in 0 until height) {
                val c: Int = bitmap.getPixel(xpos,ypos)
                val Red = Color.red(c)
                val  Green = Color.green(c)
                val Blue :Int = Color.blue(c)
                val lab = doubleArrayOf(0.0,0.0,0.0)
                RGBToLAB(Red,Green,Blue, lab)
                superPixelArray[x][y] = SuperPixel(xpos, ypos, lab)
                ypos += superPixelheight
            }
            ypos = 0
            xpos += superPixelwidth
        }
        return superPixelArray

    }

    fun assignPixelsSLIC()
    {

    }
    fun SLIC(bitmap : Bitmap)
    {

    }

}