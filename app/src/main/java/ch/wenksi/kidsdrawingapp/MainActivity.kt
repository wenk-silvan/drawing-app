package ch.wenksi.kidsdrawingapp

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.setMargins
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.dialog_brush_size.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var imageButtonCurrentPaint: ImageButton? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                try {
                    if (data!!.data != null) {
                        iv_background.visibility = View.VISIBLE
                        iv_background.setImageURI(data.data)
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Error in parsing the image or it's corrupted.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } catch(e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawing_view.setSizeForBrush(BrushSize.SMALL)
        this.imageButtonCurrentPaint = ll_paint_colors[8] as ImageButton
        this.imageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.pallet_selected)
        )
        ib_brush.setOnClickListener { this.showBrushSizeChooserDialog() }
        ib_gallery.setOnClickListener {
            if (this.isReadStorageAllowed()) {
                this.getBackgroundImageFromGallery()
            } else {
                this.requestStoragePermission()
            }
        }
        ib_undo.setOnClickListener { drawing_view.onClickUndo() }
    }

    private fun getBackgroundImageFromGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, GALLERY)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this@MainActivity,
                    "Permission granted to read the storage file",
                    Toast.LENGTH_SHORT
                ).show()
                this.getBackgroundImageFromGallery()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Oops you just denied the permission",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
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

    private fun isReadStorageAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isWriteStorageAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).toString()
            )
        ) {
            Toast.makeText(this, "Need permission to add a Background", Toast.LENGTH_SHORT).show()
        }
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), STORAGE_PERMISSION_CODE
        )
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

    companion object {
        private const val STORAGE_PERMISSION_CODE = 1
        private const val GALLERY = 2
    }
}
