package xyz.mcomella.animationoverview.property

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import kotlinx.android.synthetic.main.activity_view_animation_set.*
import xyz.mcomella.animationoverview.R

private const val BUTTON_FADE_OUT_MILLIS = 600L
private const val TEXT_FADE_IN_MILLIS = 800L

/** Percentage of the banner animation that will pass before the text should start. */
private const val TEXT_START_PERCENT = 0.70

/**
 * A "Property Animation" with [ObjectAnimator].
 *
 * Learnings:
 * - AnimatorSet has intuitive combination operators (play().with().before()...).
 * - *Slightly* less verbose than [ViewAnimationInCodeActivity], certainly fewer gotchas (like
 * invalidating the parent view).
 * - It's error-prone to declare properties in plain-text (e.g. alpha).
 * - Easy to forget to register things to animate (i.e. have to say Set.play) and any animations
 * that play together always need to be registered.
 *
 * Questions:
 * - More efficient than running multiple ViewPropertyAnimators? AnimatorSet chaining could be
 * useful, however.
 */
class ObjectAnimatorInCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_animation_set)

        animateButton.setOnClickListener {
            it.isClickable = false // Prevent additional clicks.

            // Show views hidden in layout.
            banner.visibility = View.VISIBLE
            bannerText.visibility = View.VISIBLE
            bannerText.alpha = 0f // We have a starting delay so we can't rely on the animator to set us up.

            AnimatorSet().apply {
                interpolator = AccelerateDecelerateInterpolator()

                playTogether(getAnimateButtonFadeOut(), getBannerBackgroundFadeIn())
                play(getBannerTextFadeIn()).after(Math.round(BUTTON_FADE_OUT_MILLIS * 2 * TEXT_START_PERCENT))

                addListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        animateButton.visibility = View.GONE
                    }
                })
            }.start()
        }
    }

    private fun getAnimateButtonFadeOut() = AnimatorSet().apply {
        val alpha = ObjectAnimator.ofFloat(animateButton, "alpha", 0f)
        val translation = ObjectAnimator.ofFloat(animateButton, "translationY",
                parentView.height * 0.1f)

        duration = BUTTON_FADE_OUT_MILLIS
        playTogether(alpha, translation)
    }

    private fun getBannerBackgroundFadeIn() = AnimatorSet().apply {
        val alpha = ObjectAnimator.ofFloat(banner, "alpha", 0f, 1f)
        val translation = ObjectAnimator.ofFloat(banner, "translationY",
                -banner.height.toFloat(), 0f)

        duration = BUTTON_FADE_OUT_MILLIS * 2 // Parallax with button fade out.
        playTogether(alpha, translation)
    }

    private fun getBannerTextFadeIn() = AnimatorSet().apply {
        val alpha = ObjectAnimator.ofFloat(bannerText, "alpha", 1f)
        val translation = ObjectAnimator.ofFloat(bannerText, "translationY",
                bannerText.height * 0.5f, 0f)

        duration = TEXT_FADE_IN_MILLIS
        playTogether(alpha, translation)
    }
}
