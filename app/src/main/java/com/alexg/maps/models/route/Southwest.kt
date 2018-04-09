package com.alexg.maps.models.route

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Southwest {

    @SerializedName("lat")
    @Expose
    var lat: Float? = null
    @SerializedName("lng")
    @Expose
    var lng: Float? = null

}
