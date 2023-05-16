package com.stdio.mangoapp.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.stdio.mangoapp.R
import com.stdio.mangoapp.common.Constants.DATE_FORMAT
import com.stdio.mangoapp.common.launchWhenStartedCollect
import com.stdio.mangoapp.common.showSnackbar
import com.stdio.mangoapp.common.subscribeInUI
import com.stdio.mangoapp.common.toDate
import com.stdio.mangoapp.common.viewBinding
import com.stdio.mangoapp.databinding.FragmentProfileBinding
import com.stdio.mangoapp.domain.DataState
import com.stdio.mangoapp.domain.mappers.DateToZodiacSignMapper
import com.stdio.mangoapp.domain.models.ProfileData
import com.stdio.mangoapp.presentation.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.takeWhile
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel by viewModel<ProfileViewModel>()
    private val binding by viewBinding(FragmentProfileBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
    }

    private fun setUserDataToViews(profileData: ProfileData) {
        with(binding) {
            with(profileData) {
                Glide
                    .with(requireContext())
                    .load(avatar)
                    .placeholder(R.drawable.avatar_placeholder)
                    .into(ivUser)
                tvName.text = getString(R.string.name_, name)
                tvUsername.text = getString(R.string.username_, username)
                var zodiac = ""
                profileData.birthday?.let {
                    val date = it.toDate(DATE_FORMAT)
                    if (date != null) {
                        zodiac = DateToZodiacSignMapper().map(date)
                        zodiac = "($zodiac)"
                    }
                }
                tvBirthday.text = getString(R.string.birthday_, "$birthday $zodiac")
                tvCity.text = getString(R.string.city_, city)
                tvVk.text = getString(R.string.vk_, vk)
                tvInstagram.text = getString(R.string.instagram_, instagram)
                tvStatus.text = getString(R.string.status_, status)
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.profileData.takeWhile { isAdded }.onEach {
            it?.let {
                setUserDataToViews(it)
                binding.btnEdit.setOnClickListener { view->
                    val action = ProfileFragmentDirections.actionProfileFragmentToProfileEditingFragment(it)
                    findNavController().navigate(action)
                }
            }
        }.launchWhenStartedCollect(lifecycleScope)
        viewModel.uiState.takeWhile { isAdded }.onEach {
            binding.progressBar.isVisible = it is DataState.Loading
            if (it is DataState.Success) {
                binding.mainContent.isVisible = true
            } else if (it is DataState.Error) {
                showSnackbar(it.exception)
            }
        }.launchWhenStartedCollect(lifecycleScope)
    }
}