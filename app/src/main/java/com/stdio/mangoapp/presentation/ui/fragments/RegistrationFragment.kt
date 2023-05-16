package com.stdio.mangoapp.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.stdio.mangoapp.R
import com.stdio.mangoapp.common.isNotEmpty
import com.stdio.mangoapp.common.isValidUsername
import com.stdio.mangoapp.common.showSnackbar
import com.stdio.mangoapp.common.subscribeInUI
import com.stdio.mangoapp.common.viewBinding
import com.stdio.mangoapp.databinding.FragmentRegistrationBinding
import com.stdio.mangoapp.presentation.viewmodel.RegistrationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private val viewModel by viewModel<RegistrationViewModel>()
    private val binding by viewBinding(FragmentRegistrationBinding::bind)
    private val args: RegistrationFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            etPhone.setText(args.phone)
            btnSend.setOnClickListener {
                if (nameInputLayout.isNotEmpty() && usernameInputLayout.isValidUsername()) {
                    viewModel.register(etPhone.text.toString(), etName.text.toString(), etUsername.text.toString())
                }
            }
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.uiState.subscribeInUI(this, binding.progressBar) {
            showSnackbar(it.accessToken)
        }
    }
}