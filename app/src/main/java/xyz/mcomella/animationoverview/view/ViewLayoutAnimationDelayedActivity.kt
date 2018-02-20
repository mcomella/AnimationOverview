package xyz.mcomella.animationoverview.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_view_layout_animation.*
import xyz.mcomella.animationoverview.R

/**
 * A View Animation that improves on [ViewLayoutAnimationActivity]. Older devices will lag if a
 * LayoutAnimation is used on the initial layout: we can fix this by delaying the initial layout.
 */
class ViewLayoutAnimationDelayedActivity : AppCompatActivity() {

    private val textSize by lazy { (fadeContainer.getChildAt(1) as TextView).textSize }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_layout_animation_delayed)

        startAnimButton.setOnClickListener {
            listOf(fadeContainer, translateContainer).forEach { container ->
                for (i in 0..1) {
                    val fadeChild = getChild()
                    container.addView(fadeChild, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                }
                container.scheduleLayoutAnimation() // Animates *THE WHOLE* layout.
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Delay layout to avoid animation lag.
        fadeContainer.postDelayed({
            fadeContainer.visibility = View.VISIBLE
            translateContainer.visibility = View.VISIBLE

            fadeContainer.layoutAnimation = LayoutAnimationController(AnimationUtils.loadAnimation(this, android.R.anim.fade_in))
            translateContainer.layoutAnimation = LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.translate_from_right))
        }, 500L)
    }

    private fun getChild() = TextView(this).apply {
        text = "An additional TextView with some body text."
        setTextSize(TypedValue.COMPLEX_UNIT_PX, this@ViewLayoutAnimationDelayedActivity.textSize)
    }
}
