package com.android.watchify.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.android.watchify.R
import com.android.watchify.databinding.ActivityMainBinding
import com.android.watchify.ui.fragments.main.MainFragment
import com.android.watchify.utils.Constants
import com.android.watchify.utils.OnAuthStateChange
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnAuthStateChange {
    private lateinit var binding: ActivityMainBinding
    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        authStateListener = this

        setContentView(R.layout.fragment_splash)
        showFragmentFromAuthResult()
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
    private fun onAuthResult(res: FirebaseAuthUIAuthenticationResult) {
        if (res.resultCode == RESULT_OK) {
            FirebaseAuth.getInstance().currentUser?.also {
                authStateListener?.onAuthStateChanged()
            } ?: Toast
                .makeText(
                    this,
                    "Auth error",
                    Toast.LENGTH_SHORT)
                .show()
        } else {
            if (res.idpResponse == null) {
                finish()
            } else {
                Toast
                    .makeText(
                        this,
                        "Network error",
                        Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0){
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun showFragmentFromAuthResult(){
        CoroutineScope(Dispatchers.Main).launch {
            delay(Constants.MAIN_DELAY)
            if (FirebaseAuth.getInstance().currentUser == null){
                authorize()
            } else {
                setContentView(binding.root)
                binding.bottomNavigation.setupWithNavController(binding.navigationFragment.findNavController())
            }
        }
    }

    override fun onAuthStateChanged() {
        showFragmentFromAuthResult()
    }
}