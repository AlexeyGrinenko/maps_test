package com.alexg.maps.api

import com.alexg.maps.AppDelegate
import com.alexg.maps.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object LocationApiFactory {
    private val BASE_URL = "https://maps.googleapis.com/maps/api/geocode/"

    private var sClient: OkHttpClient? = null

    @Volatile
    private var sService: LocationApiService? = null

    val locationApiService: LocationApiService
        get() {
            var service = sService
            if (service == null) {
                synchronized(LocationApiFactory::class.java) {
                    service = sService
                    if (service == null) {
                        sService = buildRetrofit().create(LocationApiService::class.java)
                        service = sService
                    }
                }
            }
            return service!!
        }

    private val client: OkHttpClient
        get() {
            var client = sClient
            if (client == null) {
                synchronized(LocationApiFactory::class.java) {
                    client = sClient
                    if (client == null) {
                        sClient = buildClient()
                        client = sClient
                    }
                }
            }
            return client!!
        }

    fun recreate() {
        sClient = null
        sClient = client
        sService = buildRetrofit().create(LocationApiService::class.java)
    }

    private fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }

    private fun buildClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor { chain ->
                    var request = chain.request()
                    val url = request.url().newBuilder().addQueryParameter(
                            "key",
                            AppDelegate.context!!.resources.getString(R.string.google_maps_key)
                    ).build()
                    request = request.newBuilder().url(url).build()
                    chain.proceed(request)
                }.build()

    }
}
