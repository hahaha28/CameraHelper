package `fun`.inaction.app

import `fun`.inaction.camera.helper.CameraHelper
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import java.io.File
import java.io.FileInputStream

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.button)
        imageView = findViewById(R.id.imageView)

        button.setOnClickListener {
            var file = File(externalCacheDir,"test.jpg")

            CameraHelper.takePhoto(this,file){ b: Boolean ->
                if(b){
                    val inputStream = FileInputStream(file)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    imageView.setImageBitmap(bitmap)
                }
            }
        }
    }
}
