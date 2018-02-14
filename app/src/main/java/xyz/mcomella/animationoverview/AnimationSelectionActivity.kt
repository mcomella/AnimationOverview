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
import xyz.mcomella.animationoverview.view.ViewAnimationSetActivity

/** The list of the sample animation activities we support. */
private val animationTitleToActivityClass = listOf(
        "View animation with AnimationSet" to ViewAnimationSetActivity::class.java
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

        // Double tapping will open two activities.
        holder.itemView.setOnClickListener { context.startActivity(Intent(context, activityClass)) }
    }

    override fun getItemCount() = animationTitleToActivityClass.size
}

private class AnimationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val animationTitle = itemView.findViewById<TextView>(R.id.animationTitle)
}
