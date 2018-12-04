package com.example.chris.githubviewer.viewUtil

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemCount = parent.adapter?.let { it.itemCount - 1 }
        if (parent.getChildAdapterPosition(view) != itemCount) {
            outRect.bottom = verticalSpaceHeight
        }
    }
}