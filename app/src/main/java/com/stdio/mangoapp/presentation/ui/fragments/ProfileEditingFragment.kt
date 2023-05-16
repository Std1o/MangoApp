package com.stdio.mangoapp.presentation.ui.fragments

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.stdio.mangoapp.R
import com.stdio.mangoapp.common.subscribeInUI
import com.stdio.mangoapp.common.viewBinding
import com.stdio.mangoapp.databinding.FragmentProfileEditingBinding
import com.stdio.mangoapp.presentation.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.IOException


class ProfileEditingFragment : Fragment(R.layout.fragment_profile_editing) {

    private val viewModel by viewModel<ProfileViewModel>()
    private val binding by viewBinding(FragmentProfileEditingBinding::bind)

    val takePicture = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        println(getBase64FromUri(uri!!))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        binding.ivUser.setOnClickListener {
            takePicture.launch("image/*")
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