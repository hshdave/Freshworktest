package com.example.freshworktest.network

import com.example.freshworktest.model.Gifsmodel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface GetService {

    @GET("search")
    fun getGifs(@Query("q") q:String, @Query("api_key") key:String,@Query("offset") offset:Int): Observable<Response<Gifsmodel>>

    @GET("trending")
    fun getTrendingGifs(@Query("api_key") key:String,@Query("offset") offset:Int): Observable<Response<Gifsmodel>>

}