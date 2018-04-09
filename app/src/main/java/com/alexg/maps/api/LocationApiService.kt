package com.alexg.maps.api


import com.alexg.maps.models.geolocation.GeocodingModel

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface LocationApiService {

    @GET("json")
    fun getAddressGeolocation(@Query("address") address: String): Observable<GeocodingModel>

}
