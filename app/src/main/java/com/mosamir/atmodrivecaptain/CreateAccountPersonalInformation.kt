package com.mosamir.atmodrivecaptain

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mosamir.atmodrivecaptain.databinding.FragmentCreateAccountPersonalInformationBinding

class CreateAccountPersonalInformation:Fragment() {

    private var _binding: FragmentCreateAccountPersonalInformationBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController
    private val PICK_IMAGE_REQUEST = 2
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

        return binding.root
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            if (imageType == "PersonalPhoto"){
                binding.imgPersonalPhoto.setImageURI(selectedImageUri)
                binding.deletePersonalPhoto.isVisible = true
                binding.editPersonalPhoto.isVisible = true
            }else if (imageType == "IdFront"){
                binding.imgIdFront.setImageURI(selectedImageUri)
                binding.deleteIdFront.isVisible = true
                binding.editIdFront.isVisible = true
            }else if (imageType == "IdBack"){
                binding.imgIdBlack.setImageURI(selectedImageUri)
                binding.deleteIdBack.isVisible = true
                binding.editIdBack.isVisible = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}