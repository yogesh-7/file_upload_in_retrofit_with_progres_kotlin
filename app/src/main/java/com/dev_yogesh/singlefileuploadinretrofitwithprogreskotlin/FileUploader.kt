package com.dev_yogesh.singlefileuploadinretrofitwithprogreskotlin

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.dev_yogesh.singlefileuploadinretrofitwithprogreskotlin.interfaces.ApiClient
import com.dev_yogesh.singlefileuploadinretrofitwithprogreskotlin.interfaces.FileUploaderCallback
import com.dev_yogesh.singlefileuploadinretrofitwithprogreskotlin.interfaces.UploadInterface
import com.google.gson.JsonElement
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class FileUploader {

    companion object{
        var fileUploaderCallback: FileUploaderCallback? = null
    }


    fun uploadFiles(
        url: String,
        fileKey: String,
        file: File,
        fileUploaderCallback1: FileUploaderCallback
    ) {
        fileUploaderCallback = fileUploaderCallback1
        uploadSingleFile(file, url, fileKey)
    }

    private fun uploadSingleFile(file: File, uploadURL: String, fileKey: String) {
        val fileBody = PRRequestBody(file)
        val filePart = MultipartBody.Part.createFormData(fileKey, file.name, fileBody)

        val authToken = ""
        val uploadInterface = ApiClient.getClient()?.create(UploadInterface::class.java)

        val call: Call<JsonElement> = if (authToken.isEmpty()) {
            uploadInterface!!.uploadFile(uploadURL, filePart)
        } else {
            uploadInterface!!.uploadFile(uploadURL, filePart, authToken)
        }

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    fileUploaderCallback!!.onFinish(response.body().toString())
                } else {
                    fileUploaderCallback!!.onFinish("")
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                fileUploaderCallback!!.onError()
            }
        })
    }


    class PRRequestBody(private val mFile: File) : RequestBody() {
        override fun contentType(): MediaType? {
            // i want to upload only images
            return MediaType.parse("image/*")
        }

        @Throws(IOException::class)
        override fun contentLength(): Long {
            return mFile.length()
        }

        @Throws(IOException::class)
        override fun writeTo(sink: BufferedSink) {
            Log.d("FileUploader", "writeTo: ")
            val fileLength = mFile.length()
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            val inputStream = FileInputStream(mFile)
            var uploaded: Long = 0
            try {
                var read: Int
                val handler = Handler(Looper.getMainLooper())
                while (inputStream.read(buffer).also { read = it } != -1) {
                    // update progress on UI thread
                    handler.post(ProgressUpdater(uploaded, fileLength))
                    uploaded += read.toLong()
                    sink.write(buffer, 0, read)
                }
            }catch (e :Exception){
                inputStream.close()
            } finally {
                inputStream.close()
            }
        }

        companion object {
            private const val DEFAULT_BUFFER_SIZE = 2048
        }
    }


    private class ProgressUpdater(private val mUploaded: Long, private val mTotal: Long) :
        Runnable {
        override fun run() {
            val currentPercent = (100 * mUploaded / mTotal).toInt()
            Log.d("FileUploader", "currentPercent: $currentPercent")
            fileUploaderCallback?.onProgressUpdate(currentPercent)
        }
    }
}