package xyz.mcomella.animationoverview.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import kotlinx.android.synthetic.main.activity_view_animation_set.*
import xyz.mcomella.animationoverview.R

private const val BUTTON_FADE_OUT_MILLIS = 600L
private const val TEXT_FADE_IN_MILLIS = 800L

/** Percentage of the banner animation that will pass before the text should start. */
private const val TEXT_START_PERCENT = 0.70

/**
 * A "View Animation" defined in code, to try to improve upon using 5 files for one
 * animation from [ViewAnimationInXMLActivity].
 *
 * Learnings:
 * - Allows you to naturally define duration, startOffset in relation to one another.
 * - Defining *Animation in code can be verbose, but avoids having to move between three
 * files to change values; can attempt to fix with custom APIs.
 * - ^ Keeping in one file may make it marginally easier to infer what an animation does before
 * running it, but declaring values in relation to one another is awkward.
 * - The way I separated *Animation from values defined in relation to one another (duration) is
 * actually similar to how I used XML and [AnimationUtils.loadAnimation].
 *
 * Next: property animation!
 */
class ViewAnimationInCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_animation_set)

        animateButton.setOnClickListener {
            it.isClickable = false // Prevent additional clicks.

            animateButton.animation = getAnimateButtonFadeOut()
            banner.animation = getBannerFadeIn()
            bannerText.animation = getBannerTextFadeIn()

            animateButton.animation.duration = BUTTON_FADE_OUT_MILLIS
            banner.animation.duration = animateButton.animation.duration * 2 // parallax.
            bannerText.animation.duration = TEXT_FADE_IN_MILLIS
            bannerText.animation.startOffset = Math.round(banner.animation.duration * TEXT_START_PERCENT)

            // Unhide views hidden in layout.
            banner.visibility = View.VISIBLE
            bannerText.visibility = View.VISIBLE

            val animationSet = AnimationSet(true).apply {
                interpolator = AccelerateDecelerateInterpolator()
                addAnimation(animateButton.animation)
                addAnimation(banner.animation)
                addAnimation(bannerText.animation)
            }

            parentView.invalidate() // Required to use `View.animation = ...`.
            animationSet.startNow()

            animationSet.setAnimationListener(object : AnimationAdapter() {
                override fun onAnimationEnd(animation: Animation?) {
                    animateButton.visibility = View.GONE
                }
            })
        }
    }

    private fun getAnimateButtonFadeOut() = AnimationSet(true).apply {
        // Interpolator, parent set outside.
        fillAfter = true

        addAnimation(AlphaAnimation(1f, 0f))
        addAnimation(TranslateAnimation(
                /* fromX */ Animation.RELATIVE_TO_SELF, 0f,
                /* toX */ Animation.RELATIVE_TO_SELF, 0f,
                /* fromY */ Animation.RELATIVE_TO_SELF, 0f,
                /* toY */ Animation.RELATIVE_TO_PARENT, 0.1f
        ))
    }

    private fun getBannerFadeIn() = AnimationSet(true).apply {
        // Interpolator, parent set outside.
        fillBefore = true

        addAnimation(AlphaAnimation(0f, 1f))
        addAnimation(TranslateAnimation(
                /* fromX */ Animation.RELATIVE_TO_SELF, 0f,
                /* toX */ Animation.RELATIVE_TO_SELF, 0f,
                /* fromY */ Animation.RELATIVE_TO_SELF, -1f,
                /* toY */ Animation.RELATIVE_TO_SELF, 0f
        ))
    }

    private fun getBannerTextFadeIn() = AnimationSet(true).apply {
        // Interpolator, parent set outside.
        fillBefore = true

        addAnimation(AlphaAnimation(0f, 1f))
        addAnimation(TranslateAnimation(
                /* fromX */ Animation.RELATIVE_TO_SELF, 0f,
                /* toX */ Animation.RELATIVE_TO_SELF, 0f,
                /* fromY */ Animation.RELATIVE_TO_SELF, 0.5f,
                /* toY */ Animation.RELATIVE_TO_SELF, 0f
        ))
    }
}