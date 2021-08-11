package ch.wenksi.kidsdrawingapp

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.setMargins
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.dialog_brush_size.*

class MainActivity : AppCompatActivity() {

    private var imageButtonCurrentPaint: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawing_view.setSizeForBrush(BrushSize.SMALL)
        this.imageButtonCurrentPaint = ll_paint_colors[8] as ImageButton
        this.imageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.pallet_selected)
        )
        ib_brush.setOnClickListener { this.showBrushSizeChooserDialog() }
    }

    fun paintClicked(view: View) {
        if (view !== this.imageButtonCurrentPaint) {
            val imageButton = view as ImageButton
            val colorHex = imageButton.tag.toString()
            drawing_view.setColor(colorHex)
            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallet_selected)
            )
            this.imageButtonCurrentPaint!!.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallet_normal)
            )
            this.imageButtonCurrentPaint = view
        }
    }

    private fun showBrushSizeChooserDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush size: ")
        // val param = iv_selected_brush.layoutParams as ViewGroup.MarginLayoutParams

        brushDialog.ib_small_brush.setOnClickListener {
            drawing_view.setSizeForBrush(BrushSize.SMALL)
            iv_selected_brush.setImageResource(R.drawable.small)
            // param.setMargins(25, 25, 25, 25)
            // iv_selected_brush.layoutParams = param
            brushDialog.dismiss()
        }
        brushDialog.ib_medium_brush.setOnClickListener {
            drawing_view.setSizeForBrush(BrushSize.MEDIUM)
            iv_selected_brush.setImageResource(R.drawable.medium)
            // param.setMargins(20, 20, 20, 20)
            // iv_selected_brush.layoutParams = param
            brushDialog.dismiss()
        }
        brushDialog.ib_large_brush.setOnClickListener {
            drawing_view.setSizeForBrush(BrushSize.LARGE)
            iv_selected_brush.setImageResource(R.drawable.large)
            // param.setMargins(15, 15, 15, 15)
            // iv_selected_brush.layoutParams = param
            brushDialog.dismiss()
        }

        brushDialog.show()
    }
}
