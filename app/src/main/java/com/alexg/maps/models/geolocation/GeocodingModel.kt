package com.alexg.maps.models.geolocation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GeocodingModel {

    @SerializedName("results")
    @Expose
    var results: List<Result>? = null
    @SerializedName("status")
    @Expose
    var status: String? = null

}
