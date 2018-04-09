package com.alexg.maps

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.alexg.maps.models.geolocation.GeocodingModel
import com.alexg.maps.models.route.Track
import com.alexg.maps.repository.GeolocationProvider
import com.alexg.maps.room.ObjectOnMap
import com.alexg.maps.room.Trip
import com.alexg.maps.utils.DoubleArrayEvaluator
import com.alexg.maps.utils.Globals
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

class MapsPresenter : MapsActivityBus.Presenter {

    private val TAG = "BaseMapsActivity"


    private var mMap: GoogleMap? = null
    private var objectsOnMapList: ArrayList<ObjectOnMap>? = ArrayList()
    private var startObject: ObjectOnMap? = null
    private var endObject: ObjectOnMap? = null
    private var isFromHistory = false

    private var systemLink: MapsActivityBus.SystemLink? = null


    internal constructor(systemLink: MapsActivityBus.SystemLink) {
        this.systemLink = systemLink
    }

    internal constructor(systemLink: MapsActivityBus.SystemLink, screen: MapsActivityBus.Screen) {
        this.systemLink = systemLink

    }


    override fun setGoogleMap(googleMap: GoogleMap) {
        mMap = googleMap

        val dnipro = LatLng(48.464717, 35.046183)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(dnipro))
    }

    override fun setSearchListener(fragment: PlaceAutocompleteFragment, flag: Int) {
        fragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                decodePlaceAddress(place.address.toString(), place.name.toString(), flag)
            }

            override fun onError(status: Status) {
                Log.i(TAG, "An error occurred: " + status)
            }
        })
    }

    override fun getHistory() {
        AsyncTask.execute {
            val db = AppDelegate.database
            val tripDao = db!!.tripDao()
            val tripsList = tripDao.all

            systemLink!!.startAlertDialog(tripsList)
        }
    }

    override fun onButtonGoClick(view: View) {
        if (startObject != null && endObject != null) {
            decodeWay()
        } else {
            systemLink!!.showsSnackBar(view, AppDelegate.context!!.resources.getString(R.string.text_go_error))
        }
    }

    private fun decodeWay() {

        val waypoints = StringBuilder()
        if (objectsOnMapList != null && objectsOnMapList!!.size > 0) {
            for (objectOnMap in objectsOnMapList!!)
                waypoints.append("|").append(objectOnMap.formattedAddress)
        }
        GeolocationProvider.provideGithubRepository()
                .routeDecoding(startObject!!.formattedAddress!!, endObject!!.formattedAddress!!, waypoints.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Track> {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        Log.i(TAG, "Address error occurred: ")

                    }

                    override fun onNext(track: Track) {
                        val routes = track.routes
                        val way = routes!![0].overviewPolyline!!.points
                        startMoving(way)
                    }
                })
    }

    override fun decodePlaceAddress(address: String, name: String, placeFlag: Int) {
        GeolocationProvider.provideGithubRepository()
                .geolocationDecoding(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<GeocodingModel> {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        Log.i(TAG, "Address error occurred: ")
                    }

                    override fun onNext(geocodingModel: GeocodingModel) {
                        val decodingresultList = geocodingModel.results
                        val result = decodingresultList!![0]

                        val cameraPosition = CameraPosition.Builder()
                                .target(LatLng(result.geometry!!.location!!.lat!!,
                                        result.geometry!!.location!!.lng!!))
                                .zoom(7f)
                                .build()
                        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                        mMap!!.addMarker(MarkerOptions()
                                .position(LatLng(result.geometry!!.location!!.lat!!,
                                        result.geometry!!.location!!.lng!!))
                                .title(name))


                        val objectOnMap = ObjectOnMap(name, address,
                                result.geometry!!.location!!.lat,
                                result.geometry!!.location!!.lng, 0)
                        when (placeFlag) {
                            Globals.START_POINT -> startObject = objectOnMap
                            Globals.END_POINT -> endObject = objectOnMap
                            Globals.MIDDLE_POINT -> objectsOnMapList!!.add(objectOnMap)
                        }
                    }
                })

    }


    private fun startMoving(way: String?) {
        val line = PolylineOptions()
        line.width(10f)
        val latLngBuilder = LatLngBounds.Builder()
        val mPoints = PolyUtil.decode(way!!)
        for (i in mPoints.indices) {
            if (i == 0) {
                val startMarkerOptions = MarkerOptions()
                        .position(mPoints[i])
                mMap!!.addMarker(startMarkerOptions)
            } else if (i == mPoints.size - 1) {
                val endMarkerOptions = MarkerOptions()
                        .position(mPoints[i])
                mMap!!.addMarker(endMarkerOptions)
            }
            line.add(mPoints[i])
            latLngBuilder.include(mPoints[i])
        }
        mMap!!.addPolyline(line)
        val size = AppDelegate.context!!.resources.displayMetrics.widthPixels
        val latLngBounds = latLngBuilder.build()
        val track = CameraUpdateFactory.newLatLngBounds(latLngBounds, size, size, 2)
        mMap!!.moveCamera(track)

        doMarkerAnimation(mMap!!.addMarker(MarkerOptions().position(mPoints[0])
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))),
                0, Globals.ANIMATION_STEP, mPoints)
        if (!isFromHistory) saveTrip()
    }

    private fun doMarkerAnimation(marker: Marker, indexStart: Int, indexEnd: Int, mPoints: List<LatLng>) {
        val startValues = doubleArrayOf(mPoints[indexStart].latitude, mPoints[indexStart].longitude)
        val endValues = doubleArrayOf(mPoints[indexEnd].latitude, mPoints[indexEnd].longitude)
        val latLngAnimator = ValueAnimator.ofObject(DoubleArrayEvaluator(), startValues, endValues)
        latLngAnimator.duration = 100
        latLngAnimator.interpolator = DecelerateInterpolator()
        latLngAnimator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as DoubleArray
            marker.position = LatLng(animatedValue[0], animatedValue[1])
        }
        latLngAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                val newIndex = if (indexEnd + Globals.ANIMATION_STEP >= mPoints.size) mPoints.size - 1 else indexEnd + Globals.ANIMATION_STEP
                doMarkerAnimation(marker, indexEnd, newIndex, mPoints)
            }
        })
        latLngAnimator.start()
    }


    private fun saveTrip() {
        AsyncTask.execute {
            val db = AppDelegate.database
            val objectOnMapDao = db!!.objectOnMapDao()
            val startId = objectOnMapDao.insert(startObject!!)
            val endId = objectOnMapDao.insert(endObject!!)
            val pointsIdsArray = StringBuilder(startId.toString() + ";")
            if (objectsOnMapList != null && objectsOnMapList!!.size > 0) {
                for (objectOnMap in objectsOnMapList!!) {
                    val pointsID = objectOnMapDao.insert(objectOnMap)
                    pointsIdsArray.append(pointsID.toString()).append(";")
                }
            }
            pointsIdsArray.append(endId.toString())
            val tripDao = db.tripDao()
            tripDao.insert(Trip(startObject!!.name + " - " + endObject!!.name, pointsIdsArray.toString()))
        }
    }

    override fun getTripPoints(trip: Trip) {
        isFromHistory = true
        val ids = trip.pointsIds!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        getPlaceById(ids[0], Globals.START_POINT)
        getPlaceById(ids[ids.size - 1], Globals.END_POINT)

        for (i in 1 until ids.size - 1) {
            getPlaceById(ids[i], Globals.MIDDLE_POINT)
        }
    }

    private fun getPlaceById(id: String, placeType: Int) {
        AsyncTask.execute {
            val db = AppDelegate.database
            val objectOnMapDao = db!!.objectOnMapDao()
            val `object` = objectOnMapDao.getById(Integer.parseInt(id))
            systemLink!!.converObjectToPlace(`object`, placeType)
        }
    }
}