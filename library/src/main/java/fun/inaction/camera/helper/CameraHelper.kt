package `fun`.inaction.camera.helper


import androidx.fragment.app.FragmentActivity
import java.io.File

class CameraHelper {



    companion object{

        private const val TAG = "InvisibleFragment"

        /**
         * 拍照
         * @param activity
         * @param outFile 照片的保存位置
         * @param callback 这个函数有一个参数为boolean，表示是否成功
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

        /**
         * 拍照
         * 为方便Java调用专门写的，如果是Kotlin，请用同名的另一个方法
         * @param activity
         * @param outFile 照片的保存位置
         * @param onReturnListener 这个函数有一个参数为boolean，表示是否成功
         */
        @JvmStatic
        fun takePhoto(activity: FragmentActivity,outFile: File,onReturnListener: OnReturnListener){
            takePhoto(activity,outFile){ b :Boolean ->
                onReturnListener.onReturn(b)
            }
        }

    }

    /**
     * 拍照结果的监听接口，为方便Java调用专门写的
     */
    interface OnReturnListener{
        fun onReturn(success:Boolean)
    }



}