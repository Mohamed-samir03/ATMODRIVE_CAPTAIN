package com.mosamir.atmodrivecaptain.futures.auth.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mosamir.atmodrivecaptain.R
import com.mosamir.atmodrivecaptain.databinding.FragmentVehicleInformationBinding
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.FileUploadResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.register.RegisterResponse
import com.mosamir.atmodrivecaptain.futures.auth.presentation.common.AuthViewModel
import com.mosamir.atmodrivecaptain.util.Constants
import com.mosamir.atmodrivecaptain.util.IResult
import com.mosamir.atmodrivecaptain.util.NetworkState
import com.mosamir.atmodrivecaptain.util.SharedPreferencesManager
import com.mosamir.atmodrivecaptain.util.decodeFile
import com.mosamir.atmodrivecaptain.util.disable
import com.mosamir.atmodrivecaptain.util.enabled
import com.mosamir.atmodrivecaptain.util.getData
import com.mosamir.atmodrivecaptain.util.showToast
import com.mosamir.atmodrivecaptain.util.visibilityGone
import com.mosamir.atmodrivecaptain.util.visibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class VehicleInformationFragment:Fragment() {

    private var _binding: FragmentVehicleInformationBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController
    private var imageType = ""
    private var side = -1
    private var editSide = listOf<ImageView>()
    private var sides = listOf<ImageView>()
    private var carSides = listOf<ImageView>()
    private var carImagesPath = MutableList(6) { "" }
    private lateinit var sideImage :ImageView
    private lateinit var close:ImageView
    private lateinit var confirm:Button
    private lateinit var carImagesPB:ProgressBar
    private var carImagesAdded = false
    private val vehicleViewModel by viewModels<AuthViewModel>()
    private var licenseFrontPhoto:Uri? = null
    private var licenseBackPhoto:Uri? = null
    private var vehicleLicenseFront = ""
    private var vehicleLicenseBack = ""
    private var imageUploading = ""
//    private var bottomSheet = BottomSheetBehavior<ConstraintLayout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNavController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVehicleInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomSheetDialog()

        carSides = listOf(binding.imgSide1,binding.imgSide2,binding.imgSide3,binding.imgSide4,binding.imgSide5,binding.imgSide6)

        sides.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                imageType = imageView.id.toString()
                sideImage = imageView
                side = index
                openGallery()
            }
        }

        binding.layoutCarLicenseFront.setOnClickListener {
            imageType = "LicenseFront"
            openGallery()
        }

        binding.layoutCarLicenseBack.setOnClickListener {
            imageType = "LicenseBack"
            openGallery()
        }

        binding.deleteLicenseFront.setOnClickListener {
            binding.apply {
                imgLicenseFront.setImageURI(null)
                deleteLicenseFront.visibilityGone()
                editLicenseFront.visibilityGone()
                uploadLicenseFront.visibilityGone()
                layoutCarLicenseFront.enabled()
            }
            vehicleLicenseFront = ""
        }

        binding.deleteLicenseBack.setOnClickListener {
            binding.apply {
                imgLicenseBack.setImageURI(null)
                deleteLicenseBack.visibilityGone()
                editLicenseBack.visibilityGone()
                uploadLicenseBack.visibilityGone()
                layoutCarLicenseBack.enabled()
            }
            vehicleLicenseBack = ""
        }

        binding.editLicenseFront.setOnClickListener {
            imageType = "LicenseFront"
            openGallery()
        }

        binding.editLicenseBack.setOnClickListener {
            imageType = "LicenseBack"
            openGallery()
        }

        binding.uploadLicenseFront.setOnClickListener {
            if (licenseFrontPhoto != null){
                uploadImage(licenseFrontPhoto!!)
                imageUploading = "LicenseFront"
            }
        }

        binding.uploadLicenseBack.setOnClickListener {
            if (licenseBackPhoto != null){
                uploadImage(licenseBackPhoto!!)
                imageUploading = "LicenseBack"
            }
        }
        observeOnUploadFile()
        binding.btnSubmitVehicleInformation.setOnClickListener {
            if (!vehicleLicenseFront.isNullOrBlank() && !vehicleLicenseBack.isNullOrBlank() && carImagesAdded){
                vehicleViewModel.registerVehicle(
                    carImagesPath[0],carImagesPath[1],carImagesPath[2],carImagesPath[3],carImagesPath[4],carImagesPath[5],vehicleLicenseFront,vehicleLicenseBack
                )
            }else{
                showToast("Add images")
            }
        }
        observeOnRegisterVehicle()

    }

    private fun observeOnUploadFile(){
        lifecycleScope.launch {
            vehicleViewModel.uploadFileResult.collect{ networkState ->
                when(networkState?.status){
                    NetworkState.Status.SUCCESS ->{
                        blockUI(false)
                        val data = networkState.data as IResult<FileUploadResponse>
                        binding.vehicleInfoProgressBar.visibilityGone()
                        carImagesPB.visibilityGone()
                        val image = data.getData()?.data.toString()
                        imageUploaded(image)
                    }
                    NetworkState.Status.FAILED ->{
                        blockUI(false)
                        showToast(networkState.msg.toString())
                        binding.vehicleInfoProgressBar.visibilityGone()
                        carImagesPB.visibilityGone()
                        carImageNotUpload(side)
                    }
                    NetworkState.Status.RUNNING ->{
                        blockUI(true)
                        binding.vehicleInfoProgressBar.visibilityVisible()
                        carImagesPB.visibilityVisible()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun observeOnRegisterVehicle(){
        lifecycleScope.launch {
            vehicleViewModel.registerVehicleResult.collect{ networkState ->
                when(networkState?.status){
                    NetworkState.Status.SUCCESS ->{
                        val data = networkState.data as IResult<RegisterResponse>
                        saveCaptainDate(data)
                        val action =
                            VehicleInformationFragmentDirections.actionCreateAccountVehicleInformationToCreateAccountBankAccount()
                        mNavController.navigate(action)
                        binding.vehicleInfoProgressBar.visibilityGone()
                    }
                    NetworkState.Status.FAILED ->{
                        showToast(networkState.msg.toString())
                        binding.vehicleInfoProgressBar.visibilityGone()
                    }
                    NetworkState.Status.RUNNING ->{
                        binding.vehicleInfoProgressBar.visibilityVisible()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun uploadImage(image: Uri) {
        lifecycleScope.launch(Dispatchers.IO) {

            val a = decodeFile(image.path)
            val baos = ByteArrayOutputStream()
            a?.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val imageBytes: ByteArray = baos.toByteArray()

            val requestFile =
                imageBytes.toRequestBody("image/*".toMediaTypeOrNull(), 0, imageBytes.size)
            val body =
                MultipartBody.Part.createFormData("file", "image.jpg", requestFile)

            val name: RequestBody = Constants.VEHICLE_IMAGE_PATH
                .toRequestBody("text/plain".toMediaTypeOrNull())

            println("ADD_VEHICLE_uploadImage  ${image.path}")

            vehicleViewModel.uploadFile(body, name)

        }
    }

    private fun openGallery(){
        ImagePicker.with(this)
            .crop()	    			//Crop image(Optional), Check Customization for more option
            .cameraOnly()
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            when (imageType) {
                "LicenseFront" -> {
                    binding.apply {
                        imgLicenseFront.setImageURI(selectedImageUri)
                        deleteLicenseFront.visibilityVisible()
                        editLicenseFront.visibilityGone()
                        uploadLicenseFront.visibilityVisible()
                        layoutCarLicenseFront.disable()
                    }
                    licenseFrontPhoto = selectedImageUri
                }
                "LicenseBack" -> {
                    binding.apply {
                        imgLicenseBack.setImageURI(selectedImageUri)
                        deleteLicenseBack.visibilityVisible()
                        editLicenseBack.visibilityGone()
                        uploadLicenseBack.visibilityVisible()
                        layoutCarLicenseBack.disable()
                    }
                    licenseBackPhoto = selectedImageUri
                }
                else -> {
                    sideImage.setImageURI(selectedImageUri)
                    editSide[side].visibilityVisible()
                    uploadImage(selectedImageUri!!)
                }
            }
        }else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setBottomSheetDialog(){

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_pick_car_images)
        close = bottomSheetDialog.findViewById<ImageView>(R.id.close_bottom_sheet_car_images)!!
        val side1 = bottomSheetDialog.findViewById<ImageView>(R.id.img_car_side1)
        val side2 = bottomSheetDialog.findViewById<ImageView>(R.id.img_car_side2)
        val side3 = bottomSheetDialog.findViewById<ImageView>(R.id.img_car_side3)
        val side4 = bottomSheetDialog.findViewById<ImageView>(R.id.img_car_side4)
        val side5 = bottomSheetDialog.findViewById<ImageView>(R.id.img_car_side5)
        val side6 = bottomSheetDialog.findViewById<ImageView>(R.id.img_car_side6)
        val editSide1 = bottomSheetDialog.findViewById<ImageView>(R.id.edit_car_side1)
        val editSide2 = bottomSheetDialog.findViewById<ImageView>(R.id.edit_car_side2)
        val editSide3 = bottomSheetDialog.findViewById<ImageView>(R.id.edit_car_side3)
        val editSide4 = bottomSheetDialog.findViewById<ImageView>(R.id.edit_car_side4)
        val editSide5 = bottomSheetDialog.findViewById<ImageView>(R.id.edit_car_side5)
        val editSide6 = bottomSheetDialog.findViewById<ImageView>(R.id.edit_car_side6)
        confirm = bottomSheetDialog.findViewById<Button>(R.id.btn_confirm_car_images)!!
        carImagesPB = bottomSheetDialog.findViewById<ProgressBar>(R.id.car_images_progressBar)!!

        sides = listOf(side1!!,side2!!,side3!!,side4!!,side5!!,side6!!)
        editSide = listOf(editSide1, editSide2, editSide3, editSide4, editSide5, editSide6) as List<ImageView>

        confirm.setOnClickListener {
            var i = 0
            if (!carImagesPath.any { it.isBlank() }){
                for (img in carSides){
                    img.setImageBitmap((sides[i++].drawable as? BitmapDrawable)?.bitmap )
                }
                bottomSheetDialog.dismiss()
                binding.editCarSidesImages.isVisible = true
                carImagesAdded = true
            }else{
                showToast("Pick Car Images")
            }
        }

        close.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.layoutCarImages.setOnClickListener {
            bottomSheetDialog.show()
        }
    }

    private fun imageUploaded(image:String){
        when (imageUploading) {
            "LicenseFront" -> {
                vehicleLicenseFront = image
                binding.apply {
                    deleteLicenseFront.visibilityGone()
                    editLicenseFront.visibilityVisible()
                    uploadLicenseFront.visibilityGone()
                    layoutCarLicenseFront.disable()
                }
            }
            "LicenseBack" -> {
                vehicleLicenseBack = image
                binding.apply {
                    deleteLicenseBack.visibilityGone()
                    editLicenseBack.visibilityVisible()
                    uploadLicenseBack.visibilityGone()
                    layoutCarLicenseBack.disable()
                }
            }
            else -> {
                when (side) {
                    0 -> {
                        carImagesPath[0] = image
                    }
                    1 -> {
                        carImagesPath[1] = image
                    }
                    2 -> {
                        carImagesPath[2] = image
                    }
                    3 -> {
                        carImagesPath[3] = image
                    }
                    4 -> {
                        carImagesPath[4] = image
                    }
                    5 -> {
                        carImagesPath[5] = image
                    }
                }
            }
        }
    }

    private fun carImageNotUpload(side : Int){
        when (side) {
            0 -> {
                sideImage.setImageResource(R.drawable.side1)
                editSide[side].visibilityGone()
            }
            1 -> {
                sideImage.setImageResource(R.drawable.side1)
                editSide[side].visibilityGone()
            }
            2 -> {
                sideImage.setImageResource(R.drawable.side3)
                editSide[side].visibilityGone()
            }
            3 -> {
                sideImage.setImageResource(R.drawable.side4)
                editSide[side].visibilityGone()
            }
            4 -> {
                sideImage.setImageResource(R.drawable.side4)
                editSide[side].visibilityGone()
            }
            5 -> {
                sideImage.setImageResource(R.drawable.side3)
                editSide[side].visibilityGone()
            }
        }
    }

    private fun blockUI(blocked: Boolean){

        if (blocked){
            binding.apply {
                layoutCarImages.disable()
                editCarSidesImages.disable()
                layoutCarLicenseFront.disable()
                editLicenseFront.disable()
                deleteLicenseFront.disable()
                uploadLicenseFront.disable()
                layoutCarLicenseBack.disable()
                editLicenseBack.disable()
                deleteLicenseBack.disable()
                uploadLicenseBack.disable()
                sides.forEachIndexed { index, imageView ->
                    imageView.disable()
                }
                close.disable()
                confirm.disable()
            }
        }else{
            binding.apply {
                layoutCarImages.enabled()
                editCarSidesImages.enabled()
                layoutCarLicenseFront.enabled()
                editLicenseFront.enabled()
                deleteLicenseFront.enabled()
                uploadLicenseFront.enabled()
                layoutCarLicenseBack.enabled()
                editLicenseBack.enabled()
                deleteLicenseBack.enabled()
                uploadLicenseBack.enabled()
                sides.forEachIndexed { index, imageView ->
                    imageView.enabled()
                }
                close.enabled()
                confirm.enabled()
            }
        }

    }

    private fun saveCaptainDate(userData : IResult<RegisterResponse>){

        val data = userData.getData()?.data
        val myPrefs = SharedPreferencesManager(requireContext())

        myPrefs.saveString(Constants.AVATAR_PREFS,data!!.avatar)
        myPrefs.saveString(Constants.EMAIL_PREFS,data.email.toString())
        myPrefs.saveString(Constants.FULL_NAME_PREFS,data.full_name.toString())
        myPrefs.saveString(Constants.IS_DARK_MODE_PREFS,data.is_dark_mode.toString())
        myPrefs.saveString(Constants.LANG_PREFS,data.lang)
        myPrefs.saveString(Constants.MOBILE_PREFS,data.mobile)
        myPrefs.saveString(Constants.CAPTAIN_CODE_PREFS,data.captain_code)
        myPrefs.saveString(Constants.BIRTHDAY_PREFS,data.birthday.toString())
        myPrefs.saveString(Constants.REMEMBER_TOKEN_PREFS,data.remember_token)
        myPrefs.saveString(Constants.GENDER_PREFS,data.gender.toString())
        myPrefs.saveString(Constants.STATUS_PREFS,data.status.toString())
        myPrefs.saveString(Constants.IS_ACTIVE_PREFS,data.is_active.toString())
        myPrefs.saveString(Constants.NATIONALITY_PREFS,data.nationality.toString())
        myPrefs.saveString(Constants.REGISTER_STEP_PREFS,data.register_step.toString())

    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val bottomSheetView = view.findViewById<ConstraintLayout>(R.id.car_bottom_sheet)
//        bottomSheet = BottomSheetBehavior.from(bottomSheetView!!)
//        bottomSheet.isDraggable = true
//        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
//
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}