package com.alexg.maps.room

import android.arch.persistence.room.*

@Dao
interface TripDao {

    @get:Query("SELECT * FROM trip")
    val all: List<Trip>

    @Query("SELECT * FROM trip WHERE name = :name")
    fun getByName(name: String): Trip

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trip: Trip)

    @Update
    fun update(trip: Trip)

    @Delete
    fun delete(trip: Trip)


}
