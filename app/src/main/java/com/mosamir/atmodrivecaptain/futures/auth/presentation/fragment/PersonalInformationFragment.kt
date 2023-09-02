package com.mosamir.atmodrivecaptain.futures.auth.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.media.Image
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
import com.github.dhaval2404.imagepicker.ImagePicker
import com.mosamir.atmodrivecaptain.databinding.FragmentPersonalInformationBinding
import com.mosamir.atmodrivecaptain.futures.auth.presentation.common.AuthViewModel
import com.mosamir.atmodrivecaptain.util.NetworkState
import com.mosamir.atmodrivecaptain.util.showToast
import com.mosamir.atmodrivecaptain.util.visibilityGone
import com.mosamir.atmodrivecaptain.util.visibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PersonalInformationFragment:Fragment() {

    private var _binding: FragmentPersonalInformationBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController
    private var imageType = ""
    private val personInfoViewModel by viewModels<AuthViewModel>()
    private val args by navArgs<PersonalInformationFragmentArgs>()

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
        }
        binding.deleteIdFront.setOnClickListener {
            binding.imgIdFront.setImageURI(null)
            binding.deleteIdFront.isVisible = false
            binding.editIdFront.isVisible = false
        }
        binding.deleteIdBack.setOnClickListener {
            binding.imgIdBlack.setImageURI(null)
            binding.deleteIdBack.isVisible = false
            binding.editIdBack.isVisible = false
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


        binding.btnSubmitPersonalInformation.setOnClickListener {
            val mobile = args.mobile.toString()
            personInfoViewModel.registerCaptain(
                mobile, "avatar.png","device_token","device_id","android",
                "captains/679d4821359d42ce14f4af0d75637fa9807d2c16.png",
                "captains/679d4821359d42ce14f4af0d75637fa9807d2c16.png",
                "captains/679d4821359d42ce14f4af0d75637fa9807d2c16.png",
                "captains/679d4821359d42ce14f4af0d75637fa9807d2c16.png",
                1
            )
        }
        observeOnRegisterCaptain()

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
                    binding.deletePersonalPhoto.isVisible = true
                    binding.editPersonalPhoto.isVisible = true
                }
                "IdFront" -> {
                    binding.imgIdFront.setImageURI(selectedImageUri)
                    binding.deleteIdFront.isVisible = true
                    binding.editIdFront.isVisible = true
                }
                "IdBack" -> {
                    binding.imgIdBlack.setImageURI(selectedImageUri)
                    binding.deleteIdBack.isVisible = true
                    binding.editIdBack.isVisible = true
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