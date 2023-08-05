package com.mosamir.atmodrivecaptain

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.mosamir.atmodrivecaptain.databinding.FragmentCreateAccountPersonalInformationBinding

class CreateAccountPersonalInformation:Fragment() {

    private var _binding: FragmentCreateAccountPersonalInformationBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController
    private var imageType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNavController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateAccountPersonalInformationBinding.inflate(inflater, container, false)

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

        binding.btnStep3Next.setOnClickListener {
            val action = CreateAccountPersonalInformationDirections.actionCreateAccountPersonalInformationToCreateAccountVehicleInformation()
            mNavController.navigate(action)
        }

        binding.CAPersonalInformationGoBack.setOnClickListener {
            val action = CreateAccountPersonalInformationDirections.actionCreateAccountPersonalInformationToCreateAccountVerification2()
            mNavController.navigate(action)
        }


        return binding.root
    }

    private fun openGallery(){
        ImagePicker.with(this)
            .crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
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