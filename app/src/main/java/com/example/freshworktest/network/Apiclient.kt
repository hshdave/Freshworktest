package com.example.freshworktest.network

import com.example.freshworktest.BuildConfig
import com.example.freshworktest.model.Gifsmodel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class Apiclient {

    private val getService : GetService
    private  val BASE_URL = "https://api.giphy.com/v1/gifs/"
    private  val TIMEOUT_LIMIT = 30_000L

    init {
        val clientBuilder = buildOkHttpClient()

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .client(clientBuilder)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        getService = retrofit.create(GetService::class.java)
    }

    private fun buildOkHttpClient() : OkHttpClient{

        val okHttpClient = OkHttpClient.Builder()

        okHttpClient.addInterceptor { chain ->

            val orRequest = chain.request()
            val requestBuilder = orRequest.newBuilder()
            val request = requestBuilder.build()
            chain.proceed(request)

        }

        if (BuildConfig.DEBUG)
        {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
            okHttpClient.connectTimeout(TIMEOUT_LIMIT,TimeUnit.MILLISECONDS)
            okHttpClient.readTimeout(TIMEOUT_LIMIT,TimeUnit.MILLISECONDS)
            okHttpClient.writeTimeout(TIMEOUT_LIMIT,TimeUnit.MILLISECONDS)
            okHttpClient.addInterceptor(loggingInterceptor)
        }

        return okHttpClient.build()
    }

    fun getSearchGIFS(searchKey : String): Observable<Response<Gifsmodel>>
    {
        return getService.getGifs(searchKey,BuildConfig.API_KEY)
    }

    fun getTrendingGIFS() : Observable<Response<Gifsmodel>>
    {
        return getService.getTrendingGifs(BuildConfig.API_KEY)
    }
}