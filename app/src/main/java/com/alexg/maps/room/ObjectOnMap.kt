package com.alexg.maps.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class ObjectOnMap {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var formattedAddress: String? = null

    var lat: Double? = null
    var lng: Double? = null

    var name: String? = null
    var tripId: Int = 0

    constructor() {}

    constructor(name: String, address: String, latitude: Double?, longitude: Double?, idOfftrip: Int) {
        formattedAddress = address
        lat = latitude
        lng = longitude
        this.name = name
        tripId = idOfftrip
    }
}
