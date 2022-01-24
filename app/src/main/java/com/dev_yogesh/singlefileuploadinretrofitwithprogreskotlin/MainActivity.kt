package com.dev_yogesh.singlefileuploadinretrofitwithprogreskotlin

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dev_yogesh.singlefileuploadinretrofitwithprogreskotlin.databinding.ActivityMainBinding
import com.dev_yogesh.singlefileuploadinretrofitwithprogreskotlin.interfaces.FileUploaderCallback
import com.dev_yogesh.singlefileuploadinretrofitwithprogreskotlin.util.ImageUtils.getBitmapFromUri
import com.dev_yogesh.singlefileuploadinretrofitwithprogreskotlin.util.ImageUtils.saveImageToInternalStorage
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var pDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pDialog = ProgressDialog(this)
        binding.btnSelectFiles.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                !== PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    2
                )
            } else {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(
                        intent,
                        getString(R.string.select_picture)
                    ), 1
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            data?.let {
                val imageUri = data.data
                if (imageUri != null) {
                    val thumbnail: Bitmap = getBitmapFromUri(this, imageUri)
                    val file = saveImageToInternalStorage(this, thumbnail)
                    uploadFile(file.absolutePath)
                }
            }

        }
    }


    private fun uploadFile(imagePath: String) {
        //showProgress()//uploadFil
        Log.d("MainActivity ", "uploadFile imagePath::  $imagePath")
        val filesToUpload = File(imagePath)
        FileUploader().uploadFiles("/", "file", filesToUpload, object : FileUploaderCallback {
            override fun onError() {
            }

            override fun onFinish(responses: String?) {

                Log.d("MainActivity ", "RESPONSE   $responses")
            }

            override fun onProgressUpdate(currentPercent: Int) {
                Log.d("MainActivity", "currentPercent: $currentPercent")
            }
        })
    }



}