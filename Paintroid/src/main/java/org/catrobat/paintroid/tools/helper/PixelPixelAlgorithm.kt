package org.catrobat.paintroid.tools.helper

import android.R.attr
import android.R.attr.bitmap
import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.VisibleForTesting
import androidx.core.graphics.ColorUtils.RGBToLAB
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt
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
    val pixelAssigments  =  Array(inputPix.width) { x ->
        Array(inputPix.height) { y ->
            DistanceCalculation(x, y)
        }
    }

    var inputImg =  inputPix

    var outputImg : Bitmap = inputImg.copy(inputImg.config, true)
    val iterations : Int =  130
    @VisibleForTesting
    val N = inputImg.width * inputImg.height
    @VisibleForTesting
    val K =  width * height
    @VisibleForTesting
    val pixelsize : Double =  (N/K).toDouble()
    @VisibleForTesting
    val superpixelInterval : Double  = sqrt(pixelsize)
    @VisibleForTesting
    val superPixelheight =  inputImg.height / height
    val superPixelwidth =  inputImg.width / width
    val m =  10 ;  // according to the paper

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

    fun calculatewSuperpixelcandidates(x :Int, y : Int): SupepixelCandidates {
        val window =  (2 * superpixelInterval).roundToInt()
        val xmin =  ((x - window).coerceAtLeast(0)/superPixelwidth.toDouble()).roundToInt()
        var xmax =  ((x+ window).coerceAtMost(inputImg.width.toDouble().toInt() - 1 )/superPixelwidth.toDouble()).roundToInt()
        if (xmax >= width){
            xmax =  width - 1
        }
        val ymin =  ((y - window).coerceAtLeast(0)/superPixelheight.toDouble()).roundToInt()
        var ymax = ((y + window).coerceAtMost(inputImg.height.toDouble().toInt() - 1 )/superPixelheight.toDouble()).roundToInt()
        if(ymax >= height){
            ymax = height - 1
        }
        return SupepixelCandidates(xmin, xmax, ymin, ymax)
    }


    fun distanceCalculation(xPixel: Int, yPixel : Int, lab : DoubleArray, superPixel :  SuperPixel ): Double {
        val lDist = (superPixel.l - lab[0]).pow(2)
        val aDist = (superPixel.a - lab[1]).pow(2)
        val bDist = (superPixel.b -  lab[2]).pow(2)
        val dLab= sqrt(lDist+aDist+bDist)
        val xDist =  (superPixel.x.toDouble() - xPixel.toDouble()).pow(2)
        val yDist =  (superPixel.y.toDouble() - yPixel.toDouble()).pow(2)
        val xyDist =  (m * sqrt(xDist+ yDist))/ superpixelInterval
        return dLab + xyDist
    }

    fun assignPixelsSLIC(input : Bitmap) {
        for (x in 0 until input.width) {
            for (y in 0 until input.height) {
                val candidates = calculatewSuperpixelcandidates(x, y)
                val c: Int = input.getPixel(x,y)
                val Red = Color.red(c)
                val Green = Color.green(c)
                val Blue :Int = Color.blue(c)
                val lab = doubleArrayOf(0.0,0.0,0.0)
                RGBToLAB(Red,Green,Blue, lab)
                for (XSup in candidates.xMin until candidates.xMax) {
                    for (YSup in candidates.yMin until candidates.yMax) {
                       val dist =  distanceCalculation(x,y, lab, superPixelArray[XSup][YSup])
                        pixelAssigments[x][y].addSuperPixel(dist, Pair(XSup,YSup))

                    }
                }

            }
        }
    }
    fun SLIC()
    {
        innitSLIC(inputImg)
        assignPixelsSLIC(inputImg)

    }

}
