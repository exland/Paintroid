package org.catrobat.paintroid.tools.helper

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import kotlin.math.roundToInt
import kotlin.math.sqrt

class PixelPixelAlgorithm(
        private val boxHeight: Int,
        private val boxWidth: Int,
        x_pos: Int,
        y_pos: Int,
        private val width: Int,
        private val height: Int
) {

    var inputImg : Bitmap =   Bitmap.createBitmap(boxWidth, boxHeight, Bitmap.Config.ARGB_8888)

    var outputImg: Bitmap =   Bitmap.createBitmap(boxWidth, boxHeight, Bitmap.Config.ARGB_8888)

    var tool_pos_X: Int = x_pos
    var tool_pos_y: Int = y_pos

    val iterations: Int = 130

    @VisibleForTesting
    val N = inputImg.width * inputImg.height

    @VisibleForTesting
    val K = width * height

    @VisibleForTesting
    val pixelsize: Double = (N / K).toDouble()

    @VisibleForTesting
    val superpixelInterval: Double = sqrt(pixelsize)

    @VisibleForTesting
    val superPixelheight = inputImg.height / height
    val superPixelwidth = inputImg.width / width
    val m = 10;  // according to the paper

    fun getMean(input: Bitmap): Triple<Int, Int, Int> {
        var Red: Int = 0
        var Green: Int = 0
        var Blue: Int = 0
        for (x in 0 until input.width) {
            for (y in 0 until input.height) {
                val c: Int = input.getPixel(x, y)
                Red += Color.red(c)
                Green += Color.green(c)
                Blue += Color.blue(c)
            }
        }
        val pixelNum = input.width * input.height
        // return Triple((Red.toFloat() / pixelNum), (Green.toFloat() / pixelNum), (Blue.toFloat()/pixelNum))
        return Triple(Math.round(Red.toFloat() / pixelNum), Math.round(Green.toFloat() / pixelNum), Math.round(Blue.toFloat() / pixelNum))
        //   return Triple(floor(Red.toFloat() / pixelNum), floor(Green.toFloat() / pixelNum), floor(Blue.toFloat()/pixelNum))


    }

      fun getOuput(): Bitmap {
        return outputImg
    }
    fun calculatewSuperpixelcandidates(x: Int, y: Int): SupepixelCandidates {
        val window = (2 * superpixelInterval).roundToInt()
        var xmin = ((x - window).coerceAtLeast(0) / superPixelwidth.toDouble()).roundToInt()
        var xmax = ((x + window).coerceAtMost(inputImg.width.toDouble().toInt() - 1) / superPixelwidth.toDouble()).roundToInt()
        if (xmax >= width) {
            xmax = (width - 1).toInt()
            xmin = xmax - 1
        }
        // needs testing with low widht and height maybe artifical blocker
        var ymin = ((y - window).coerceAtLeast(0) / superPixelheight.toDouble()).roundToInt()
        var ymax = ((y + window).coerceAtMost(inputImg.height.toDouble().toInt() - 1) / superPixelheight.toDouble()).roundToInt()
        if (ymax >= height) {
            ymax = (height - 1).toInt()
            ymin = ymax - 1
        }
        return SupepixelCandidates(xmin, xmax, ymin, ymax)
    }


    /* fun distanceCalculation(xPixel: Int, yPixel : Int, lab : DoubleArray, superPixel :  SuperPixel ): Double {
        val lDist = (superPixel.l - lab[0]).pow(2)
        val aDist = (superPixel.a - lab[1]).pow(2)
        val bDist = (superPixel.b -  lab[2]).pow(2)
        val dLab= sqrt(lDist+aDist+bDist)
        val xDist =  (superPixel.x.toDouble() - xPixel.toDouble()).pow(2)
        val yDist =  (superPixel.y.toDouble() - yPixel.toDouble()).pow(2)
        val xyDist =  (m * sqrt(xDist+ yDist))/ superpixelInterval
        return dLab + xyDist
    }

*/
    @RequiresApi(Build.VERSION_CODES.N)
    fun KNearestNeighbour(inputImgArg: Bitmap): Bitmap {

        for (x in (tool_pos_X.toInt() -boxWidth.toInt()/2) ..(tool_pos_X.toInt()+boxWidth.toInt()/2) step superPixelwidth) { // add checkers for the boundary
            for (y in (tool_pos_y.toInt()-boxHeight.toInt()/2) ..(tool_pos_y.toInt() + boxHeight.toInt()/2) step superPixelheight) { // here i think the tool_pos_x is the midle of the box??
                val pixelCountMap = mutableMapOf<Int, Int>()
                // calculate the pixels areound the middle that is inputImt at x and y
                for (i in x until (x + (superPixelwidth))) {
                    for (j in (y ) until (y + (superPixelheight))) {
                        // store the pixel in the map and count how ofter the pixel exists
                        //

                        val pixel = inputImgArg.getPixel(i, j) // TODO THE PROBLEM is that is scales with the box and not global data
                        pixelCountMap[pixel] = pixelCountMap.getOrDefault(pixel, 0) + 1
                        // if more then half processed and one has enough points we can skip it
                    }
                }
                /// width = 1440
                // height = 2560

                // sets all the pixels to the most commmon in the map
                for (i in (0) until (boxWidth)) { // scale to a new img
                    for (j in (0) until (boxHeight)) {
                        outputImg.setPixel(i, j, Color.BLUE) // should be working
                    }
                }
               // outputImg.setPixel(x, y, pixelCountMap.maxByOrNull { it.value }!!.key) // should be working
               // pixelCountMap.clear()

            }
        }
        val t = 0;
        return outputImg
    }



}
