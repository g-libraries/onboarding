package com.ggroup.gbsfo.view.onboardinglib

import android.widget.FrameLayout
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.core.onboarding.fragment.OnboardingFragment

/**
 * Base Onboarding implementation.
 * params - Default params, can modify
 * active - Onboarding active status
 * rootActivity - Root activity
 * rootView - Onboarding container
 */
abstract class OnboardingImpl(
    val params: Params,
    var rootViewId: Int,
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
                },
                closeClick = {
                    onboardingViewed()
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
                rootViewId,
                onboardingFragment
            )
            .commit()
    }
}