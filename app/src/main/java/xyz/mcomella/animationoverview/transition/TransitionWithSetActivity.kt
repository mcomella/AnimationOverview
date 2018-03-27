/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package xyz.mcomella.animationoverview.transition

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.transition.AutoTransition
import android.transition.Explode
import android.transition.Fade
import android.transition.Scene
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.transition.TransitionValues
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import kotlinx.android.synthetic.main.activity_transition.*
import kotlinx.android.synthetic.main.scene_transition_start.*
import xyz.mcomella.animationoverview.R

private const val BUTTON_FADE_OUT_MILLIS = 600L
private const val TEXT_FADE_IN_MILLIS = 800L

/** Percentage of the banner animation that will pass before the text should start. */
private const val TEXT_START_PERCENT = 0.70

private val INTERPOLATOR = AccelerateDecelerateInterpolator()

/**
 * Activity to test out making transitions.
 *
 * Learnings:
 * - Provides an API to recreate re-usable generic transitions.
 * - Seems much more verbose than just animating in code if your animations are specific to your
 * views though perhaps there are benefits (e.g. cancelling?).
 *
 * Questions:
 * - What is the intended way to write transitions that are specific to views? That have start offsets?
 * Perhaps using TransitionSet?
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class TransitionWithSetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition)

        animateButton.setOnClickListener {
            val endScene = Scene.getSceneForLayout(container, R.layout.scene_transition_end, this)

            val animateButtonTransition = Fade().apply {
                addTarget(animateButton)
            }

            val otherTransition = AutoTransition().apply {
                excludeTarget(animateButton, true)
            }

            val set = TransitionSet().apply {
                addTransition(animateButtonTransition)
                addTransition(otherTransition)
            }

            TransitionManager.go(endScene, set)
        }
    }
}