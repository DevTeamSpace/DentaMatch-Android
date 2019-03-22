package com.appster.dentamatch.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.appster.dentamatch.R
import kotlin.math.absoluteValue

class SwipeToDeleteCallback(private val adapter: SwipeableAdapter) :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_delete_white_24dp)

    private val background = ColorDrawable(Color.RED)

    private val iconRightMargin =
            adapter.getContext().resources.getDimensionPixelSize(R.dimen.notification_delete_margin)

    override fun onMove(p0: RecyclerView,
                        p1: RecyclerView.ViewHolder,
                        p2: RecyclerView.ViewHolder) = false

    override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) =
            adapter.delete(p0.adapterPosition)

    override fun onChildDraw(c: Canvas,
                             recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder,
                             dX: Float,
                             dY: Float,
                             actionState: Int,
                             isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val iconMargin = (itemView.height - (icon?.intrinsicHeight ?: 0)) / 2
        val iconTop = itemView.top + (itemView.height - (icon?.intrinsicHeight ?: 0)) / 2
        val iconBottom = iconTop + (icon?.intrinsicHeight ?: 0)
        if (dX < 0) { // Swiping to the left
            val iconLeft = itemView.right - iconRightMargin - (icon?.intrinsicWidth ?: 0)
            val iconRight = itemView.right - iconRightMargin
            icon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom)
            background.alpha = calculateBackgroundAlpha(dX, itemView.width)
            background.draw(c)
            icon?.draw(c)
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0)
            background.draw(c)
        }
    }

    private fun calculateBackgroundAlpha(dX: Float, itemWidth: Int): Int {
        val alpha = (65 + ((dX.absoluteValue) / (itemWidth.toFloat() / 2))*255).toInt()
        if (alpha < 255) {
            return alpha
        }
        return 255
    }
}