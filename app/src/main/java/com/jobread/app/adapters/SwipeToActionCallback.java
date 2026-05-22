package com.jobread.app.adapters;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToActionCallback extends ItemTouchHelper.SimpleCallback {

    public interface OnSwipeListener {
        void onSwipeDelete(int position);
        void onSwipeArchive(int position);
    }

    private final OnSwipeListener mListener;
    private final Paint mDeletePaint;
    private final Paint mArchivePaint;
    private final Paint mTextPaint;

    public SwipeToActionCallback(android.content.Context context, OnSwipeListener listener) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.mListener = listener;

        mDeletePaint = new Paint();
        mDeletePaint.setColor(Color.parseColor("#F44336"));

        mArchivePaint = new Paint();
        mArchivePaint.setColor(Color.parseColor("#FF9800"));

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(48f);
        mTextPaint.setAntiAlias(true);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.RIGHT) {
            mListener.onSwipeDelete(position);
        } else if (direction == ItemTouchHelper.LEFT) {
            mListener.onSwipeArchive(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c,
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            android.view.View itemView = viewHolder.itemView;
            float height = itemView.getBottom() - itemView.getTop();
            float width = height / 3f;

            if (dX > 0) {
                // Swipe right → Delete (red)
                RectF background = new RectF(
                        itemView.getLeft(),
                        itemView.getTop(),
                        itemView.getLeft() + dX,
                        itemView.getBottom());
                c.drawRect(background, mDeletePaint);
                float textX = itemView.getLeft() + width * 0.4f;
                float textY = itemView.getTop() + height / 2f + 16f;
                c.drawText("DELETE", textX, textY, mTextPaint);
            } else if (dX < 0) {
                // Swipe left → Archive (orange)
                RectF background = new RectF(
                        itemView.getRight() + dX,
                        itemView.getTop(),
                        itemView.getRight(),
                        itemView.getBottom());
                c.drawRect(background, mArchivePaint);
                float textWidth = mTextPaint.measureText("ARCHIVE");
                float textX = itemView.getRight() - width - textWidth / 2f;
                float textY = itemView.getTop() + height / 2f + 16f;
                c.drawText("ARCHIVE", textX, textY, mTextPaint);
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
