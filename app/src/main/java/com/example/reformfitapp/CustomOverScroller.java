package com.example.reformfitapp;

import android.content.Context;
import android.widget.ScrollView;

public class CustomOverScroller extends ScrollView {


    public CustomOverScroller(Context context) {
        super(context);

    }
    public interface OnOverScrolledListener {
        void onOverScrolled(android.widget.ScrollView scrollView,
                            int deltaX, int deltaY, boolean clampedX, boolean clampedY);
    }

    private OnOverScrolledListener mOnOverScrolledListener;

    private int mOverScrollByDeltaX;
    private int mOverScrollByDeltaY;

    @Override protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        this.mOverScrollByDeltaX = deltaX;
        this.mOverScrollByDeltaY = deltaY;
        final boolean result = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
        return result;
    };

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (mOnOverScrolledListener != null && (clampedX || clampedY)) {
            mOnOverScrolledListener.onOverScrolled(this, mOverScrollByDeltaX, mOverScrollByDeltaY, clampedX, clampedY);
        }
    }

    public OnOverScrolledListener getOnOverScrolledListener() {
        return mOnOverScrolledListener;
    }

    public void setOnOverScrolledListener(OnOverScrolledListener onOverScrolledListener) {
        this.mOnOverScrolledListener = onOverScrolledListener;
    }
}
