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
import com.stdio.mangoapp.databinding.FragmentProfileBinding
import com.stdio.mangoapp.domain.DataState
import com.stdio.mangoapp.domain.models.LoginResponse
import com.stdio.mangoapp.presentation.viewmodel.AuthViewModel
import com.stdio.mangoapp.presentation.viewmodel.ProfileViewModel
import me.phonemask.lib.PhoneNumberKit
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel by viewModel<ProfileViewModel>()
    private val binding by viewBinding(FragmentProfileBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_profileEditingFragment)
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.uiState.subscribeInUI(this, binding.progressBar) {

        }
    }
}