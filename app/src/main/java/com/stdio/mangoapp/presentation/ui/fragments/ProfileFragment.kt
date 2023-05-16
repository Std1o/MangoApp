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
import com.stdio.mangoapp.domain.models.ProfileData
import com.stdio.mangoapp.domain.models.ProfileDataResponse
import com.stdio.mangoapp.presentation.viewmodel.AuthViewModel
import com.stdio.mangoapp.presentation.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.first
import me.phonemask.lib.PhoneNumberKit
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel by viewModel<ProfileViewModel>()
    private val binding by viewBinding(FragmentProfileBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnEdit.setOnClickListener {
            if (viewModel.uiState.value is DataState.Success) {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToProfileEditingFragment(
                        (viewModel.uiState.value as DataState.Success<ProfileDataResponse>).data.profileData
                    )
                findNavController().navigate(action)
            }
        }
        subscribeObservers()
    }

    private fun setUserDataToViews(profileData: ProfileData) {
        with(binding) {
            with(profileData) {
                tvName.text = getString(R.string.name_, name)
                tvUsername.text = getString(R.string.username_, username)
                tvBirthday.text = getString(R.string.birthday_, birthday)
                tvCity.text = getString(R.string.city_, city)
                tvVk.text = getString(R.string.vk_, vk)
                tvInstagram.text = getString(R.string.instagram_, instagram)
                tvStatus.text = getString(R.string.status_, status)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCurrentUser()
    }

    private fun subscribeObservers() {
        viewModel.uiState.subscribeInUI(this, binding.progressBar) {
            setUserDataToViews(it.profileData)
        }
    }
}