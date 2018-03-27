package xyz.mcomella.animationoverview

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_animation_selection.*
import xyz.mcomella.animationoverview.property.AnimateLayoutChangesActivity
import xyz.mcomella.animationoverview.property.ObjectAnimatorInCodeActivity
import xyz.mcomella.animationoverview.property.StateListAnimatorActivity
import xyz.mcomella.animationoverview.property.ViewPropertyAnimatorActivity
import xyz.mcomella.animationoverview.transition.TransitionActivity
import xyz.mcomella.animationoverview.transition.TransitionWithSetActivity
import xyz.mcomella.animationoverview.view.ViewAnimationInCodeActivity
import xyz.mcomella.animationoverview.view.ViewAnimationInXMLActivity
import xyz.mcomella.animationoverview.view.ViewLayoutAnimationActivity
import xyz.mcomella.animationoverview.view.ViewLayoutAnimationDelayedActivity

/** The list of the sample animation activities we support. */
private val animationTitleToActivityClass = listOf(
        "View animation in XML" to ViewAnimationInXMLActivity::class.java,
        "View animation in code" to ViewAnimationInCodeActivity::class.java,
        "View LayoutAnimation" to ViewLayoutAnimationActivity::class.java,
        "View LayoutAnimation delayed" to ViewLayoutAnimationDelayedActivity::class.java,
        "(11+) ObjectAnimator in code" to ObjectAnimatorInCodeActivity::class.java,
        "(12+) ViewPropertyAnimator" to ViewPropertyAnimatorActivity::class.java,
        "(11+) animateLayoutChanges=true" to AnimateLayoutChangesActivity::class.java,
        "(21+) StateListAnimator" to StateListAnimatorActivity::class.java,
        "(19+ or support) Transitions" to TransitionActivity::class.java,
        "(19+ or support) Transitions 2" to TransitionWithSetActivity::class.java
)

/**
 * Presents the user of a list of animations to try. We use this approach to make it easy
 * to write multiple animations in the same application.
 */
class AnimationSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation_selection)

        animationList.apply {
            val orientation = LinearLayoutManager.VERTICAL
            layoutManager = LinearLayoutManager(this@AnimationSelectionActivity,
                    orientation, false)
            adapter = AnimationListAdapter()
            addItemDecoration(DividerItemDecoration(this@AnimationSelectionActivity, orientation))
        }
    }

    override fun onResume() {
        super.onResume()
        for (i in 0..animationList.childCount-1) {
            val child = animationList.getChildAt(i)
            child.isClickable = true // We disable click listeners after click to prevent double click: undo it.
        }
    }
}

private class AnimationListAdapter : RecyclerView.Adapter<AnimationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_animation_list, parent, false)
        return AnimationViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimationViewHolder, position: Int) {
        val context = holder.itemView.context
        val (title, activityClass) = animationTitleToActivityClass[position]
        holder.animationTitle.text = title
        holder.itemView.setOnClickListener {
            it.isClickable = false // Prevent double-tap.
            context.startActivity(Intent(context, activityClass))
        }
    }

    override fun getItemCount() = animationTitleToActivityClass.size
}

private class AnimationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val animationTitle = itemView.findViewById<TextView>(R.id.animationTitle)
}
