package com.alexg.maps.repository

import android.support.annotation.MainThread

object GeolocationProvider {

    private var sGeolocationInterface: GeolocationInterface? = null

    fun provideGithubRepository(): GeolocationInterface {
        if (sGeolocationInterface == null) {
            sGeolocationInterface = DefaultGeolocationInterface()
        }
        return sGeolocationInterface as GeolocationInterface
    }

    fun setGeolocationInterface(geolocationInterface: GeolocationInterface) {
        sGeolocationInterface = geolocationInterface
    }

    @MainThread
    fun init() {
        sGeolocationInterface = DefaultGeolocationInterface()
    }
}
