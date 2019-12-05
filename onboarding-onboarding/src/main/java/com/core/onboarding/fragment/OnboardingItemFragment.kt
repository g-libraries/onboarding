package com.core.onboarding.fragment

import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.core.onboarding.R
import com.ggroup.gbsfo.view.onboardinglib.AnimationDrawableHelper

class OnboardingItemFragment : Fragment() {
    companion object {
        const val EXTRA_GIF = "gifs"
        const val EXTRA_ANIMATION = "animations"
        const val EXTRA_ANIMATION_FRAMES = "frames"
        const val EXTRA_ANIMATION_INTERVAL = "interval" //ms
        const val EXTRA_TITLE = "titles"
        const val EXTRA_MESSAGE = "messages"

        fun newInstance(gif: String?, animation: String?, frames: Int = 0, interval: Int = 0, title: String, message: String): OnboardingItemFragment {
            val fragment =
                OnboardingItemFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_GIF, gif)
            bundle.putString(EXTRA_ANIMATION, animation)
            bundle.putInt(EXTRA_ANIMATION_FRAMES, frames)
            bundle.putInt(EXTRA_ANIMATION_INTERVAL, interval)
            bundle.putString(EXTRA_TITLE, title)
            bundle.putString(EXTRA_MESSAGE, message)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var animationView: ImageView? = null
    private var animation: AnimationDrawable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout = inflater.inflate(R.layout.fragment_onboarding_item, container, false)

        animationView = layout.findViewById(R.id.onboarding_animation)
        val title: TextView = layout.findViewById(R.id.onboarding_title)
        val message: TextView = layout.findViewById(R.id.onboarding_message)

        arguments?.let {
            val gif = it.getString(EXTRA_GIF)
            val anim = it.getString(EXTRA_ANIMATION)
            val frames = it.getInt(EXTRA_ANIMATION_FRAMES)
            val interval = it.getInt(EXTRA_ANIMATION_INTERVAL)
            if (gif != null)
                setGifImageView(animationView, gif)
            else if (anim != null)
                setImageView(animationView, anim, frames, interval)

            title.text = it.getString(EXTRA_TITLE)
            message.text = it.getString(EXTRA_MESSAGE)
        }
        return layout
    }

    private fun setGifImageView(imageView: ImageView?, s: String?) {
        Glide.with(this)
            .asGif()
            .load(s)
            .into(imageView!!)
    }

    private fun setImageView(
        imageView: ImageView?,
        s: String,
        frames: Int,
        interval: Int
    ) {
        val context = context ?: return
        animation =
            AnimationDrawableHelper.createAnimationDrawable(
                context,
                s,
                frames,
                interval
            )
        imageView?.let {
            it.background = animation
            (it.background as AnimationDrawable).start()
        }
    }

    override fun onDestroyView() {
        // recycle frames
        animation?.let {
            it.stop()
            for (i in 0 until it.numberOfFrames) {
                val frame = it.getFrame(i) as BitmapDrawable
                val bitmap = frame.bitmap
                bitmap.recycle()
                frame.callback = null
            }
            it.callback = null
            animation = null
        }
        super.onDestroyView()
    }
}