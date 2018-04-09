package com.alexg.maps

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import com.alexg.maps.room.ObjectOnMap
import com.alexg.maps.room.Trip
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import java.util.*

class MapsActivity : FragmentActivity(), OnMapReadyCallback, MapsActivityBus.SystemLink {

    private var mPresenter: MapsActivityBus.Presenter? = null
    private var mScreen: MapsActivityBus.Screen? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        mPresenter = MapsPresenter(this)

        mScreen = MapsScreen(this, mPresenter!!)
        mScreen!!.init(fragmentManager)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val goButton = findViewById<Button>(R.id.button_start)
        goButton.setOnClickListener { v -> mPresenter!!.onButtonGoClick(v) }
        val historyButton = findViewById<Button>(R.id.button_history)
        historyButton.setOnClickListener { mPresenter!!.getHistory() }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mPresenter!!.setGoogleMap(googleMap)
    }


    override fun converObjectToPlace(`object`: ObjectOnMap, placeType: Int) {
        runOnUiThread { mScreen!!.convertMapObjectToPlace(`object`, placeType, fragmentManager) }
    }

    override fun startAlertDialog(tripsList: List<Trip>) {
        runOnUiThread {
            if (tripsList != null && tripsList.size > 0)
                showAlertDialog(tripsList)
        }

    }

    fun showAlertDialog(tripsList: List<Trip>) {
        val tripsNameList = ArrayList<String>()
        for (trip in tripsList) {
            tripsNameList.add(trip.name)
        }
        val tripsArray = tripsNameList.toTypedArray()

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Trips")
        dialogBuilder.setItems(tripsArray) { dialog, item -> mPresenter!!.getTripPoints(tripsList[item]) }
        val alertDialogObject = dialogBuilder.create()
        alertDialogObject.show()
    }

    override fun showsSnackBar(view: View, message: String) {
        Snackbar.make(view, resources.getString(R.string.text_go_error), Snackbar.LENGTH_SHORT).show()

    }
}
