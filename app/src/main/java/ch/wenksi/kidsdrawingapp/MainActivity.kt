package ch.wenksi.kidsdrawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setMargins
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.dialog_brush_size.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawing_view.setSizeForBrush(BrushSize.SMALL)
        ib_brush.setOnClickListener { this.showBrushSizeChooserDialog() }
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
