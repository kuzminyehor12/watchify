package com.android.watchify.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.android.watchify.R
import com.android.watchify.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val DELAY: Long = 5000
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.fragment_splash)
        Looper.myLooper()?.let { Handler(it).postDelayed({
            setContentView(binding.root)
            binding.bottomNavigation.setupWithNavController(binding.navigationFragment.findNavController())
        }, DELAY) }
    }
}