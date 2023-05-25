package com.android.watchify.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.android.watchify.R
import com.android.watchify.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseAuth.getInstance().currentUser?.let {
            binding.profileProgressBar.isVisible = true
            binding.displayName.text = it.displayName
            binding.profileEmail.text = it.email
            it.photoUrl?.let { url ->
                Glide.with(requireContext()).load(url).into(binding.profilePhoto)
                binding.profilePhoto.clipToOutline = true
            }
        }

        binding.logoutButton.setOnClickListener {
            AuthUI.getInstance().signOut(requireContext())
            relaunch()
        }

        binding.profileProgressBar.isVisible = false
    }

    private fun relaunch(){
        val packageName = requireContext().packageName
        val launchIntent = requireContext().packageManager.getLaunchIntentForPackage(packageName)

        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(launchIntent)
        }

        requireActivity().finish()
    }
}