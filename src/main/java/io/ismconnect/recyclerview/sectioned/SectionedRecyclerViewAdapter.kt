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

package io.ismconnect.recyclerview.sectioned

import android.support.v7.widget.RecyclerView

/**
 * Represents a section position, most often used for tables with sectionCounts and rows.
 */
class SectionPosition(val section: Int, val position: Int) {
    override fun equals(other: Any?): Boolean {
        if (other !is SectionPosition) {
            return false
        }
        return this.section == other.section && this.position == other.position
    }

    override fun hashCode(): Int {
        var result = section
        result = 31 * result + position
        return result
    }
}

/**
 * A recycler view with section support.
 */
abstract class SectionedRecyclerViewAdapter<T: RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {

    abstract val numberOfSections: Int
    abstract fun itemCount(section: Int): Int
    abstract fun itemViewType(pos: SectionPosition): Int
    abstract fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: SectionPosition)

    /**
     * Holds a list of sectionCounts and their row counts.
     */
    internal val sectionCounts: List<Int>
        get() {
            return (0 until numberOfSections).map { itemCount(it) }
        }

    /**
     * Returns a [SectionPosition] based on an absolute position.
     */
    internal fun sectionPosition(absolutePosition: Int): SectionPosition {
        var sum = 0
        for ((index, value) in sectionCounts.withIndex()) {
            if (absolutePosition < sum + value) {
                return SectionPosition(index, absolutePosition - sum)
            }
            sum += value
        }

        throw IndexOutOfBoundsException()
    }

    /**
     * Returns an item count by totaling up all of the section counts.
     */
    final override fun getItemCount(): Int {
        return sectionCounts.reduce { sum, element -> sum + element}
    }

    /**
     * Returns an itemViewType.
     */
    final override fun getItemViewType(position: Int): Int {
        return itemViewType(sectionPosition(position))
    }

    /**
     * Binds the view holder.
     */
    final override fun onBindViewHolder(holder: T, position: Int) {
        return onBindViewHolder(holder, sectionPosition(position))
    }

}