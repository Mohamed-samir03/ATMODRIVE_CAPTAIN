package com.mosamir.atmodrivecaptain.features.trip.presentation.common

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mosamir.atmodrivecaptain.R
import com.mosamir.atmodrivecaptain.databinding.ActivityMapsBinding
import com.mosamir.atmodrivecaptain.features.trip.domain.model.UpdateAvailabilityResponse
import com.mosamir.atmodrivecaptain.util.AnimationUtils
import com.mosamir.atmodrivecaptain.util.Constants
import com.mosamir.atmodrivecaptain.util.IResult
import com.mosamir.atmodrivecaptain.util.LocationHelper
import com.mosamir.atmodrivecaptain.util.MapUtils
import com.mosamir.atmodrivecaptain.util.NetworkState
import com.mosamir.atmodrivecaptain.util.SharedPreferencesManager
import com.mosamir.atmodrivecaptain.util.getData
import com.mosamir.atmodrivecaptain.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding

    private lateinit var mMap: GoogleMap
    var mLocationRequest: LocationRequest?= null
    var mLocationCallback: LocationCallback?= null
    var mFusedLocationClient: FusedLocationProviderClient?= null
    private var bottomSheet = BottomSheetBehavior<ConstraintLayout>()

    private val tripViewModel by viewModels<TripViewModel>()

    val mapLocation = HashMap<String,Any>()
    var isOnline = false
    private var captainId = ""

    private var mBackPressed: Long = 0
    private var movingCabMarker : Marker?= null
    private var previousLatLng: LatLng? = null
    private var currentLatLng: LatLng? = null
    private var valueAnimator: ValueAnimator? = null
    private var valueEventListener : ValueEventListener?= null

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.reference
        captainId = SharedPreferencesManager(this).getString(Constants.CAPTAIN_ID_PREFS)
        updateStatusCaptainLayout()

//        val captain = OnlineCaptain(captainId,"30.25","30.25",0)
//        database.child("Online_captains").child(captainCode).setValue(captain)

        val model = ViewModelProvider(this).get(SharedViewModel::class.java)

        model.requestStatus.observe(this, Observer {

            if (it){
                // trip accepted

            }else{
                bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
            }

        })

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initLocation()

        binding.checkBoxCaptainStatus.setOnCheckedChangeListener { buttonView, isChecked ->
            tripViewModel.updateAvailability(mapLocation["lat"].toString(),mapLocation["lng"].toString())
            SharedPreferencesManager(this).saveBoolean(Constants.CAPTAIN_STATUS,isChecked)
            updateStatusCaptainLayout()
            disPlayBottomSheet()
        }

        observeOnTrip()

    }

    private fun observeOnTrip(){
        lifecycleScope.launch {
            tripViewModel.updateAvaResult.collect{ networkState ->
                when(networkState?.status){
                    NetworkState.Status.SUCCESS ->{
                        val data = networkState.data as IResult<UpdateAvailabilityResponse>
                        showToast(data.getData()?.available.toString())
                    }
                    NetworkState.Status.FAILED ->{
                        showToast(networkState.msg.toString())
                    }
                    NetworkState.Status.RUNNING ->{
                    }
                    else -> {}
                }
            }
        }
    }

    private fun disPlayBottomSheet(){
        val bottomSheetView = findViewById<ConstraintLayout>(R.id.bottom_sheet_trip)
        bottomSheet = BottomSheetBehavior.from(bottomSheetView!!)
        bottomSheet.isDraggable = true
        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
//        bottomSheetHeight()
    }

    private fun bottomSheetHeight(){
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        val desiredHeight = (screenHeight * 0.1).toInt()
        bottomSheet.peekHeight = desiredHeight
    }

    private fun updateStatusCaptainLayout(){

        val captainStatus = SharedPreferencesManager(this).getBoolean(Constants.CAPTAIN_STATUS)

        if (!captainStatus){
            binding.tvCaptainStatus.apply {
                isOnline = false
                text = "You are offline"
                setTextColor(ContextCompat.getColor(this@MapsActivity, R.color.white))
            }
            binding.layoutCaptainStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.error))
            binding.checkBoxCaptainStatus.isChecked = false
        }else{
            binding.tvCaptainStatus.apply {
                isOnline = true
                text = "You are online"
                setTextColor(ContextCompat.getColor(this@MapsActivity, R.color.title))
            }
            binding.layoutCaptainStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.background))
            binding.checkBoxCaptainStatus.isChecked = true
        }

    }


    private fun setMapDarkStyle(map: GoogleMap) {
        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_dark)
            )
            if (!success) {
                Log.e("TAG", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("TAG", "Can't find style. Error: ", e)
        }
    }

    private fun initLocation(){
        if (mFusedLocationClient == null){
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        }

        if (mLocationRequest == null){
            mLocationRequest = LocationRequest.create()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setInterval(2000)
                .setFastestInterval(2000)
                .setSmallestDisplacement(5f)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){


        mLocationCallback = object : LocationCallback(){
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)

                val latLng = result.lastLocation!!
                mapLocation["lat"] = latLng.latitude.toString()
                mapLocation["lng"] = latLng.longitude.toString()
                updateCarLocation(LatLng(latLng.latitude,latLng.longitude))

                if (isOnline){
                    database.child("OnlineCaptains").child(captainId).updateChildren(mapLocation)
                }

            }
        }

        mFusedLocationClient?.requestLocationUpdates(mLocationRequest!!,mLocationCallback!!, Looper.getMainLooper())

    }

    private fun getAddressFromLatLng(latLng: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses?.isNotEmpty()!!) {
                val address = addresses[0]
                // You can format the address as per your requirements
                return address.getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            showToast("Error getting address")
        }
        return "Address not found"
    }

    private fun moveCameraMap(latLng: LatLng){
        val cameraPosition = CameraPosition.builder().target(latLng).zoom(15f).build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun locationChecker(){

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest!!)

        val result : Task<LocationSettingsResponse> = LocationServices.getSettingsClient(this)
            .checkLocationSettings(builder.build())

        result.addOnCompleteListener {task ->

            try {
                task.getResult(ApiException::class.java)
                getLocation()
            }catch (exception : ApiException){
                when (exception.statusCode){

                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->{
                        try {
                            val resolve = exception as ResolvableApiException
                            startIntentSenderForResult(resolve.resolution.intentSender,
                                Priority.PRIORITY_HIGH_ACCURACY,null,0,0,0,null)
                        }catch (ex:Exception){}
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->{
                        showToast("GPS Not available")
                    }

                }
            }

        }

    }

    private fun addCarMarkerAndGet(latLng: LatLng): Marker {
        val bitmapDescriptor =
            BitmapDescriptorFactory.fromBitmap(MapUtils.getCarBitmap(this))
        return mMap.addMarker(
            MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor)
        )!!
    }

    private fun updateCarLocation(latLng: LatLng) {
        val a = System.currentTimeMillis() - mBackPressed
        if (a < 2600) {
            return
        }
        mBackPressed = System.currentTimeMillis()
        if (movingCabMarker == null) {
            movingCabMarker = addCarMarkerAndGet(latLng)
        }
        if (previousLatLng == null) {
            currentLatLng = latLng
            previousLatLng = currentLatLng
            movingCabMarker?.position = currentLatLng!!
            movingCabMarker?.setAnchor(0.5f, 0.5f)
            animateCameraInTrip(currentLatLng!!, previousLatLng!!)
        } else {
            // animateCameraInTrip(currentLatLng!!, previousLatLng!!)
            previousLatLng = currentLatLng

            animateCameraInTrip(latLng, previousLatLng!!)
            valueAnimator = AnimationUtils.carAnimator()

            valueAnimator?.addUpdateListener { va ->
                currentLatLng = latLng
                if (currentLatLng != null && previousLatLng != null) {

                    val multiplier = va.animatedFraction
                    val nextLocation = LatLng(
                        multiplier * currentLatLng!!.latitude + (1 - multiplier) * previousLatLng!!.latitude,
                        multiplier * currentLatLng!!.longitude + (1 - multiplier) * previousLatLng!!.longitude
                    )
                    movingCabMarker?.position = nextLocation
                    val rotation = MapUtils.getRotation(previousLatLng!!, nextLocation)
                    if (!rotation.isNaN()) {
                        movingCabMarker?.rotation = rotation
                    }
                    movingCabMarker?.setAnchor(0.5f, 0.5f)
                }
            }
            valueAnimator?.start()
        }

    }

    private fun animateCameraInTrip(latLng: LatLng, previous: LatLng) {

        val cameraPosition = CameraPosition.Builder()
            .bearing(LocationHelper.getBearing(previous, latLng))
            .target(latLng).tilt(45f).zoom(16f).build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            Priority.PRIORITY_HIGH_ACCURACY ->{
                when(resultCode){
                    Activity.RESULT_OK ->{
                        getLocation()
                    }
                    Activity.RESULT_CANCELED ->{
                        locationChecker()
                    }
                }
            }
        }

    }

    private fun checkPermission(){
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) && (
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) ){
            ActivityCompat.requestPermissions(
                this as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),2)
        }else{
            locationChecker()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (resources.getString(R.string.mode) == "Night"){
            setMapDarkStyle(mMap)
        }

//        mMap.uiSettings.apply {
//            isCompassEnabled = true
//            isZoomControlsEnabled = true
//            isMyLocationButtonEnabled = true
//        }

        checkPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 2 && permissions.isNotEmpty() &&
            grantResults[0]== PackageManager.PERMISSION_GRANTED){
            locationChecker()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

}