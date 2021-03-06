package ch.wenksi.kidsdrawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var brushSize: Float = 0.toFloat()
    private var canvas: Canvas? = null
    private var canvasBitmap: Bitmap? = null
    private var canvasPaint: Paint? = null
    private var color = Color.parseColor("#3d3d3d")
    private var drawPaint: Paint? = null
    private var drawPath: CustomPath? = null
    private val paths = ArrayList<CustomPath>()
    private val undoPaths = ArrayList<CustomPath>()

    init {
        this.setUpDrawing()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        this.canvas = Canvas(this.canvasBitmap!!)
    }

    // change Canvas to Canvas? if fails
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(this.canvasBitmap!!, 0f, 0f, this.canvasPaint)

        for (path in this.paths) {
            this.drawPaint!!.strokeWidth = path.brushThickness
            this.drawPaint!!.color = path.color
            canvas.drawPath(path, this.drawPaint!!)
        }

        if (!this.drawPath!!.isEmpty) {
            this.drawPaint!!.strokeWidth = this.drawPath!!.brushThickness
            this.drawPaint!!.color = this.drawPath!!.color
            canvas.drawPath(this.drawPath!!, this.drawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                this.drawPath!!.color = this.color
                this.drawPath!!.brushThickness = this.brushSize
                this.drawPath!!.reset()
                this.drawPath!!.moveTo(touchX!!, touchY!!)
            }
            MotionEvent.ACTION_MOVE -> {
                this.drawPath!!.lineTo(touchX!!, touchY!!)
            }
            MotionEvent.ACTION_UP -> {
                this.paths.add(this.drawPath!!)
                this.drawPath = CustomPath(this.color, this.brushSize)
            }
            else -> return false
        }
        invalidate()
        return true
    }

    fun onClickUndo() {
        if (this.paths.size > 0) {
            this.undoPaths.add(this.paths.removeAt(this.paths.size - 1))
            invalidate()
        }
    }

    fun setColor(newColor: String) {
        this.color = Color.parseColor(newColor)
        this.drawPaint!!.color = this.color
    }

    fun setSizeForBrush(newSize: BrushSize) {
        this.brushSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            newSize.size,
            resources.displayMetrics
        )
        this.drawPaint!!.strokeWidth = this.brushSize
    }

    private fun setUpDrawing() {
        this.drawPaint = Paint()
        this.drawPaint!!.color = color
        this.drawPaint!!.style = Paint.Style.STROKE
        this.drawPaint!!.strokeJoin = Paint.Join.ROUND
        this.drawPaint!!.strokeCap = Paint.Cap.ROUND
        this.drawPath = CustomPath(this.color, this.brushSize)
        this.canvasPaint = Paint(Paint.DITHER_FLAG)
    }

    internal inner class CustomPath(var color: Int, var brushThickness: Float) : Path()
}


