package com.alexg.maps


import android.app.FragmentManager
import android.widget.EditText
import com.alexg.maps.room.ObjectOnMap
import com.alexg.maps.utils.Globals
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment

class MapsScreen internal constructor(private val systemLink: MapsActivityBus.SystemLink, private val presenter: MapsActivityBus.Presenter) : MapsActivityBus.Screen {

    override fun init(fragmentManager: FragmentManager) {

        val autocompleteFragmentStart = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment_start) as PlaceAutocompleteFragment
        val etStart = autocompleteFragmentStart.view!!.findViewById<EditText>(R.id.place_autocomplete_search_input)
        etStart.hint = AppDelegate.context!!.resources.getString(R.string.text_start_address)
        etStart.setHintTextColor(AppDelegate.context!!.resources.getColor(R.color.colorhintEdittext))
        presenter.setSearchListener(autocompleteFragmentStart, Globals.START_POINT)


        val autocompleteFragmentEnd = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment_end) as PlaceAutocompleteFragment
        val etEnd = autocompleteFragmentEnd.view!!.findViewById<EditText>(R.id.place_autocomplete_search_input)
        etEnd.hint = AppDelegate.context!!.resources.getString(R.string.text_end_address)
        etEnd.setHintTextColor(AppDelegate.context!!.resources.getColor(R.color.colorhintEdittext))
        presenter.setSearchListener(autocompleteFragmentEnd, Globals.END_POINT)

        val autocompleteFragmentMiddle = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment_middle) as PlaceAutocompleteFragment
        val etMiddle = autocompleteFragmentMiddle.view!!.findViewById<EditText>(R.id.place_autocomplete_search_input)
        etMiddle.hint = AppDelegate.context!!.resources.getString(R.string.text_transition_point)
        etMiddle.setHintTextColor(AppDelegate.context!!.resources.getColor(R.color.colorhintEdittext))
        presenter.setSearchListener(autocompleteFragmentMiddle, Globals.MIDDLE_POINT)


    }

    override fun convertMapObjectToPlace(`object`: ObjectOnMap, placeType: Int, fragmentManager: FragmentManager) {
        var autocompleteFragment: PlaceAutocompleteFragment? = null
        when (placeType) {
            Globals.START_POINT -> autocompleteFragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment_start) as PlaceAutocompleteFragment
            Globals.END_POINT -> autocompleteFragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment_end) as PlaceAutocompleteFragment
            Globals.MIDDLE_POINT -> autocompleteFragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment_middle) as PlaceAutocompleteFragment
        }
        val etMiddle = autocompleteFragment!!.view!!.findViewById<EditText>(R.id.place_autocomplete_search_input)
        etMiddle.setText(`object`.name)

        presenter.decodePlaceAddress(`object`.formattedAddress!!, `object`.formattedAddress!!, placeType)
    }
}
