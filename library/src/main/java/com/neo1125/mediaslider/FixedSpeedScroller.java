package com.neo1125.mediaslider;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

class FixedSpeedScroller extends Scroller {
    private int duration;

    public FixedSpeedScroller(Context context, int duration) {
        super(context);
        this.duration = duration;
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator, int duration) {
        super(context, interpolator);
        this.duration = duration;
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel, int duration) {
        super(context, interpolator, flywheel);
        this.duration = duration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, this.duration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, duration);
    }
}
