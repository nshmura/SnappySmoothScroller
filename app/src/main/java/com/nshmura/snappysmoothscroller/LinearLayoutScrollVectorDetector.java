package com.nshmura.snappysmoothscroller;

import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;

public class LinearLayoutScrollVectorDetector implements SnappySmoothScroller.ScrollVectorDetector {

    private LinearLayoutManager layoutManager;

    public LinearLayoutScrollVectorDetector(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        return layoutManager.computeScrollVectorForPosition(targetPosition);
    }
}
