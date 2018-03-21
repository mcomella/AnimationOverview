package xyz.mcomella.animationoverview.property

import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_animate_layout_changes.*
import xyz.mcomella.animationoverview.R

/**
 * A "Property Animation" that animates by default when modifying a layout.
 *
 * Learnings:
 * - Really simple
 * - Can use custom animations for various add/removal states.
 */
class AnimateLayoutChangesActivity : AppCompatActivity() {

    var viewCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animate_layout_changes)

        addButton.setOnClickListener {
            val textView = TextView(this).apply {
                text = "A new TextView: ${viewCount++}"
                textSize = 24f
            }

            content.addView(textView)
        }
    }
}
