package com.core.onboarding.util

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable

class AnimationDrawableHelper {
    companion object {
        fun createAnimationDrawable(
            context: Context,
            animName: String,
            frames: Int,
            duration: Int
        ): AnimationDrawable {
            val animation = AnimationDrawable()
            animation.isOneShot = false
            for (i in 0 until frames) {
                val bitmapStringId = animName + "_" + i.toString()
                val resID =
                    context.resources.getIdentifier(bitmapStringId, "drawable", context.packageName)
                val bitmap = BitmapFactory.decodeResource(context.resources, resID)
                val frame = BitmapDrawable(context.resources, bitmap)
                animation.addFrame(frame, duration)
            }
            return animation
        }
    }
}