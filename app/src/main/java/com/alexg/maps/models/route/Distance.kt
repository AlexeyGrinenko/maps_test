package com.alexg.maps.models.route

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Distance {

    @SerializedName("text")
    @Expose
    var text: String? = null
    @SerializedName("value")
    @Expose
    var value: Int? = null

}
