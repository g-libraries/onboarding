package com.core.onboarding.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.ArrayList

class OnboardingPagerAdapter(fm: FragmentManager,
                             private val gifs: ArrayList<String>?,
                             private val anims: ArrayList<String>?,
                             private val frames: Int = 0,
                             private val interval: Int = 0,
                             val text: ArrayList<Pair<String, String>>) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return OnboardingItemFragment.newInstance(
            gifs?.get(position),
            anims?.get(position),
            frames,
            interval,
            text[position].first,
            text[position].second
        )
    }

    override fun getCount(): Int {
        return text.size
    }
}