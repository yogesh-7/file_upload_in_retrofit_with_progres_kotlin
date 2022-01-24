package com.dev_yogesh.singlefileuploadinretrofitwithprogreskotlin.interfaces

import com.google.gson.JsonElement
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface UploadInterface {
    @Multipart
    @POST
    fun uploadFile(
        @Url url: String?,
        @Part file: MultipartBody.Part?,
        @Header("Authorization") authorization: String?
    ): Call<JsonElement>

    @Multipart
    @POST
    fun uploadFile(@Url url: String?, @Part file: MultipartBody.Part?): Call<JsonElement>
}