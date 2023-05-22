package com.android.watchify.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.android.watchify.R
import com.android.watchify.databinding.ActivityMainBinding
import com.android.watchify.ui.fragments.auth.AuthFragment
import com.android.watchify.utils.Constants
import com.android.watchify.utils.OnAuthStateChange
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnAuthStateChange {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_splash)
        showFragmentFromAuthResult()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0){
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun showFragmentFromAuthResult(){
        if (FirebaseAuth.getInstance().currentUser == null){
            CoroutineScope(Dispatchers.Main).launch {
                delay(Constants.MAIN_DELAY)
                binding = ActivityMainBinding.inflate(layoutInflater)
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.navigationFragment, AuthFragment())
                    .commit()
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                delay(Constants.MAIN_DELAY)
                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)
                binding.bottomNavigation.setupWithNavController(binding.navigationFragment.findNavController())
            }
        }
    }

    override fun onAuthStateChanged() {
        showFragmentFromAuthResult()
    }
}