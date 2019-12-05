package com.core.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.core.onboarding.R
import kotlinx.android.synthetic.main.fragment_onboarding.view.*

class OnboardingFragment(
    private val gifs: ArrayList<String>? = null,
    private val anims: ArrayList<String>? = null,
    private val frames: Int = 0,
    private val interval: Int = 0,
    private val titles: ArrayList<String>,
    private val messages: ArrayList<String>,
    private var nextClick: () -> Unit,
    private var closeClick: () -> Unit
) : Fragment() {

    private val pages: Int = titles.size

//    companion object {
//        const val EXTRA_GIFS = "gifs"
//        const val EXTRA_ANIMATIONS = "animations"
//        const val EXTRA_ANIMATION_FRAMES = "frames"
//        const val EXTRA_ANIMATION_INTERVAL = "interval" //ms
//        const val EXTRA_TITLES = "titles"
//        const val EXTRA_MESSAGES = "messages"
//        fun newInstance(
//            gifs: ArrayList<String>?,
//            animations: ArrayList<String>?,
//            frames: Int = 0,
//            interval: Int = 0,
//            titles: ArrayList<String>,
//            messages: ArrayList<String>
//        ): OnboardingItemFragment {
//            val fragment = OnboardingItemFragment()
//            val bundle = Bundle()
//            bundle.putStringArrayList(EXTRA_GIFS, gifs)
//            bundle.putStringArrayList(EXTRA_ANIMATIONS, animations)
//            bundle.putInt(EXTRA_ANIMATION_FRAMES, frames)
//            bundle.putInt(EXTRA_ANIMATION_INTERVAL, interval)
//            bundle.putStringArrayList(EXTRA_TITLES, titles)
//            bundle.putStringArrayList(EXTRA_MESSAGES, messages)
//            fragment.arguments = bundle
//            return fragment
//        }
//    }

    private lateinit var tutorialPager: ViewPager

    private lateinit var skipButton: TextView
    private lateinit var nextButton: ImageView

    private var currentPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding, container, false)

        tutorialPager = view.fragment_onboarding_vp

        skipButton = view.fragment_onboarding_btn_skip
        nextButton = view.fragment_onboarding_btn_next

//        arguments?.let {
//            val gifs = it.getStringArrayList(EXTRA_GIFS)
//            val anims = it.getStringArrayList(EXTRA_ANIMATIONS)
//            val frames = it.getInt(EXTRA_ANIMATION_FRAMES)
//            val interval = it.getInt(EXTRA_ANIMATION_INTERVAL)
//            val titles: ArrayList<String> = it.getStringArrayList(EXTRA_TITLES) ?: arrayListOf()
//            val messages: ArrayList<String> = it.getStringArrayList(EXTRA_MESSAGES) ?: arrayListOf()

        setPages(
            OnboardingPagerAdapter(
                childFragmentManager,
                gifs,
                anims,
                frames,
                interval,
                makePairsList(titles, messages)
            )
        )

        val indicator = view.fragment_onboarding_ci_dots
        indicator.setViewPager(tutorialPager)

        nextButton.setOnClickListener {
            if (currentPosition < pages - 1) {
                tutorialPager.currentItem = currentPosition + 1
                nextClick.invoke()
            } else {
                closeClick.invoke()
//                activity?.supportFragmentManager?.popBackStack() ?: onDestroyView()
//                activity!!.supportFragmentManager.popBackStack()
            }
        }

        skipButton.setOnClickListener {
            // No need to show tutorial anymore, mark as shown
            closeClick.invoke()
//            activity?.supportFragmentManager?.popBackStack() ?: onDestroyView()
//            activity!!.supportFragmentManager.popBackStack()
        }
        return view
    }

    private fun makePairsList(
        titles: ArrayList<String>,
        messages: ArrayList<String>
    ): ArrayList<Pair<String, String>> {
        val list = ArrayList<Pair<String, String>>()
        for (i in 0 until pages)
            list.add(Pair(titles[i], messages[i]))
        return list
    }

    private fun setPages(viewPagerAdapter: PagerAdapter) {
        val initialPosition = 0

        tutorialPager.adapter = viewPagerAdapter
        tutorialPager.currentItem = initialPosition

        tutorialPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                changePage(position)
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
            }
        })
    }

    private fun changePage(position: Int) {
        if (position == 0) nextButton.isActivated = false
        else nextButton.isActivated = position < pages
    }
}