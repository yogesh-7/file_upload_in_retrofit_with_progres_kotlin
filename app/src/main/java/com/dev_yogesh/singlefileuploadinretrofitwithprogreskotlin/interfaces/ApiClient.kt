package com.dev_yogesh.singlefileuploadinretrofitwithprogreskotlin.interfaces

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    /**
     * Created by Yogesh on 20/1/22.
     */


    companion object{
        private const val BASE_URL = "https://file.io"
        private var retrofit: Retrofit? = null
        fun getClient(): Retrofit? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
    }




}