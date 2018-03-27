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
import android.transition.Explode
import android.transition.Scene
import android.transition.TransitionManager
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
class TransitionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition)

        animateButton.setOnClickListener {
            val endScene = Scene.getSceneForLayout(container, R.layout.scene_transition_end, this)
            TransitionManager.go(endScene, TransitionToBanner())
        }
    }

    inner class TransitionToBanner : Explode() { // Why is this Explode?

        // In hindsight, I'm not supposed to make transitions for specific views like this: it
        // should be a generic transition built around startValues and endValues.
        //
        // Also, I'm supposed to specify the properties I want to capture with `captureStartValues`
        // and friends.
        override fun onAppear(sceneRoot: ViewGroup, view: View, startValues: TransitionValues?, endValues: TransitionValues?): Animator {
            return when (view.id) {
                R.id.banner -> {
                    AnimatorSet().apply {
                        val alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
                        val translation = ObjectAnimator.ofFloat(view, "translationY",
                                -view.height.toFloat(), 0f)

                        duration = BUTTON_FADE_OUT_MILLIS * 2 // Parallax with button fade out.
                        playTogether(alpha, translation)
                    }
                }

                R.id.bannerText -> {
                    AnimatorSet().apply {
                        val alpha = ObjectAnimator.ofFloat(view, "alpha", 1f)
                        val translation = ObjectAnimator.ofFloat(view, "translationY",
                                view.height * 0.5f, 0f)

                        duration = TEXT_FADE_IN_MILLIS
                        startDelay = Math.round(BUTTON_FADE_OUT_MILLIS * 2 * TEXT_START_PERCENT)
                        playTogether(alpha, translation)
                    }
                }

                else -> super.onAppear(sceneRoot, view, startValues, endValues)
            }
        }

        override fun onDisappear(sceneRoot: ViewGroup, view: View, startValues: TransitionValues?, endValues: TransitionValues?): Animator {
            return when (view.id) {
                R.id.animateButton -> {
                   AnimatorSet().apply {
                       duration = BUTTON_FADE_OUT_MILLIS
                       interpolator = INTERPOLATOR

                       val fade = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
                       val translation = ObjectAnimator.ofFloat(animateButton, "translationY",
                               sceneRoot.height * 0.1f)

                       playTogether(fade, translation)
                   }
                }

                else -> super.onDisappear(sceneRoot, view, startValues, endValues)
            }
        }
    }
}