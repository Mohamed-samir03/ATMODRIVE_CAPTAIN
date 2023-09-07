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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            binding.apply {
                imgPersonalPhoto.setImageURI(null)
                deletePersonalPhoto.visibilityGone()
                editPersonalPhoto.visibilityGone()
                uploadPersonalPhoto.visibilityGone()
                layoutPersonalPhoto.enabled()
            }
            avatar = ""
        }
        binding.deleteIdFront.setOnClickListener {
            binding.apply {
                imgIdFront.setImageURI(null)
                deleteIdFront.visibilityGone()
                editIdFront.visibilityGone()
                uploadIdFront.visibilityGone()
                layoutIdFront.enabled()
            }
            idFront = ""
        }
        binding.deleteIdBack.setOnClickListener {
            binding.apply {
                imgIdBlack.setImageURI(null)
                deleteIdBack.visibilityGone()
                editIdBack.visibilityGone()
                uploadIdBack.visibilityGone()
                layoutIdBack.enabled()
            }
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

        binding.uploadPersonalPhoto.setOnClickListener {
            if (personalPhoto != null){
                uploadImage(personalPhoto!!)
                imageUploading = "PersonalPhoto"
            }
        }

        binding.uploadIdFront.setOnClickListener {
            if (idFrontPhoto != null){
                uploadImage(idFrontPhoto!!)
                imageUploading = "IdFront"
            }
        }

        binding.uploadIdBack.setOnClickListener {
            if (idBackPhoto != null){
                uploadImage(idBackPhoto!!)
                imageUploading = "IdBack"
            }
        }


        binding.btnSubmitPersonalInformation.setOnClickListener {
            if(!avatar.isNullOrBlank() && !idFront.isNullOrBlank() && !idBack.isNullOrBlank()){
                val mobile = args.mobile.toString()
                var isDarkMode = 0
                if (resources.getString(R.string.mode) == "Night"){
                    isDarkMode = 1
                }
                personInfoViewModel.registerCaptain(
                    mobile, avatar,"device_token","device_id","android",
                    idFront, idBack,
                    "captains/679d4821359d42ce14f4af0d75637fa9807d2c16.png",
                    "captains/679d4821359d42ce14f4af0d75637fa9807d2c16.png",
                    isDarkMode
                )
            }else {
                showToast("Submit images")
            }
        }

        observeOnRegisterCaptain()

        observeOnUploadFile()

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
                        blockUI(false)
                        val data = networkState.data as IResult<FileUploadResponse>
                        binding.personalInfoProgressBar.visibilityGone()
                        val image = data.getData()?.data.toString()
                        imageUploaded(image)
                    }
                    NetworkState.Status.FAILED ->{
                        blockUI(false)
                        showToast(networkState.msg.toString())
                        binding.personalInfoProgressBar.visibilityGone()
                    }
                    NetworkState.Status.RUNNING ->{
                        blockUI(true)
                        binding.personalInfoProgressBar.visibilityVisible()
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
                    binding.apply {
                        imgPersonalPhoto.setImageURI(selectedImageUri)
                        deletePersonalPhoto.visibilityVisible()
                        uploadPersonalPhoto.visibilityVisible()
                        layoutPersonalPhoto.disable()
                    }
                    personalPhoto = selectedImageUri
                }
                "IdFront" -> {
                    binding.apply {
                        imgIdFront.setImageURI(selectedImageUri)
                        deleteIdFront.visibilityVisible()
                        uploadIdFront.visibilityVisible()
                        layoutIdFront.disable()
                    }
                    idFrontPhoto = selectedImageUri
                }
                "IdBack" -> {
                    binding.apply {
                        imgIdBlack.setImageURI(selectedImageUri)
                        deleteIdBack.visibilityVisible()
                        uploadIdBack.visibilityVisible()
                        layoutIdBack.disable()
                    }
                    idBackPhoto = selectedImageUri
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun imageUploaded(image:String){
        when (imageUploading) {
            "PersonalPhoto" -> {
                avatar = image
                binding.apply {
                    uploadPersonalPhoto.visibilityGone()
                    editPersonalPhoto.visibilityVisible()
                    deletePersonalPhoto.visibilityGone()
                    layoutPersonalPhoto.disable()
                }
            }
            "IdFront" -> {
                idFront = image
                binding.apply {
                    uploadIdFront.visibilityGone()
                    editIdFront.visibilityVisible()
                    deleteIdFront.visibilityGone()
                    layoutIdFront.disable()
                }

            }
            "IdBack" -> {
                idBack = image
                binding.apply {
                    uploadIdBack.visibilityGone()
                    editIdBack.visibilityVisible()
                    deleteIdBack.visibilityGone()
                    layoutIdBack.disable()
                }
            }
        }
    }

    private fun blockUI(blocked: Boolean){
        if (blocked){
            binding.apply {
                layoutPersonalPhoto.disable()
                layoutIdFront.disable()
                layoutIdBack.disable()
                deletePersonalPhoto.disable()
                deleteIdFront.disable()
                deleteIdBack.disable()
                editPersonalPhoto.disable()
                editIdFront.disable()
                editIdBack.disable()
                uploadPersonalPhoto.disable()
                uploadIdFront.disable()
                uploadIdBack.disable()
                btnSubmitPersonalInformation.disable()
            }
        }else{
            binding.apply {
                layoutPersonalPhoto.enabled()
                layoutIdFront.enabled()
                layoutIdBack.enabled()
                deletePersonalPhoto.enabled()
                deleteIdFront.enabled()
                deleteIdBack.enabled()
                editPersonalPhoto.enabled()
                editIdFront.enabled()
                editIdBack.enabled()
                uploadPersonalPhoto.enabled()
                uploadIdFront.enabled()
                uploadIdBack.enabled()
                btnSubmitPersonalInformation.enabled()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}