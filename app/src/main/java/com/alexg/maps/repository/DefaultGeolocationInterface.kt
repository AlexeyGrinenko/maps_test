package com.alexg.maps.repository

import com.alexg.maps.api.LocationApiFactory
import com.alexg.maps.api.RouteApiFactory
import com.alexg.maps.models.geolocation.GeocodingModel
import com.alexg.maps.models.route.Track

import rx.Observable

class DefaultGeolocationInterface : GeolocationInterface {

    override fun geolocationDecoding(address: String): Observable<GeocodingModel> {
        return LocationApiFactory.locationApiService
                .getAddressGeolocation(address)
    }

    override fun routeDecoding(from: String, to: String, waypoints: String): Observable<Track> {
        return RouteApiFactory.routeApiService
                .getRouteTrack(from, to, waypoints)
    }
}
