package com.mosamir.atmodrivecaptain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mosamir.atmodrivecaptain.databinding.FragmentCreateAccountVehicleInformationBinding

class CreateAccountVehicleInformation:Fragment() {

    private var _binding: FragmentCreateAccountVehicleInformationBinding? = null
    private val binding get() = _binding!!
    private lateinit var mNavController: NavController
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

        binding.btnCAVehicleInformationNext.setOnClickListener {
            val action = CreateAccountVehicleInformationDirections.actionCreateAccountVehicleInformationToCreateAccountBankAccount()
            mNavController.navigate(action)
        }

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_pick_car_images)
        val close = bottomSheetDialog.findViewById<ImageView>(R.id.close_bottom_sheet_car_images)

        close?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.layoutCarImages.setOnClickListener {
            bottomSheetDialog.show()
        }


        return binding.root
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