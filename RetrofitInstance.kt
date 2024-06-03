package com.demomvvm.MVVM

import android.util.Base64
import com.demomvvm.Retrofit.ServiceBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets

object RetrofitInstance {

    class BasicAuthInterceptor(): Interceptor {
        var credentials = "wemakeadifference" + ":" + "nHNfTwfu!)Sq7m06J73BNYHq"
        var base64EncodedCredentials = Base64.encodeToString(credentials.toByteArray(
            StandardCharsets.UTF_8), Base64.NO_WRAP)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            var request = chain.request()
            request = request.newBuilder().header("Authorization", "Basic "+base64EncodedCredentials).build()

            return chain.proceed(request)
        }
    }

    private val client = OkHttpClient.Builder().addInterceptor(BasicAuthInterceptor()).build()

    val api : MovieApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://app.wemad.com.au/sub/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MovieApi::class.java)
    }




}
