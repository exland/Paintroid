package org.catrobat.paintroid.tools.implementation

import android.graphics.Bitmap
import android.graphics.PointF
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import androidx.test.espresso.idling.CountingIdlingResource
import org.catrobat.paintroid.command.CommandManager
import org.catrobat.paintroid.tools.ContextCallback
import org.catrobat.paintroid.tools.ToolPaint
import org.catrobat.paintroid.tools.ToolType
import org.catrobat.paintroid.tools.Workspace
import org.catrobat.paintroid.tools.helper.PixelPixelAlgorithm
import org.catrobat.paintroid.tools.options.PixelationToolOptionsView
import org.catrobat.paintroid.tools.options.ToolOptionsViewController

class PixelTool(
    pixelToolOptionsViewParam: PixelationToolOptionsView,
    contextCallback: ContextCallback,
    toolOptionsViewController: ToolOptionsViewController,
    toolPaint: ToolPaint,
    workspace: Workspace,
    idlingResource: CountingIdlingResource,
    commandManager: CommandManager,
    override var drawTime: Long
) : BaseToolWithRectangleShape(contextCallback, toolOptionsViewController, toolPaint, workspace, idlingResource, commandManager) {
    private val pixelToolOptionsView: PixelationToolOptionsView
    @VisibleForTesting
    @JvmField
    var numPixelHeight = 40

    @VisibleForTesting
    @JvmField
    var numPixelWidth = 60

    @VisibleForTesting
    @JvmField
    var numCollors = 0f

    lateinit var algorithm : PixelPixelAlgorithm
    lateinit var localWorkspace:  Workspace

    init {
        boxHeight = workspace.height.toFloat()
        boxWidth = workspace.width.toFloat()
        toolPosition.x = boxWidth / 2f
        toolPosition.y = boxHeight / 2f
        this.pixelToolOptionsView = pixelToolOptionsViewParam
        localWorkspace = workspace
        setBitmap(Bitmap.createBitmap(boxWidth.toInt(), boxHeight.toInt(), Bitmap.Config.ARGB_8888))
        toolOptionsViewController.showDelayed()
        this.pixelToolOptionsView.setPixelPreviewListener(object : PixelationToolOptionsView.OnPixelationPreviewListener {
            override fun setPixelWidth(widthPixels: Float) {
                    this@PixelTool.numPixelWidth = widthPixels.toInt()
            }

            override fun setPixelHeight(heightPixels: Float) {
               this@PixelTool.numPixelHeight = heightPixels.toInt()
            }

            override fun setNumCollor(collorNum: Float) {
                this@PixelTool.numCollors = collorNum
            }
        })
    }

    override val toolType: ToolType
        get() = ToolType.PIXEL

    override fun handleUpAnimations(coordinate: PointF?) {
        super.handleUp(coordinate)
    }

    override fun handleDownAnimations(coordinate: PointF?) {
        super.handleDown(coordinate)
    }

    override fun toolPositionCoordinates(coordinate: PointF): PointF = coordinate

    // is the checkmark to run the programm
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClickOnButton() {
        workspace.width
        workspace.height
        algorithm = PixelPixelAlgorithm(boxHeight.toInt(),boxWidth.toInt(), toolPosition.x.toInt(),toolPosition.y.toInt(), numPixelWidth,numPixelHeight) // bug that we have to pass the boxwidth box heigt, touch down poition X and touchDownPosiutionY
        workspace.bitmapOfCurrentLayer?.let { algorithm.KNearestNeighbour(it) }
     //   drawingBitmap = algorithm.getOuput().copy(algorithm.outputImg.config, true)
     //   localWorkspace.bitmapOfCurrentLayer = drawingBitmap
    }

    override fun resetInternalState() = Unit
}
