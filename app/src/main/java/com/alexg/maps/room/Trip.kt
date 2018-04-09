package com.alexg.maps.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class Trip {

    @PrimaryKey
    var id: Int = 0

    var name: String = ""

    var pointsIds: String? = null


    constructor() {}

    constructor(nameOfTrip: String, pointsIdsArray: String) {
        pointsIds = pointsIdsArray
        name = nameOfTrip
    }
}
