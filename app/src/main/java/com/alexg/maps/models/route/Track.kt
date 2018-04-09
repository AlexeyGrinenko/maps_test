package com.alexg.maps.models.route

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Track {

    @SerializedName("geocoded_waypoints")
    @Expose
    var geocodedWaypoints: List<GeocodedWaypoint>? = null
    @SerializedName("routes")
    @Expose
    var routes: List<Route>? = null
    @SerializedName("status")
    @Expose
    var status: String? = null

}
