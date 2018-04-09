package com.alexg.maps.repository

import com.alexg.maps.models.geolocation.GeocodingModel
import com.alexg.maps.models.route.Track

import rx.Observable

interface GeolocationInterface {

    fun geolocationDecoding(address: String): Observable<GeocodingModel>

    fun routeDecoding(from: String, to: String, waypoints: String): Observable<Track>
}
