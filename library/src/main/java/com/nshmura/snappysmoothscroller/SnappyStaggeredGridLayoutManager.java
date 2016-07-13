package com.nshmura.snappysmoothscroller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class SnappyStaggeredGridLayoutManager extends StaggeredGridLayoutManager implements SnappyLayoutManager {

    private SnappySmoothScroller.Builder builder;

    public SnappyStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public SnappyStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
        init();
    }

    private void init() {
        builder = new SnappySmoothScroller.Builder();
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        SnappySmoothScroller scroller = builder
                .setPosition(position)
                .setScrollVectorDetector(new StaggeredGridLayoutScrollVectorDetector(this))
                .build(recyclerView.getContext());

        startSmoothScroll(scroller);
    }

    @Override
    public void setSnapType(SnapType snapType) {
        builder.setSnapType(snapType);
    }

    @Override
    public void setSnapDuration(int snapDuration) {
        builder.setSnapDuration(snapDuration);
    }

    @Override
    public void setSnapInterpolator(Interpolator snapInterpolator) {
        builder.setSnapInterpolator(snapInterpolator);
    }

    @Override
    public void setSnapPadding(int snapPadding) {
        builder.setSnapPadding(snapPadding);
    }

    @Override
    public void setSnapPaddingStart(int snapPaddingStart) {
        builder.setSnapPaddingStart(snapPaddingStart);
    }

    @Override
    public void setSnapPaddingEnd(int snapPaddingEnd) {
        builder.setSnapPaddingEnd(snapPaddingEnd);
    }

    @Override
    public void setSeekDuration(int seekDuration) {
        builder.setSeekDuration(seekDuration);
    }
}
