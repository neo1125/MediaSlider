package com.neo1125.mediaslider;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

class SlideView extends RelativeLayout {

    SlideViewListner listner;
    String uri;
    int duration = 0;
    boolean isStart = false;

    public static SlideView create(Context context, Media media) {
        SlideView slideView;
        if (media.type == Media.Type.image)
            slideView = new SlideImageView(context);
        else
            slideView = new SlideVideoView(context);
        slideView.setUri(media.uri);
        return slideView;
    }

    public SlideView(Context context) {
        this(context, null);
    }

    public SlideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addListner(SlideViewListner listner) {
        this.listner = listner;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void start() {
        isStart = true;
    }

    public void pause() {
    }

    public void resume() {
    }

    public boolean isPlayEnd() {
        return true;
    }

    public Media.Type getType() {
        return Media.Type.image;
    }
}
