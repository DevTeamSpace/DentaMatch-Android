package com.appster.dentamatch.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.appster.dentamatch.ui.messages.MessageListAdapter;
import com.appster.dentamatch.util.LogUtils;

/**
 * Created by Appster on 09/02/17.
 */

public class ItemHelperCallback extends ItemTouchHelper.SimpleCallback {
    private Paint mPaint = new Paint();
    private boolean translationComplete = false;
    float translationX = 0.0f;


    public ItemHelperCallback(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = ItemTouchHelper.START;
        return makeMovementFlags(dragFlags, swipeFlags);
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    //    @Override
//    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        float translationX;
//        float swipeWidth = viewHolder.itemView.getWidth() / 3;
//
//        if (dX < 0) {
//            translationX = Math.max(dX, -1 * swipeWidth);
//        } else {
//            translationX = Math.min(-dX, swipeWidth);
//        }
//
//        LogUtils.LOGD("denta_swipe", "x ->" + dX);
//        LogUtils.LOGD("denta_swipe", "item width" + swipeWidth);
//
//        if (translationX < 0) {
//            mPaint.setColor(Color.parseColor("#fe3824"));
//
//
//            RectF boundary = new RectF((float) viewHolder.itemView.getRight() + translationX,
//                    (float) viewHolder.itemView.getTop(),
//                    (float) viewHolder.itemView.getRight(),
//                    (float) viewHolder.itemView.getBottom());
//            c.drawRect(boundary, mPaint);
//
//            mPaint.setColor(Color.WHITE);
//            mPaint.setTextSize(50);
//            c.drawText("DELETE", boundary.centerX(), boundary.centerY(), mPaint);
//        }
//
//
//        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
//    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        float swipeWidth = ((MessageListAdapter.MyHolder) viewHolder).getConstantView().getWidth();

        if (dX < 0) {
            translationX = Math.max(dX, -1 * swipeWidth);
        }

        LogUtils.LOGD("denta_swipe", "x ->" + dX);
        LogUtils.LOGD("denta_swipe", "translationX ->" + translationX);
        LogUtils.LOGD("denta_swipe", "item width" + swipeWidth);
        LogUtils.LOGD("denta_swipe", "translationComplete:" + translationComplete);

        getDefaultUIUtil().onDraw(c, recyclerView, ((MessageListAdapter.MyHolder) viewHolder).getRemovableView(), translationX, dY, actionState, isCurrentlyActive);
    }


    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        getDefaultUIUtil().onDrawOver(c, recyclerView, ((MessageListAdapter.MyHolder) viewHolder).getRemovableView(), dX, dY, actionState, isCurrentlyActive);
    }
}

