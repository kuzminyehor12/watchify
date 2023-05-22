package com.android.watchify.ui.fragments.auth

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.watchify.R
import com.android.watchify.utils.OnAuthStateChange
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class AuthFragment : Fragment() {
    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authorize()
    }

    private fun authorize() {
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.ic_default_news_pic)
            .setIsSmartLockEnabled(false)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) {
        this.onAuthResult(it)
    }

    private var authStateListener: OnAuthStateChange? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnAuthStateChange){
            authStateListener = context
        }
    }

    private fun onAuthResult(res: FirebaseAuthUIAuthenticationResult) {
        if (res.resultCode == AppCompatActivity.RESULT_OK) {
            FirebaseAuth.getInstance().currentUser?.also {
                authStateListener?.onAuthStateChanged()
            } ?: Toast
                .makeText(
                    requireContext(),
                    "Auth error",
                    Toast.LENGTH_SHORT)
                .show()
        } else {
            if (res.idpResponse == null) {
                requireActivity().finish()
            } else {
                Toast
                    .makeText(
                        requireContext(),
                        "Auth error",
                        Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}