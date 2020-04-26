## 简介

Android 中要调用相机拍照并保存照片相当麻烦，这个库对这个过程做了封装。

只需几行代码便可实现。



## 导入

[![](https://jitpack.io/v/hahaha28/CameraHelper.svg)](https://jitpack.io/#hahaha28/CameraHelper)

在整个工程的`build.gradle`添加仓库：

```groovy
allprojects {
    repositories {
        ...
		maven { url 'https://jitpack.io' }
    }
}
```

在要使用的模块添加依赖

```groovy
dependencies {
    implementation 'com.github.hahaha28:CameraHelper:1.0.0'
}
```



## 使用

```kotlin
// 选择照片的保存位置
val file = File(externalCacheDir,"img.jpg")
// 拍照
CameraHelper.takePhoto(activity,file){ success ->
    if(success){
       // 成功
       // val bitmap = BitmapFactory.decodeStream(FileInputStream(file))
       // ...
    }else{
       // 失败
    }
}
```



