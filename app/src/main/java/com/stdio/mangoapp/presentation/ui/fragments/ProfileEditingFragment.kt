package com.stdio.mangoapp.presentation.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.stdio.mangoapp.R
import com.stdio.mangoapp.common.formatToString
import com.stdio.mangoapp.common.subscribeInUI
import com.stdio.mangoapp.common.viewBinding
import com.stdio.mangoapp.databinding.FragmentProfileEditingBinding
import com.stdio.mangoapp.presentation.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.util.Date


class ProfileEditingFragment : Fragment(R.layout.fragment_profile_editing) {

    private val viewModel by viewModel<ProfileViewModel>()
    private val binding by viewBinding(FragmentProfileEditingBinding::bind)

    private val takePicture = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        println(getBase64FromUri(uri!!))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        with(binding) {
            ivUser.setOnClickListener {
                takePicture.launch("image/*")
            }
            birthdayInputLayout.editText?.keyListener = null
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату")
                .build()
            val onClickListener = View.OnClickListener {
                datePicker.show(parentFragmentManager, "datePicker")
            }
            birthdayInputLayout.setOnClickListener(onClickListener)
            birthdayInputLayout.editText?.setOnClickListener(onClickListener)
            datePicker.addOnPositiveButtonClickListener {
                datePicker.selection?.let { date ->
                    val dateStr = Date(date).formatToString("yyyy-MM-dd")
                    birthdayInputLayout.editText?.setText(dateStr)
                }
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.uiState.subscribeInUI(this, binding.progressBar) {

        }
    }

    private fun getBase64FromUri(uri: Uri): String {
        try {
            val bytes = requireActivity().contentResolver.openInputStream(uri)?.readBytes()
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (error: IOException) {
            error.printStackTrace() // This exception always occurs
        }
        return ""
    }
}