package com.android.watchify.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.android.watchify.R
import com.android.watchify.databinding.ActivityMainBinding
import com.android.watchify.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_splash)

        CoroutineScope(Dispatchers.Main).launch {
            delay(Constants.MAIN_DELAY)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            binding.bottomNavigation.setupWithNavController(binding.navigationFragment.findNavController())
        }
    }
}