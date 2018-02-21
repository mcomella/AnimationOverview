package xyz.mcomella.animationoverview.property

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
 * A "Property Animation" with [ViewPropertyAnimator].
 *
 * Learnings:
 * - Easier to write than View Animation (but maybe writing animations is just getting easier).
 * - Easier to read than View Animations.
 * - The AnimationSet abstractions seem like extra overhead from code reading perspective (though
 * it can make it easy to start a view at once with only a single reference).
 *
 * Questions:
 * - Maybe AnimationSet is more efficient on device resources? e.g. it has one update message
 * to schedule instead of one for each view?
 */
class ViewPropertyAnimatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_animation_set)

        animateButton.setOnClickListener {
            it.isClickable = false // Prevent additional clicks.

            val interpolator = AccelerateDecelerateInterpolator()

            animateButton.animate()
                    .setDuration(BUTTON_FADE_OUT_MILLIS)
                    .setInterpolator(interpolator)
                    .alpha(0f)
                    .translationYBy(parentView.height * 0.1f)
                    .start()

            banner.visibility = View.VISIBLE // Hidden in layout.
            banner.alpha = 0f
            banner.translationY = -banner.height.toFloat()
            banner.animate()
                    .setDuration(BUTTON_FADE_OUT_MILLIS * 2)
                    .setInterpolator(interpolator)
                    .alpha(1f)
                    .translationY(0f)
                    .start()

            bannerText.visibility = View.VISIBLE // Hidden in layout.
            bannerText.alpha = 0f
            bannerText.translationY = bannerText.height * 0.5f
            bannerText.animate()
                    .setDuration(TEXT_FADE_IN_MILLIS)
                    .setInterpolator(interpolator)
                    .alpha(1f)
                    .translationY(0f)
                    .setStartDelay(Math.round(BUTTON_FADE_OUT_MILLIS * 2 * TEXT_START_PERCENT))
        }
    }
}
