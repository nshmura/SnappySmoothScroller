package com.nshmura.snappysmoothscroller;

import android.view.animation.Interpolator;

public interface SnappyLayoutManager {

    void setSnapType(SnapType snapType);

    void setSnapDuration(int snapDuration);

    void setSnapInterpolator(Interpolator snapInterpolator);

    void setSnapPadding(int snapPadding);

    void setSnapPaddingStart(int snapPaddingStart);

    void setSnapPaddingEnd(int snapPaddingEnd);

    void setSeekDuration(int seekDuration);
}
