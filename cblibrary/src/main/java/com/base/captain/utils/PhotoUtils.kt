package com.base.captain.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.soundcloud.android.crop.Crop
import java.io.File

object PhotoUtils {
    private lateinit var file: File
    private lateinit var uri: Uri
    lateinit var outFile: File
    private lateinit var outUri: Uri
    private const val CAMERA_REQUESTCODE = 1001
    private lateinit var dic: File

    fun takeGalley(aty: Activity) {
        initFiles(aty)
        Intent().also {
            it.type = "image/*"
            it.action = Intent.ACTION_GET_CONTENT
            aty.startActivityForResult(it, CAMERA_REQUESTCODE)
        }
    }

    fun takeGalley(fragment: Fragment) {
        initFiles(fragment.context!!)
        Intent().also {
            it.type = "image/*"
            it.action = Intent.ACTION_GET_CONTENT
            fragment.startActivityForResult(it, CAMERA_REQUESTCODE)
        }
    }

    private fun initFiles(context: Context) {
        dic = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/ico")
        file = File(dic, "photo" + System.currentTimeMillis() + ".jpg")
        outFile = File(dic, "res" + System.currentTimeMillis() + ".jpg")
        uri =
            getURI(context, file)
        outUri =
            getURI(context, outFile)
    }

    fun takePhoto(aty: Activity) {
        initFiles(aty)
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            it.resolveActivity(aty.packageManager)?.also { s ->
                file.createNewFile()
                file.also { _ ->
                    it.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    aty.startActivityForResult(it, CAMERA_REQUESTCODE)
                }
            }
        }
    }

    fun takePhoto(fragment: Fragment) {
        initFiles(fragment.context!!)
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            if (null != fragment.context) {
                it.resolveActivity(fragment.context!!.packageManager)?.also { s ->
                    file.createNewFile()
                    file.also { _ ->
                        it.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                        fragment.startActivityForResult(it, CAMERA_REQUESTCODE)
                    }
                }
            }
        }
    }

    fun activityResult(aty: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUESTCODE) {
            Crop.of(data?.data ?: uri, outUri).asSquare().start(aty)
        }
    }

    fun fragmentResult(fragment: Fragment, requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUESTCODE) {
            Crop.of(data?.data ?: uri, outUri).asSquare().start(fragment.context!!, fragment)
        }
    }

    fun clean() {
        try {
            val fileTreeWalk = dic.walk()
            fileTreeWalk.iterator().forEach { it.delete() }
        } catch (noException: Exception) {
        }
    }

    private fun getURI(context: Context, file: File): Uri {
        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            Uri.fromFile(file)
        } else {
            FileProvider.getUriForFile(context, context.packageName + ".provider", file)
        }
    }
}