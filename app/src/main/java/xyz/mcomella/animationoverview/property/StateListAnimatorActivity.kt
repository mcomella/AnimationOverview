/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package xyz.mcomella.animationoverview.property

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import xyz.mcomella.animationoverview.R

/**
 * "Property animation" for setting animations in XML based on view state (e.g. focused, pressed).
 *
 * Learnings:
 * - Probably how ripples are implemented
 * - Can specify custom transition animations between states.
 * - Allows you to cancel animations midway (e.g. holding and releasing before completion)
 */
class StateListAnimatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_list_animator)
    }
}