package com.mosamir.atmodrivecaptain.futures.auth.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.mosamir.atmodrivecaptain.R
import com.mosamir.atmodrivecaptain.databinding.FragmentPersonalInformationBinding
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.FileUploadResponse
import com.mosamir.atmodrivecaptain.futures.auth.presentation.common.AuthViewModel
import com.mosamir.atmodrivecaptain.util.Constants
import com.mosamir.atmodrivecaptain.util.IResult
import com.mosamir.atmodrivecaptain.util.NetworkState
import com.mosamir.atmodrivecaptain.util.decodeFile
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
class PersonalInformationFragment:Fragment() {

    private var _binding: FragmentPersonalInformationBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController
    private var imageType = ""
    private val personInfoViewModel by viewModels<AuthViewModel>()
    private val args by navArgs<PersonalInformationFragmentArgs>()
    private var personalPhoto:Uri? = null
    private var idFrontPhoto:Uri? = null
    private var idBackPhoto:Uri? = null
    private var avatar = ""
    private var idFront = ""
    private var idBack = ""
    private var imageUploading = ""
    private var imagesUris: List<Uri> = listOf()
    private var images: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNavController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPersonalInformationBinding.inflate(inflater, container, false)

        binding.layoutPersonalPhoto.setOnClickListener {
            imageType  = "PersonalPhoto"
            openGallery()
        }
        binding.layoutIdFront.setOnClickListener {
            imageType  = "IdFront"
            openGallery()
        }
        binding.layoutIdBack.setOnClickListener {
            imageType  = "IdBack"
            openGallery()
        }

        binding.deletePersonalPhoto.setOnClickListener {
            binding.imgPersonalPhoto.setImageURI(null)
            binding.deletePersonalPhoto.isVisible = false
            binding.editPersonalPhoto.isVisible = false
            binding.uploadPersonalPhoto.visibilityGone()
            avatar = ""
        }
        binding.deleteIdFront.setOnClickListener {
            binding.imgIdFront.setImageURI(null)
            binding.deleteIdFront.isVisible = false
            binding.editIdFront.isVisible = false
            binding.uploadIdFront.visibilityGone()
            idFront = ""
        }
        binding.deleteIdBack.setOnClickListener {
            binding.imgIdBlack.setImageURI(null)
            binding.deleteIdBack.isVisible = false
            binding.editIdBack.isVisible = false
            binding.uploadIdBack.visibilityGone()
            idBack = ""
        }

        binding.editPersonalPhoto.setOnClickListener {
            imageType  = "PersonalPhoto"
            openGallery()
        }
        binding.editIdFront.setOnClickListener {
            imageType  = "IdFront"
            openGallery()
        }
        binding.editIdBack.setOnClickListener {
            imageType  = "IdBack"
            openGallery()
        }

//        binding.uploadPersonalPhoto.setOnClickListener {
//            if (personalPhoto != null){
//                uploadImage(personalPhoto!!)
//                imageUploading = "PersonalPhoto"
//            }
//        }
//
//        binding.uploadIdFront.setOnClickListener {
//            if (idFrontPhoto != null){
//                uploadImage(idFrontPhoto!!)
//                imageUploading = "IdFront"
//            }
//        }
//
//        binding.uploadIdBack.setOnClickListener {
//            if (idBackPhoto != null){
//                uploadImage(idBackPhoto!!)
//                imageUploading = "IdBack"
//            }
//        }


        binding.btnSubmitPersonalInformation.setOnClickListener {
//            if(personalPhoto != null && idFrontPhoto != null && idBackPhoto != null){
//                imagesUris = listOf(personalPhoto!!,idFrontPhoto!!,idBackPhoto!!)
//                lifecycleScope.launch {
//                    for (uri in imagesUris) {
//                        uploadImage(uri)
//                    }
//                }
//            }else {
//                showToast("pick images")
//            }


            val mobile = args.mobile.toString()
            personInfoViewModel.registerCaptain(
                mobile, avatar,"device_token","device_id","android",
                idFront, idBack,
                "captains/679d4821359d42ce14f4af0d75637fa9807d2c16.png",
                "captains/679d4821359d42ce14f4af0d75637fa9807d2c16.png",
                1
            )
        }
        observeOnRegisterCaptain()

        observeOnUploadFile()

        return binding.root
    }


    private fun observeOnRegisterCaptain(){
        lifecycleScope.launch {
            personInfoViewModel.registerCaptainResult.collect{ networkState ->
                when(networkState?.status){
                    NetworkState.Status.SUCCESS ->{
                        val action = PersonalInformationFragmentDirections.actionCreateAccountPersonalInformationToCreateAccountVehicleInformation()
                        mNavController.navigate(action)
                        binding.personalInfoProgressBar.visibilityGone()
                    }
                    NetworkState.Status.FAILED ->{
                        showToast(networkState.msg.toString())
                        binding.personalInfoProgressBar.visibilityGone()
                    }
                    NetworkState.Status.RUNNING ->{
                        binding.personalInfoProgressBar.visibilityVisible()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun observeOnUploadFile(){
        lifecycleScope.launch {
            personInfoViewModel.uploadFileResult.collect{ networkState ->
                when(networkState?.status){
                    NetworkState.Status.SUCCESS ->{
                        val data = networkState.data as IResult<FileUploadResponse>
                        binding.personalInfoProgressBar.visibilityGone()
                        val image = data.getData()?.data.toString()
                        images.add(image)
                        showToast("s"+images.size)
                        if(images.size == 3){
                            Glide.with(requireContext()).load("https://s1.drive.dashboard.atmosphere.solutions/storage/"+images[0]).centerCrop().placeholder(R.drawable.upload).into(binding.testImage1)
                            Glide.with(requireContext()).load("https://s1.drive.dashboard.atmosphere.solutions/storage/"+images[1]).centerCrop().placeholder(R.drawable.upload).into(binding.testImage2)
                            Glide.with(requireContext()).load("https://s1.drive.dashboard.atmosphere.solutions/storage/"+images[2]).centerCrop().placeholder(R.drawable.upload).into(binding.testImage3)
                        }

//                        if (imageUploading == "PersonalPhoto"){
//                            avatar = data.getData()?.data.toString()
//                            showToast("avatar"+data.getData()?.data.toString())
//                            Glide.with(requireContext()).load("https://s1.drive.dashboard.atmosphere.solutions/storage/"+avatar).centerCrop().placeholder(R.drawable.upload).into(binding.testImage1)
//                            binding.uploadPersonalPhoto.visibilityGone()
//                            binding.editPersonalPhoto.visibilityVisible()
//                            binding.deletePersonalPhoto.visibilityGone()
//                        }else if(imageUploading == "IdFront"){
//                            idFront = data.getData()?.data.toString()
//                            showToast("idFront"+data.getData()?.data.toString())
//                            Glide.with(requireContext()).load("https://s1.drive.dashboard.atmosphere.solutions/storage/"+idFront).centerCrop().placeholder(R.drawable.upload).into(binding.testImage2)
//                            binding.uploadIdFront.visibilityGone()
//                            binding.editIdFront.visibilityVisible()
//                            binding.deleteIdFront.visibilityGone()
//                        }else if (imageUploading == "IdBack"){
//                            idBack = data.getData()?.data.toString()
//                            showToast("idBack"+idBack)
//                            Glide.with(requireContext()).load("https://s1.drive.dashboard.atmosphere.solutions/storage/"+idBack).centerCrop().placeholder(R.drawable.upload).into(binding.testImage3)
//                            binding.uploadIdBack.visibilityGone()
//                            binding.editIdBack.visibilityVisible()
//                            binding.deleteIdBack.visibilityGone()
//                        }

                    }
                    NetworkState.Status.FAILED ->{
                        showToast(networkState.msg.toString())
                        binding.personalInfoProgressBar.visibilityGone()
                    }
                    NetworkState.Status.RUNNING ->{
                        binding.personalInfoProgressBar.visibilityVisible()
                    }
                    else -> {}
                }
            }
        }
    }

    private suspend fun uploadImage(image: Uri) {
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

            personInfoViewModel.uploadFile(body, name)

        }
    }

    private fun openGallery(){
        ImagePicker.with(this)
            .crop() //Crop image(Optional), Check Customization for more option
            .cameraOnly()
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            when (imageType) {
                "PersonalPhoto" -> {
                    binding.imgPersonalPhoto.setImageURI(selectedImageUri)
                    binding.deletePersonalPhoto.visibilityVisible()
                    binding.uploadPersonalPhoto.visibilityVisible()
                    personalPhoto = selectedImageUri
                }
                "IdFront" -> {
                    binding.imgIdFront.setImageURI(selectedImageUri)
                    binding.deleteIdFront.visibilityVisible()
                    binding.uploadIdFront.visibilityVisible()
                    idFrontPhoto = selectedImageUri
                }
                "IdBack" -> {
                    binding.imgIdBlack.setImageURI(selectedImageUri)
                    binding.deleteIdBack.visibilityVisible()
                    binding.uploadIdBack.visibilityVisible()
                    idBackPhoto = selectedImageUri
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}