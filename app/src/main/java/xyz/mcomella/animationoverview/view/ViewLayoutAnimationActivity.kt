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
 * - Setting a LayoutAnimationController will animate the *whole* layout
 * - You can set one animation for a ViewGroup. Presumably, you can
 *
 * I was unable to inflate in XML but didn't care to. Relpaced later?
 *
 * Grid.
 *
 * Learning
 */
class ViewLayoutAnimationActivity : AppCompatActivity() {

    private val textSize by lazy { (textContainerView.getChildAt(1) as TextView).textSize }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_layout_animation)

        // Will animate the initial layout.
        textContainerView.layoutAnimation = LayoutAnimationController(AnimationUtils.loadAnimation(this, android.R.anim.fade_in))

        startAnimButton.setOnClickListener {
            for (i in 0..1) {
                val newChild = getChild()
                textContainerView.addView(newChild, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                textContainerView.scheduleLayoutAnimation() // Animates *THE WHOLE* layout.
            }
        }
    }

    private fun getChild() = TextView(this).apply {
        text = "An additional TextView with some body text."
        setTextSize(TypedValue.COMPLEX_UNIT_PX, this@ViewLayoutAnimationActivity.textSize)
    }
}
