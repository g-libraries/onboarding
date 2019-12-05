package com.ggroup.gbsfo.view.onboardinglib

import android.widget.FrameLayout
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.core.onboarding.fragment.OnboardingFragment
import com.ggroup.gbsfo.R

/**
 * Base Onboarding implementation.
 * params - Default params, can modify
 * active - Onboarding active status
 * rootActivity - Root activity
 * rootView - Onboarding container
 */
abstract class OnboardingImpl(
    val params: Params,
    var active: Boolean,
    private val rootActivity: AppCompatActivity,
    private val rootView: FrameLayout
) : IOnboarding {
    data class Params(
        val gifs: ArrayList<String>? = null,
        val anims: ArrayList<String>? = null,
        val frames: Int = 0,
        val interval: Int = 0,
        val titles: ArrayList<String>,
        val messages: ArrayList<String>
//        ,
//        var nextClick: () -> Unit,
//        var closeClick: () -> Unit
    )

    // Must be inside attach() method to listen for Fragments lifecycle events, see implementation for reference
    abstract fun attachNavigationCallbacks(fragmentManager: FragmentManager)

    // Override to control Onboarding logic
    abstract fun showOnboarding()
    abstract fun onboardingViewed()

    lateinit var onboardingFragment: Fragment

    protected fun showOnboardingView() {
        rootView.visibility = View.VISIBLE
    }

    protected fun hideOnboardingView() {
        rootView.visibility = View.GONE
    }

    /**
     * show Onboarding
     */
    fun showOnboardingFragment(
    ) {
        onboardingFragment =
            OnboardingFragment(
                params.gifs,
                params.anims,
                params.frames,
                params.interval,
                params.titles,
                params.messages,
                nextClick = {
                    onboardingViewed()
//                mainActivityViewModel.onboardingViewed()
                },
                closeClick = {
                    onboardingViewed()
//                mainActivityViewModel.onboardingViewed()
                    rootActivity.supportFragmentManager
                        .beginTransaction()
                        .remove(onboardingFragment)
                        .commit()
                    hideOnboardingView()
                }
            )

        rootActivity.supportFragmentManager
            .beginTransaction()
            .add(
                R.id.activity_main_top_container,
                onboardingFragment
            )
            .commit()
    }
}