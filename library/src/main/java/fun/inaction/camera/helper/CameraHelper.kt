package `fun`.inaction.camera.helper


import androidx.fragment.app.FragmentActivity
import java.io.File

object CameraHelper {

    private const val TAG = "InvisibleFragment"

    /**
     * 拍照
     * @param activity
     * @param outFile 照片的保存位置
     * @param callback 这个函数有两个参数，第一个为boolean，表示是否成功，第二个是照片的Uri
     */
    fun takePhoto(activity:FragmentActivity, outFile:File, callback:TakePhotoCallback){
        val fragmentManager = activity.supportFragmentManager
        val existedFragment = fragmentManager.findFragmentByTag(TAG)
        val fragment = if(existedFragment != null){
            existedFragment as InvisibleFragment
        }else {
            val invisibleFragment = InvisibleFragment()
            fragmentManager.beginTransaction().add(invisibleFragment,TAG).commitNow()
            invisibleFragment
        }

        fragment.callback = callback
        fragment.takePhoto(outFile)

    }



}