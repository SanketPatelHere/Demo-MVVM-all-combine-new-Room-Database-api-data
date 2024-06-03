package com.demomvvm.MVVM

import com.demomvvm.MVVM.GiftCard.GiftCardMovies
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MovieApi {
    @GET("popular?")
    fun getPopularMovies(@Query("api_key") api_key : String) : Call<Movies>

    @POST("ecard/get_ecard")
    fun getEgiftCardLiveURL(@Body params: HashMap<String,String>): Call<GiftCardMovies>
}