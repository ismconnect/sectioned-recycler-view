/* Copyright 2018 ISM Connect. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package io.ismconnect.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Test for a [SectionedRecyclerViewAdapter].
 */
class SectionedRecyclerViewAdapterTest {

    class TestAdapterOne : SectionedRecyclerViewAdapter<RecyclerView.ViewHolder>() {

        private val headerViewType = 0
        private val row1ViewType = 1
        private val row2ViewType = 2
        private val row3ViewType = 3
        private val row4ViewType = 4

        override val numberOfSections: Int = 3

        override fun itemCount(section: Int): Int {
            return when (section) {
                0 -> 1
                1 -> 2
                2 -> 2
                else -> {
                    0
                }
            }
        }

        override fun itemViewType(pos: SectionPosition): Int {
            return if (pos.section == 0 && pos.position == 0) {
                headerViewType
            } else if (pos.section == 1 && pos.position == 0) {
                row1ViewType
            } else if (pos.section == 1 && pos.position > 0) {
                row2ViewType
            } else if (pos.section == 2 && pos.position == 0) {
                row3ViewType
            } else {
                row4ViewType
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: SectionPosition) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(View(parent.context))
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    }

    @Test
    fun numberOfSectionsIsCorrect() {
        val adapter = TestAdapterOne()
        assertEquals(3, adapter.numberOfSections)
        assertEquals(3, adapter.sectionCounts.count())
    }

    @Test
    fun sectionPositionIsCorrect() {
        val adapter = TestAdapterOne()

        // Test getItemViewType method which doesn't have sectionCounts and uses an absolute position
        assertEquals(adapter.sectionPosition(0), SectionPosition(0, 0))
        assertEquals(adapter.sectionPosition(1), SectionPosition(1, 0))
        assertEquals(adapter.sectionPosition(2), SectionPosition(1, 1))
        assertEquals(adapter.sectionPosition(3), SectionPosition(2, 0))
        assertEquals(adapter.sectionPosition(4), SectionPosition(2, 1))
    }

    @Test
    fun itemCountIsCorrect() {
        val adapter = TestAdapterOne()

        // Make sure the object above does what it should
        assertEquals(1, adapter.itemCount(0))
        assertEquals(2, adapter.itemCount(1))
        assertEquals(2, adapter.itemCount(2))
        assertEquals(0, adapter.itemCount(3))

        // All of the section counts added up should be 5
        assertEquals(5, adapter.itemCount)
    }
}
