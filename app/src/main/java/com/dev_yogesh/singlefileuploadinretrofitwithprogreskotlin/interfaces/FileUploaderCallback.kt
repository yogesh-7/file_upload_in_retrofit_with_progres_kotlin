package com.dev_yogesh.singlefileuploadinretrofitwithprogreskotlin.interfaces

interface FileUploaderCallback {
    fun onError()

    fun onFinish(responses: String?)

    fun onProgressUpdate(currentPercent: Int)
}