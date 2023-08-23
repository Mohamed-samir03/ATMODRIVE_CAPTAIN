package com.mosamir.atmodrivecaptain

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mosamir.atmodrivecaptain.databinding.FragmentCreateAccountVehicleInformationBinding

class CreateAccountVehicleInformation:Fragment() {

    private var _binding: FragmentCreateAccountVehicleInformationBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController
    private var imageType = ""
    private lateinit var sideImage :ImageView
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
        _binding = FragmentCreateAccountVehicleInformationBinding.inflate(inflater, container, false)

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_pick_car_images)
        val close = bottomSheetDialog.findViewById<ImageView>(R.id.close_bottom_sheet_car_images)
        val side1 = bottomSheetDialog.findViewById<ImageView>(R.id.img_car_side1)
        val side2 = bottomSheetDialog.findViewById<ImageView>(R.id.img_car_side2)
        val side3 = bottomSheetDialog.findViewById<ImageView>(R.id.img_car_side3)
        val side4 = bottomSheetDialog.findViewById<ImageView>(R.id.img_car_side4)
        val side5 = bottomSheetDialog.findViewById<ImageView>(R.id.img_car_side5)
        val side6 = bottomSheetDialog.findViewById<ImageView>(R.id.img_car_side6)
        val confirm = bottomSheetDialog.findViewById<Button>(R.id.btn_confirm_car_images)

        val sides = listOf(side1!!,side2!!,side3!!,side4!!,side5!!,side6!!)
        val carSides = listOf(binding.imgSide1,binding.imgSide2,binding.imgSide3,binding.imgSide4,binding.imgSide5,binding.imgSide6)

        sides.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                imageType = imageView.id.toString()
                sideImage = imageView
                openGallery()
            }
        }

        confirm?.setOnClickListener {
            var i = 0
            for (img in carSides){
                img.setImageBitmap((sides[i++].drawable as? BitmapDrawable)?.bitmap )
            }
            bottomSheetDialog.dismiss()
            binding.editCarSidesImages.isVisible = true
        }

        close?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.layoutCarImages.setOnClickListener {
            bottomSheetDialog.show()
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
            binding.imgLicenseFront.setImageURI(null)
            binding.deleteLicenseFront.isVisible = false
            binding.editLicenseFront.isVisible = false
        }

        binding.deleteLicenseBack.setOnClickListener {
            binding.imgLicenseBack.setImageURI(null)
            binding.deleteLicenseBack.isVisible = false
            binding.editLicenseBack.isVisible = false
        }

        binding.editLicenseFront.setOnClickListener {
            imageType = "LicenseFront"
            openGallery()
        }

        binding.editLicenseBack.setOnClickListener {
            imageType = "LicenseBack"
            openGallery()
        }

        binding.btnCAVehicleInformationNext.setOnClickListener {
            val action =
                CreateAccountVehicleInformationDirections.actionCreateAccountVehicleInformationToCreateAccountBankAccount()
            mNavController.navigate(action)
        }

        binding.CAVecicleInformationGoBack.setOnClickListener {
            val action =
                CreateAccountVehicleInformationDirections.actionCreateAccountVehicleInformationToCreateAccountPersonalInformation()
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
                "LicenseFront" -> {
                    binding.imgLicenseFront.setImageURI(selectedImageUri)
                    binding.deleteLicenseFront.isVisible = true
                    binding.editLicenseFront.isVisible = true
                }
                "LicenseBack" -> {
                    binding.imgLicenseBack.setImageURI(selectedImageUri)
                    binding.deleteLicenseBack.isVisible = true
                    binding.editLicenseBack.isVisible = true
                }
                else -> {
                    sideImage.setImageURI(selectedImageUri)
                }
            }
        }else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
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