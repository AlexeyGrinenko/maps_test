package com.alexg.maps.room

import android.arch.persistence.room.*

@Dao
interface ObjectOnMapDao {

    @get:Query("SELECT * FROM objectonmap")
    val all: List<ObjectOnMap>

    @Query("SELECT * FROM objectonmap WHERE formattedAddress = :address")
    fun getByAddress(address: String): ObjectOnMap

    @Query("SELECT * FROM objectonmap WHERE id = :id")
    fun getById(id: Int): ObjectOnMap

    @Insert
    fun insert(objectOnMap: ObjectOnMap): Long

    @Update
    fun update(objectOnMap: ObjectOnMap): Int

    @Delete
    fun delete(objectOnMap: ObjectOnMap)
}
