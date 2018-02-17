package xyz.mcomella.animationoverview.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_view_layout_animation.*
import xyz.mcomella.animationoverview.R

/**
 * A View Animation that explores the [LayoutAnimationController] API.
 *
 * Learnings:
 * - Setting a LayoutAnimationController will animate the layout of the specified ViewGroup.
 * - This does not animate layout *changes*: it will re-animate the entire layout when views are added.
 * There are Property Animation APIs to translate layout changes as well as transitions.
 * - You can combine multiple ViewGroups with LayoutAnimationControllers to use different animations.
 * - You specify only one animation XML with which to animate the views.
 * - I was unable to inflate in the XML: the code seems brief enough that it seems unnecessary.
 * - *THERE ARE FRAMED DROPS WHEN USING THIS FOR THE INITIAL LAYOUT ON OLDER DEVICES (GS5, N5).* You
 * can delay the layout to fix it - see [ViewLayoutAnimationDelayedActivity].
 */
class ViewLayoutAnimationActivity : AppCompatActivity() {

    private val textSize by lazy { (fadeContainer.getChildAt(1) as TextView).textSize }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_layout_animation)

        // Will animate the initial layout.
        fadeContainer.layoutAnimation = LayoutAnimationController(AnimationUtils.loadAnimation(this, android.R.anim.fade_in))
        translateContainer.layoutAnimation = LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.translate_from_right))

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

    private fun getChild() = TextView(this).apply {
        text = "An additional TextView with some body text."
        setTextSize(TypedValue.COMPLEX_UNIT_PX, this@ViewLayoutAnimationActivity.textSize)
    }
}
