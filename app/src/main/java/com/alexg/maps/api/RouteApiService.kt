package com.alexg.maps.api

import com.alexg.maps.models.route.Track

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface RouteApiService {

    @GET("json")
    fun getRouteTrack(@Query("origin") from: String, @Query("destination") to: String,
                      @Query("waypoints") waypoints: String): Observable<Track>

}
