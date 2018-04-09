package com.alexg.maps

import android.app.FragmentManager
import android.view.View

import com.alexg.maps.room.ObjectOnMap
import com.alexg.maps.room.Trip
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.maps.GoogleMap

interface MapsActivityBus {

    interface Screen {

        fun init(fragmentManager: FragmentManager)

        fun convertMapObjectToPlace(`object`: ObjectOnMap, placeType: Int, fragmentManager: FragmentManager)

    }

    interface Presenter {

        fun setGoogleMap(googleMap: GoogleMap)

        fun setSearchListener(fragment: PlaceAutocompleteFragment, flag: Int)

        fun getHistory()

        fun onButtonGoClick(view: View)

        fun decodePlaceAddress(address: String, name: String, placeFlag: Int)

        fun getTripPoints(trip: Trip)
    }

    interface SystemLink {
        fun converObjectToPlace(`object`: ObjectOnMap, placeType: Int)

        fun startAlertDialog(tripsList: List<Trip>)

        fun showsSnackBar(view: View, message: String)

    }


}


