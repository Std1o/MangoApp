package com.stdio.mangoapp.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.stdio.mangoapp.R
import com.stdio.mangoapp.common.showSnackbar
import com.stdio.mangoapp.common.subscribeInUI
import com.stdio.mangoapp.common.viewBinding
import com.stdio.mangoapp.databinding.FragmentAuthBinding
import com.stdio.mangoapp.presentation.viewmodel.AuthViewModel
import me.phonemask.lib.PhoneNumberKit
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val viewModel by viewModel<AuthViewModel>()
    private val binding by viewBinding(FragmentAuthBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val phoneNumberKit = PhoneNumberKit.Builder(requireContext())
                .setIconEnabled(true)
                .build()
            //PhoneNumberKit.ASSET_FILE_NAME = ""

            phoneNumberKit.attachToInput(phoneInputLayout, Locale.getDefault().language)
            phoneNumberKit.setupCountryPicker(requireActivity() as AppCompatActivity, searchEnabled = true)
            btnSend.setOnClickListener {
                viewModel.sendPhone(etPhone.text.toString())
            }
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.uiState.subscribeInUI(this, binding.progressBar) {
            showSnackbar(it.toString())
        }
    }
}