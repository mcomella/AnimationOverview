package xyz.mcomella.animationoverview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*

/** Percentage of the banner animation that will pass before the text should start. */
private const val TEXT_START_PERCENT = 0.70

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        - Sucks to coordinate animations from 3 files.
        - Can't define translation distance in relation to one another.
        - Lots of trial/error
        - alternative: use animation enders. TODO: write it!
         */
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

private open class AnimationAdapter : Animation.AnimationListener {
    override fun onAnimationRepeat(animation: Animation?) {}
    override fun onAnimationEnd(animation: Animation?) {}
    override fun onAnimationStart(animation: Animation?) {}
}
