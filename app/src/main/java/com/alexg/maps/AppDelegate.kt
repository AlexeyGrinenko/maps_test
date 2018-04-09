package com.alexg.maps

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context

import com.alexg.maps.room.AppDatabase

class AppDelegate : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        database = Room.databaseBuilder(context!!, AppDatabase::class.java, "database")
                .build()
    }

    companion object {

        var context: Context? = null
            private set
        var database: AppDatabase? = null
            private set
    }

//    fun getDatabase(): AppDatabase = database!!

}

