package com.android.watchify.ui.activities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.watchify.ui.adapters.FavouritesAdapter
import com.android.watchify.ui.fragments.favourites.FavouritesFragment
import com.android.watchify.ui.fragments.main.MainFragment
import com.android.watchify.ui.fragments.profile.ProfileFragment
import com.android.watchify.ui.fragments.search.SearchFragment

class WatchifyViewPagerAdapter(fragmentActivity: FragmentActivity)
    : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        MainFragment(),
        SearchFragment(),
        FavouritesFragment(),
        ProfileFragment()
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}