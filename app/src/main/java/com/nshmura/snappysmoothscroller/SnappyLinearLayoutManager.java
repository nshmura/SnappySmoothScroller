package com.nshmura.snappysmoothscroller;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class SnappyLinearLayoutManager extends LinearLayoutManager {

    private SnappyType snapType;
    private int snapDuration;
    private Interpolator snapInterpolator;
    private int snapPaddingStart;
    private int snapPaddingEnd;
    private int seekDuration;

    public SnappyLinearLayoutManager(Context context) {
        super(context);
    }

    public SnappyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public SnappyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        SnappySmoothScroller scroller = new SnappySmoothScroller(recyclerView.getContext());
        scroller.setScrollVectorDetector(new LinearLayoutScrollVectorDetector(this));
        scroller.setSnapType(snapType);
        scroller.setSnapDuration(snapDuration);
        scroller.setSnapInterpolator(snapInterpolator);
        scroller.setTargetPosition(position);
        scroller.setSnapPaddingStart(snapPaddingStart);
        scroller.setSnapPaddingEnd(snapPaddingEnd);
        scroller.setSeekDuration(seekDuration);
        startSmoothScroll(scroller);
    }

    public void setSnapType(SnappyType snapType) {
        this.snapType = snapType;
    }

    public void setSnapDuration(int snapDuration) {
        this.snapDuration = snapDuration;
    }

    public void setSnapInterpolator(Interpolator snapInterpolator) {
        this.snapInterpolator = snapInterpolator;
    }

    public void setSnapPadding(int snapPadding) {
        this.snapPaddingStart = snapPadding;
        this.snapPaddingEnd = snapPadding;
    }

    public void setSnapPaddingStart(int snapPaddingStart) {
        this.snapPaddingStart = snapPaddingStart;
    }

    public void setSnapPaddingEnd(int snapPaddingEnd) {
        this.snapPaddingEnd = snapPaddingEnd;
    }

    public void setSeekDuration(int seekDuration) {
        this.seekDuration = seekDuration;
    }
}
