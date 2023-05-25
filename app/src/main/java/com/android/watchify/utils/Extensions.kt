package com.android.watchify.utils

import android.content.Intent
import androidx.fragment.app.Fragment

fun Fragment.relaunch(){
    val packageName = requireContext().packageName
    val launchIntent = requireContext().packageManager.getLaunchIntentForPackage(packageName)

    if (launchIntent != null) {
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(launchIntent)
    }

    requireActivity().finish()
}