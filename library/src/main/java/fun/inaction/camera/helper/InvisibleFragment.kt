package `fun`.inaction.camera.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

/**
 * 拍完照片的回调
 * 一个参数，Boolean 表示是否成功
 */
typealias TakePhotoCallback = (Boolean)->Unit

class InvisibleFragment : Fragment() {

    /**
     * 照片文件
     */
    private lateinit var outputFile: File

    /**
     * 结束的回调
     */
    var callback: TakePhotoCallback? = null

    fun takePhoto(outputFile: File) {
        this.outputFile = outputFile
        // 创建文件
        if (outputFile.exists()) {
            outputFile.delete()
        }
        outputFile.createNewFile()
        // 获取Uri
        val fileUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context!!,
                "fun.inaction.camera.helper.fileprovider",
                outputFile
            )
        } else {
            Uri.fromFile(outputFile)
        }
        // 启动相机App
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                // 成功
                callback?.let {
                    // 如果有需要，旋转照片并重新保存
                    val bitmap = rotateIfRequired()
                    saveImage(bitmap,outputFile)
                    it(true)
                }
            } else {
                // 失败
                callback?.let { it(false) }
            }
        }
    }

    /**
     * 如果有需要，就旋转照片
     */
    private fun rotateIfRequired(): Bitmap {
        val bitmap = BitmapFactory.decodeStream(FileInputStream(outputFile))
        val exif = ExifInterface(outputFile.path)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL)
        return when(orientation){
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap,90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap,180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap,270)
            else -> bitmap
        }
    }

    /**
     * 将Bitmap 保存为图片
     */
    private fun saveImage(bitmap: Bitmap, file:File){
        if(file.exists()){
            file.delete()
        }
        file.createNewFile()
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos)
        fos.close()
    }

    /**
     * 旋转 Bitmap
     */
    private fun rotateBitmap(bitmap: Bitmap, degree:Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,
            matrix,true)
        bitmap.recycle()
        return rotatedBitmap
    }

}