package com.mosamir.atmodrivecaptain.features.trip.presentation.fragment

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mosamir.atmodrivecaptain.R
import com.mosamir.atmodrivecaptain.databinding.FragmentHomeTripBinding
import com.mosamir.atmodrivecaptain.features.auth.presentation.common.AuthActivity
import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerDetailsResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.model.UpdateAvailabilityResponse
import com.mosamir.atmodrivecaptain.features.trip.presentation.common.RealTimeTripObject
import com.mosamir.atmodrivecaptain.features.trip.presentation.common.SharedViewModel
import com.mosamir.atmodrivecaptain.features.trip.presentation.common.TripViewModel
import com.mosamir.atmodrivecaptain.util.AnimationUtils
import com.mosamir.atmodrivecaptain.util.Constants
import com.mosamir.atmodrivecaptain.util.IResult
import com.mosamir.atmodrivecaptain.util.LocationHelper
import com.mosamir.atmodrivecaptain.util.MapUtils
import com.mosamir.atmodrivecaptain.util.NetworkState
import com.mosamir.atmodrivecaptain.util.SharedPreferencesManager
import com.mosamir.atmodrivecaptain.util.getData
import com.mosamir.atmodrivecaptain.util.showToast
import com.mosamir.atmodrivecaptain.util.visibilityGone
import com.mosamir.atmodrivecaptain.util.visibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

@AndroidEntryPoint
class HomeTripFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeTripBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController

    private lateinit var mMap: GoogleMap
    var mLocationRequest: LocationRequest?= null
    var mLocationCallback: LocationCallback?= null
    var mFusedLocationClient: FusedLocationProviderClient?= null
    private var bottomSheet = BottomSheetBehavior<ConstraintLayout>()
    private var myNavHostFragment: NavHostFragment? = null

    private val tripViewModel by viewModels<TripViewModel>()

    val mapLocation = HashMap<String,Any>()
    var isOnline = false
    private var captainId = ""
    var model = SharedViewModel()
    var tripAccepted = false
    var tripId = 0
    private var status = ""

    private var mBackPressed: Long = 0
    private var movingCabMarker : Marker?= null
    private var previousLatLng: LatLng? = null
    private var currentLatLng: LatLng? = null
    private var valueAnimator: ValueAnimator? = null
    private var valueEventListener : ValueEventListener?= null
    private var valueEventListenerOnTrip : ValueEventListener?= null

    private var pickUpMarker : Marker?= null
    private var dropOffMarker : Marker?= null

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNavController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.database.reference
        captainId = SharedPreferencesManager(requireContext()).getString(Constants.CAPTAIN_ID_PREFS)
        tripAccepted = SharedPreferencesManager(requireContext()).getBoolean(Constants.TRIP_ACCEPT)
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initLocation()
        initBottomSheet()
        onClick()
        observer()
        observeOnRequestStatus()
        updateStatusCaptainLayout()
//        onBackPressHandle()
//        handleBottomSheetSize()

    }

    private fun onClick(){

        binding.imgCategory.setOnClickListener {
            // logout for test only
            SharedPreferencesManager(requireContext()).clearString(Constants.REMEMBER_TOKEN_PREFS)
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        binding.checkBoxCaptainStatus.setOnClickListener {
            if (mapLocation.isNotEmpty())
                tripViewModel.updateAvailability(mapLocation["lat"].toString(),mapLocation["lng"].toString())
        }

    }

    private fun initBottomSheet(){
        val bottomSheetView = view?.findViewById<ConstraintLayout>(R.id.bottom_sheet_trip)
        bottomSheet = BottomSheetBehavior.from(bottomSheetView!!)
        bottomSheet.isDraggable = false
        myNavHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_trip_fragment) as NavHostFragment
    }

    private fun observeOnRequestStatus(){
        model.requestStatus.observe(requireActivity(), Observer {

            if (it){
                // trip accepted
                tripAccepted = true
                SharedPreferencesManager(requireContext()).saveBoolean(Constants.TRIP_ACCEPT,true)
                disPlayBottomSheet(R.navigation.trip_status_nav_graph)
                listenerOnTrip()
            }else{
                clearMap()
            }

        })
    }


    private fun listenerOnTrip(){
        valueEventListenerOnTrip =  object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val tripStatus = snapshot.getValue(String::class.java)

                if(tripStatus != null){
                    status = tripStatus
                    when (status){
                        "accepted" -> {
                            pickUpPassengerMarker()
                        }
                        "on_the_way" -> {
                            pickUpPassengerMarker()
                        }
                        "arrived" -> {
                            pickUpMarker?.remove()
                            dropOffPassengerMarker()
                        }
                        "start_trip" -> {
                            dropOffPassengerMarker()
                        }
                        "pay" -> {
                            dropOffMarker?.remove()
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        database.child("trips").child(tripId.toString()).child("status")
            .addValueEventListener(valueEventListenerOnTrip!!)
    }

    private fun observer(){
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.updateAvaResult.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            val data = networkState.data as IResult<UpdateAvailabilityResponse>
                            val captainStatus = data.getData()?.available
                            SharedPreferencesManager(requireContext()).saveBoolean(
                                Constants.CAPTAIN_STATUS,
                                captainStatus!!
                            )
                            updateStatusCaptainLayout()
                        }

                        NetworkState.Status.FAILED -> {
                            updateStatusCaptainLayout()
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                        }

                        else -> {}
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tripViewModel.onTripResult.collect { networkState ->
                    when (networkState?.status) {
                        NetworkState.Status.SUCCESS -> {
                            val data = networkState.data as IResult<PassengerDetailsResponse>
                            tripAccepted = true
                            tripId = data.getData()?.data?.id!!
                            model.setTripId(tripId)
                            Constants.pickUpLatLng = LatLng(
                                data.getData()?.data?.pickup_lat?.toDouble()!!,
                                data.getData()?.data?.pickup_lng?.toDouble()!!
                            )
                            Constants.dropOffLatLng = LatLng(
                                data.getData()?.data?.dropoff_lat?.toDouble()!!,
                                data.getData()?.data?.dropoff_lng?.toDouble()!!
                            )
                            disPlayBottomSheet(R.navigation.trip_status_nav_graph)
                            listenerOnTrip()
                        }

                        NetworkState.Status.FAILED -> {
                            showToast(networkState.msg.toString())
                        }

                        NetworkState.Status.RUNNING -> {
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun listenerOnTripId(){
        valueEventListener =  object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val id = snapshot.getValue(Int::class.java)

                if (id != null){
                    if (id != 0){
                        tripId = id
                        model.setTripId(id)
                        if(!tripAccepted){
                            disPlayBottomSheet(R.navigation.trip_sheet_nav_graph)
                        }
                    }else{
                        clearMap()
                    }
                    SharedPreferencesManager(requireContext()).saveBoolean(Constants.CAPTAIN_STATUS,true)
                    updateStatusCaptainLayout()
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        database.child("OnlineCaptains").child(captainId).child("tripId")
            .addValueEventListener(valueEventListener!!)
    }

    private fun disPlayBottomSheet(nav:Int) {
        bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        Constants.isBottomSheetOn = true
        binding.layoutCaptainStatus.visibilityGone()
        val finalHost = NavHostFragment.create(nav)
        childFragmentManager.beginTransaction()
            .replace(R.id.nav_host_trip_fragment, finalHost)
            .setPrimaryNavigationFragment(finalHost)
            .commit()
    }


    private fun handleBottomSheetSize() {

        myNavHostFragment?.navController?.addOnDestinationChangedListener { _, destination, arguments ->

            if (destination.id == R.id.newRequestFragment2 || destination.id == R.id.tripLifecycleFragment || destination.id == R.id.tripFinishedFragment) {
                changeHeightOfSheet(requireContext(), 0.90)
                bottomSheet.isDraggable = false
            }

        }

    }

    private fun changeHeightOfSheet(context: Context, percent: Double) {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        bottomSheet.peekHeight = (displayMetrics.heightPixels * percent).toInt()
    }

    private fun onBackPressHandle() {

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

            val childFragment = myNavHostFragment?.childFragmentManager?.fragments

            if (childFragment?.size != 0 && Constants.isBottomSheetOn) {
                var fragment = childFragment?.get(0)

                if ((fragment is NewRequestFragment)
                    && bottomSheet.state == BottomSheetBehavior.STATE_EXPANDED
                ) {
                    clearMap()
                }
            } else {
                requireActivity().finish()
            }

        }

    }

    private fun clearMap(){
        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.layoutCaptainStatus.visibilityVisible()
        tripAccepted = false
        SharedPreferencesManager(requireContext()).saveBoolean(Constants.TRIP_ACCEPT,false)
        pickUpMarker = null
        dropOffMarker = null
        mMap.clear()
        Constants.pickUpLatLng = null
        Constants.dropOffLatLng = null
        getLocation()
        if(Constants.captainLatLng != null){
            addCarMarkerAndGet(Constants.captainLatLng!!)
        }
    }

    private fun updateStatusCaptainLayout(){

        val captainStatus = SharedPreferencesManager(requireContext()).getBoolean(Constants.CAPTAIN_STATUS)

        if (!captainStatus){
            binding.tvCaptainStatus.apply {
                isOnline = false
                text = "You are offline"
                setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            binding.layoutCaptainStatus.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.error))
            binding.checkBoxCaptainStatus.isChecked = false

        }else{
            binding.tvCaptainStatus.apply {
                isOnline = true
                text = "You are online"
                setTextColor(ContextCompat.getColor(requireContext(), R.color.title))
            }
            binding.layoutCaptainStatus.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background))
            binding.checkBoxCaptainStatus.isChecked = true
        }

    }


    private fun setMapDarkStyle(map: GoogleMap) {
        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style_dark)
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
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
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
                val lat = latLng.latitude.toString()
                val lng = latLng.longitude.toString()
                mapLocation["lat"] = lat
                mapLocation["lng"] = lng
                updateCarLocation(LatLng(latLng.latitude,latLng.longitude))
                Constants.captainLatLng = LatLng(latLng.latitude,latLng.longitude)

                if (isOnline){
                    database.child("OnlineCaptains").child(captainId).updateChildren(mapLocation)
                    if (tripAccepted){
                        database.child("trips").child(tripId.toString()).updateChildren(mapLocation)
                    }
                }

            }
        }

        mFusedLocationClient?.requestLocationUpdates(mLocationRequest!!,mLocationCallback!!, Looper.getMainLooper())

    }

    private fun moveCameraMap(latLng: LatLng){
        val cameraPosition = CameraPosition.builder().target(latLng).zoom(15f).build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun locationChecker(){

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest!!)

        val result : Task<LocationSettingsResponse> = LocationServices.getSettingsClient(requireContext())
            .checkLocationSettings(builder.build())

        result.addOnCompleteListener {task ->

            try {
                task.getResult(ApiException::class.java)
                getLocation()
                tripViewModel.onTrip()
                listenerOnTripId()
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

    private fun pickUpPassengerMarker(){
        if(pickUpMarker == null){
            pickUpMarker = addPickUpMarker(Constants.pickUpLatLng!!)
        }
        pickUpMarker?.position = Constants.pickUpLatLng!!
    }

    private fun dropOffPassengerMarker(){
        if (dropOffMarker == null){
            dropOffMarker = addDropOffMarker(Constants.dropOffLatLng!!)
        }
        dropOffMarker?.position = Constants.dropOffLatLng!!
    }

    private fun addPickUpMarker(latLng: LatLng): Marker {
        val bitmapDescriptor =
            BitmapDescriptorFactory.fromBitmap(MapUtils.getPickupBitmap(requireContext()))
        return mMap.addMarker(
            MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor)
        )!!
    }

    private fun addDropOffMarker(latLng: LatLng): Marker {
        val bitmapDescriptor =
            BitmapDescriptorFactory.fromBitmap(MapUtils.getDropOffBitmap(requireContext()))
        return mMap.addMarker(
            MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor)
        )!!
    }

    private fun addCarMarkerAndGet(latLng: LatLng): Marker {
        val bitmapDescriptor =
            BitmapDescriptorFactory.fromBitmap(MapUtils.getCarBitmap(requireContext()))
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
                        tripViewModel.onTrip()
                        listenerOnTripId()
                    }
                    Activity.RESULT_CANCELED ->{
                        locationChecker()
                    }
                }
            }
        }

    }

    private fun checkPermission(){
        if ((ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) && (
                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) ){
            ActivityCompat.requestPermissions(
                requireActivity(),
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        database.child("OnlineCaptains").child(captainId).child("tripId")
            .removeEventListener(valueEventListener!!)
        if(tripId != 0)
            database.child("trips").child(tripId.toString()).child("status")
                .removeEventListener(valueEventListenerOnTrip!!)
    }

}