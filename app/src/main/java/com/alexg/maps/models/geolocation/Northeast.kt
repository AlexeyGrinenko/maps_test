package com.alexg.maps.models.geolocation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Northeast {

    @SerializedName("lat")
    @Expose
    var lat: Double? = null
    @SerializedName("lng")
    @Expose
    var lng: Double? = null

}
