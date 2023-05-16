package com.stdio.mangoapp.presentation.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.stdio.mangoapp.R
import com.stdio.mangoapp.common.formatToString
import com.stdio.mangoapp.common.showSnackbar
import com.stdio.mangoapp.common.subscribeInUI
import com.stdio.mangoapp.common.viewBinding
import com.stdio.mangoapp.databinding.FragmentProfileEditingBinding
import com.stdio.mangoapp.domain.models.AvatarUploadingRequest
import com.stdio.mangoapp.presentation.viewmodel.ProfileEditingViewModel
import com.stdio.mangoapp.presentation.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.util.Date
import java.util.UUID


class ProfileEditingFragment : Fragment(R.layout.fragment_profile_editing) {

    private val viewModel by viewModel<ProfileEditingViewModel>()
    private val binding by viewBinding(FragmentProfileEditingBinding::bind)
    private val args: ProfileEditingFragmentArgs by navArgs()

    private val takePicture = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        viewModel.userData.avatar = AvatarUploadingRequest(UUID.randomUUID().toString(), getBase64FromUri(uri!!))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        setUserDataToViews()
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
                    viewModel.userData.birthday = dateStr
                }
            }
            btnSave.setOnClickListener {
                with(viewModel) {
                    userData.username = args.profileData.username
                    userData.name = etName.text.toString()
                    userData.city = etCity.text.toString()
                    userData.vk = etVk.text.toString()
                    userData.instagram = etInstagram.text.toString()
                    userData.status = etStatus.text.toString()
                    updateUser()
                }
            }
        }
    }

    private fun setUserDataToViews() {
        with(binding) {
            with(args.profileData) {
                etName.setText(name)
                etBirthday.setText(birthday)
                etCity.setText(city)
                etVk.setText(vk)
                etInstagram.setText(instagram)
                etStatus.setText(status)
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.uiState.subscribeInUI(this, binding.progressBar) {
            showSnackbar(R.string.saved_successfully)
            findNavController().popBackStack()
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