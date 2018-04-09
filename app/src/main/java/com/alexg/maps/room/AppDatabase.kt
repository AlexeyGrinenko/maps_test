package com.alexg.maps.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(ObjectOnMap::class, Trip::class), version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun objectOnMapDao(): ObjectOnMapDao
    abstract fun tripDao(): TripDao
}
