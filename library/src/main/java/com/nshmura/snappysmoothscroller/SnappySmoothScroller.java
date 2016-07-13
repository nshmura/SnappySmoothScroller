package com.nshmura.snappysmoothscroller;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public class SnappySmoothScroller extends LinearSmoothScroller {

    private static final SeekDistance INVALID_SEEK_DISTANCE = new SeekDistance(0, 0);
    private static final int SEEK_MIN_DURATION = 10000;
    private static final int DEFAULT_SEEK_SCROLL_DURATION = 500;
    private static final int DEFAULT_SNAP_DURATION = 600;

    private SnapType snapType = SnapType.VISIBLE;
    private ScrollVectorDetector scrollVectorDetector;
    private Interpolator snapInterpolator = new DecelerateInterpolator();
    private int snapDuration = DEFAULT_SNAP_DURATION;
    private int seekDuration = DEFAULT_SEEK_SCROLL_DURATION;

    private SeekDistance seekDistance;
    private int snapPaddingStart;
    private int snapPaddingEnd;

    public static class Builder {
        private SnapType snapType;
        private Interpolator snapInterpolator;
        private int snapDuration = -1;
        private int seekDuration = -1;
        private int snapPaddingStart;
        private int snapPaddingEnd;
        private int position;
        private ScrollVectorDetector scrollVectorDetector;

        public Builder setSnapType(SnapType snapType) {
            this.snapType = snapType;
            return this;
        }

        public Builder setSnapDuration(int snapDuration) {
            this.snapDuration = snapDuration;
            return this;
        }

        public Builder setSnapInterpolator(Interpolator snapInterpolator) {
            this.snapInterpolator = snapInterpolator;
            return this;
        }

        public Builder setSnapPadding(int snapPadding) {
            this.snapPaddingStart = snapPadding;
            this.snapPaddingEnd = snapPadding;
            return this;
        }

        public Builder setSnapPaddingStart(int snapPaddingStart) {
            this.snapPaddingStart = snapPaddingStart;
            return this;
        }

        public Builder setSnapPaddingEnd(int snapPaddingEnd) {
            this.snapPaddingEnd = snapPaddingEnd;
            return this;
        }

        public Builder setSeekDuration(int seekDuration) {
            this.seekDuration = seekDuration;
            return this;
        }

        public Builder setPosition(int position) {
            this.position = position;
            return this;
        }

        public Builder setScrollVectorDetector(ScrollVectorDetector scrollVectorDetector) {
            this.scrollVectorDetector = scrollVectorDetector;
            return this;
        }

        public SnappySmoothScroller build(Context context) {
            SnappySmoothScroller scroller = new SnappySmoothScroller(context);
            scroller.setTargetPosition(position);

            if (scrollVectorDetector != null) {
                scroller.setScrollVectorDetector(scrollVectorDetector);
            }

            if (snapType != null) {
                scroller.setSnapType(snapType);
            }

            if (snapDuration >= 0) {
                scroller.setSnapDuration(snapDuration);
            }

            if (snapInterpolator != null) {
                scroller.setSnapInterpolator(snapInterpolator);
            }

            if (seekDuration >= 0) {
                scroller.setSeekDuration(seekDuration);
            }

            scroller.setSnapPaddingStart(snapPaddingStart);
            scroller.setSnapPaddingEnd(snapPaddingEnd);

            return scroller;
        }
    }

    public interface ScrollVectorDetector {
        PointF computeScrollVectorForPosition(int targetPosition);
    }

    private static class SeekDistance {
        public final float distanceInPixels;
        public final float duration;

        private SeekDistance(float distanceInPixels, float duration) {
            this.distanceInPixels = distanceInPixels;
            this.duration = duration;
        }
    }

    public SnappySmoothScroller(Context context) {
        super(context);
    }

    public void setSnapType(SnapType snapType) {
        this.snapType = snapType;
    }

    public void setSnapDuration(int snapDuration) {
        this.snapDuration = snapDuration;
    }

    public void setScrollVectorDetector(ScrollVectorDetector scrollVectorDetector) {
        this.scrollVectorDetector = scrollVectorDetector;
    }

    public void setSeekDuration(int seekDuration) {
        this.seekDuration = seekDuration;
    }

    public void setSnapInterpolator(Interpolator snapIterpolator) {
        snapInterpolator = snapIterpolator;
    }

    public void setSnapPaddingStart(int snapPaddingStart) {
        this.snapPaddingStart = snapPaddingStart;
    }

    public void setSnapPaddingEnd(int snapPaddingEnd) {
        this.snapPaddingEnd = snapPaddingEnd;
    }


    @Override
    protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
        final int dx = calculateDxToMakeVisible(targetView, getHorizontalSnapPreference());
        final int dy = calculateDyToMakeVisible(targetView, getVerticalSnapPreference());
        action.update(-dx, -dy, snapDuration, snapInterpolator);
    }

    @Override
    protected void onSeekTargetStep(int dx, int dy, RecyclerView.State state, Action action) {
        if (seekDistance == null) {
            computeSeekDistance();
        }
        super.onSeekTargetStep(dx, dy, state, action);
    }

    private void computeSeekDistance() {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null
                && layoutManager.getChildCount() > 0
                && layoutManager.getItemCount() > 0
                && (layoutManager.canScrollHorizontally() || layoutManager.canScrollVertically())) {

            int currentPosition = layoutManager.getPosition(layoutManager.getChildAt(0));

            int totalWidth = 0;
            int totalHeight = 0;
            final int count = layoutManager.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = layoutManager.getChildAt(i);
                totalWidth += child.getWidth();
                totalHeight += child.getHeight();
            }

            int distanceX = 0;
            if (layoutManager.canScrollHorizontally()) {
                final int averageWidth = totalWidth / count;
                distanceX = Math.abs((currentPosition - getTargetPosition()) * averageWidth);
            }

            int distanceY = 0;
            if (layoutManager.canScrollVertically()) {
                final int averageHeight = totalHeight / count;
                distanceY = Math.abs((currentPosition - getTargetPosition()) * averageHeight);
            }

            final int distanceInPixels = (int) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
            if (distanceInPixels > SEEK_MIN_DURATION) {
                seekDistance = new SeekDistance(distanceInPixels, seekDuration);
            }
        }
        if (seekDistance == null) {
            seekDistance = INVALID_SEEK_DISTANCE;
        }
    }

    @Override
    public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
        switch (snapType) {
            case START:
                return boxStart - viewStart + snapPaddingStart;

            case END:
                return boxEnd - viewEnd - snapPaddingEnd;

            case CENTER:
                final int boxDistance = boxEnd - boxStart;
                final int viewDistance = viewEnd - viewStart;
                return (boxDistance - viewDistance) / 2 - viewStart + boxStart;

            case VISIBLE:
                final int dtStart = boxStart - viewStart + snapPaddingStart;
                if (dtStart > 0) {
                    return dtStart;
                }
                final int dtEnd = boxEnd - viewEnd - snapPaddingEnd;
                if (dtEnd < 0) {
                    return dtEnd;
                }
                return 0;

            default:
                return super.calculateDtToFit(viewStart, viewEnd, boxStart, boxEnd, snapPreference);
        }
    }

    @Override
    public int calculateDxToMakeVisible(View view, int snapPreference) {
        int dx = super.calculateDxToMakeVisible(view, snapPreference);
        if (dx == 0) {
            return dx;
        }
        switch (snapType) {
            case START:
                dx = adjustDxForLeft(dx);
                break;

            case END:
                dx = adjustDxForRight(dx);
                break;

            case CENTER:
                if (dx > 0) {
                    dx = adjustDxForRight(dx);
                } else {
                    dx = adjustDxForLeft(dx);
                }
                break;
        }
        return dx;
    }

    @Override
    public int calculateDyToMakeVisible(View view, int snapPreference) {
        int dy = super.calculateDyToMakeVisible(view, snapPreference);
        if (dy == 0) {
            return dy;
        }
        switch (snapType) {
            case START:
                dy = adjustDyForUp(dy);
                break;

            case END:
                dy = adjustDyForDown(dy);
                break;

            case CENTER:
                if (dy > 0) {
                    dy = adjustDyForDown(dy);
                } else {
                    dy = adjustDyForUp(dy);
                }
                break;

            case VISIBLE:

        }
        return dy;
    }

    private int adjustDxForLeft(int dx) {
        final RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null || !layoutManager.canScrollHorizontally()) {
            return 0;
        }

        final View lastChild = layoutManager.getChildAt(layoutManager.getChildCount() - 1);
        final int position = layoutManager.getPosition(lastChild);
        if (position == layoutManager.getItemCount() - 1) {
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) lastChild.getLayoutParams();
            final int maxDx = layoutManager.getWidth() - layoutManager.getPaddingRight()
                    - (layoutManager.getDecoratedRight(lastChild) + params.rightMargin);
            if (dx < maxDx) {
                return maxDx;
            }
        }
        return dx;
    }

    private int adjustDxForRight(int dx) {
        final RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null || !layoutManager.canScrollHorizontally()) {
            return 0;
        }

        final View firstChild = layoutManager.getChildAt(0);
        final int position = layoutManager.getPosition(firstChild);
        if (position == 0) {
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) firstChild.getLayoutParams();
            final int maxDx = -(layoutManager.getDecoratedLeft(firstChild) - params.leftMargin) + layoutManager.getPaddingLeft();
            if (dx > maxDx) {
                return maxDx;
            }
        }
        return dx;
    }

    private int adjustDyForUp(int dy) {
        final RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null || !layoutManager.canScrollVertically()) {
            return 0;
        }

        final View lastChild = layoutManager.getChildAt(layoutManager.getChildCount() - 1);
        final int position = layoutManager.getPosition(lastChild);
        if (position == layoutManager.getItemCount() - 1) {
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) lastChild.getLayoutParams();
            final int maxDy = layoutManager.getHeight() - layoutManager.getPaddingBottom()
                    - (layoutManager.getDecoratedBottom(lastChild) + params.bottomMargin);

            if (dy < maxDy) {
                return maxDy;
            }
        }
        return dy;
    }

    private int adjustDyForDown(int dy) {
        final RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null || !layoutManager.canScrollVertically()) {
            return 0;
        }

        final View firstChild = layoutManager.getChildAt(0);
        final int position = layoutManager.getPosition(firstChild);
        if (position == 0) {
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) firstChild.getLayoutParams();
            final int maxDy = -(layoutManager.getDecoratedTop(firstChild) - params.topMargin) + layoutManager.getPaddingTop();
            if (dy > maxDy) {
                return maxDy;
            }
        }
        return dy;
    }

    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        if (scrollVectorDetector != null) {
            return scrollVectorDetector.computeScrollVectorForPosition(targetPosition);
        } else {
            return null;
        }
    }

    @Override
    protected int calculateTimeForScrolling(int dx) {
        if (seekDistance != null && seekDistance != INVALID_SEEK_DISTANCE) {
            float proportion = (float) dx / seekDistance.distanceInPixels;
            int time = (int) (seekDistance.duration * proportion);
            if (time > 0) {
                return time;
            }
        }
        return super.calculateTimeForScrolling(dx);
    }
}
