package xyz.mcomella.animationoverview.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_view_animation_set.*
import xyz.mcomella.animationoverview.R

/** Percentage of the banner animation that will pass before the text should start. */
private const val TEXT_START_PERCENT = 0.70

/**
 * A "View Animation" using the [AnimationSet] API to coordinate separate animations.
 *
 * Learnings:
 * - Must define the animation over five files (3x animation, 1 layout, 1 code) since you can't
 * define one animation file for multiple views coordinating. These files require context switching
 * and make it hard to know what the animation does without running it.
 * - Can't define animation values (duration, distance) in relation to one another: the animation
 * XML files can't reference values in other files (you could create two dimens, but they cant really
 * be defined in relation to one another). I ended up defining essential values (duration) in code,
 * making the XML useless by itself.
 * - ^ This makes starting multiple animations offset from one another difficult. You can only
 * define startOffsets in relation to now.
 * - Lots of trial and error to get the animation you want.
 *
 * Thoughts:
 * - I'm skeptical of the value of XML files for animations coordinating multiple views:
 *   - XML makes layouts declarative. However, XML animations can't be defined in relation to one
 *   another, making the declarative nature less useful (e.g. values still need to be defined in code).
 *   - XML encourages re-use, but if each animation XML is defined in relation to another, re-use is
 *   restricted.
 *   - You need to read multiple files in order to write one animation.
 *
 * Next: [ViewAnimationInCodeActivity].
 */
class ViewAnimationInXMLActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_animation_set)

        animateButton.setOnClickListener {
            it.isClickable = false // Prevent additional clicks.

            animateButton.animation = AnimationUtils.loadAnimation(this, R.anim.fade_down_button)

            banner.animation = AnimationUtils.loadAnimation(this, R.anim.fade_down_banner).apply {
                // Add parallax: XML doesn't make this easy.
                duration = animateButton.animation.duration * 2
            }
            banner.visibility = View.VISIBLE // Was hidden in layout.

            bannerText.animation = AnimationUtils.loadAnimation(this, R.anim.banner_text_fade_up).apply {
                startOffset = Math.round(banner.animation.duration * TEXT_START_PERCENT)
            }
            bannerText.visibility = View.VISIBLE // Was hidden in layout.

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
}