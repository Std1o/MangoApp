package com.stdio.mangoapp.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.stdio.mangoapp.R
import com.stdio.mangoapp.common.handlePhone
import com.stdio.mangoapp.common.showSnackbar
import com.stdio.mangoapp.common.subscribeInUI
import com.stdio.mangoapp.common.viewBinding
import com.stdio.mangoapp.databinding.FragmentAuthBinding
import com.stdio.mangoapp.domain.DataState
import com.stdio.mangoapp.domain.models.LoginResponse
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

            phoneNumberKit.attachToInput(phoneInputLayout, Locale.getDefault().language)
            phoneNumberKit.setupCountryPicker(requireActivity() as AppCompatActivity, searchEnabled = true)
            btnSend.setOnClickListener {
                if (viewModel.uiState.value !is DataState.Success){
                    viewModel.sendPhone(etPhone.text.toString().handlePhone())
                } else {
                    viewModel.checkAuthCode(etPhone.text.toString().handlePhone(), etCode.text.toString())
                }
            }
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.uiState.subscribeInUI(this, binding.progressBar) {
            binding.codeInputLayout.isVisible = true
            binding.btnSend.text = getString(R.string.sign_in)
            if (it is LoginResponse) {
                if (it.isUserExist == true) {
                    showSnackbar(it.accessToken)
                } else {
                    val action = AuthFragmentDirections
                        .actionAuthFragmentToRegistrationFragment(binding.etPhone.text.toString())
                    findNavController().navigate(action)
                }
            }
        }
    }
}