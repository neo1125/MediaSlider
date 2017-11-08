package com.neo1125.mediaslider;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

class SlideImageView extends SlideView {

    private ImageView imageView;
    private Handler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (listner != null)
                listner.nextPage();

            handler.removeCallbacks(runnable);
            handler = null;
        }
    };

    public SlideImageView(Context context) {
        this(context, null);
    }

    public SlideImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        imageView = new ImageView(getContext());
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setBackgroundColor(Color.BLACK);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        addView(imageView);
    }

    @Override
    public void start() {
        if (uri != null && !uri.isEmpty()) {
            Picasso.with(getContext())
                    .load(uri)
                    .fit()
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            isStart = true;
                            if (duration > 0) {
                                handler = new Handler();
                                handler.postDelayed(runnable, duration);
                            }

                            if (listner != null)
                                listner.onReady();
                        }

                        @Override
                        public void onError() {
                            if (listner != null)
                                listner.onError(new Error("Not found image"));
                        }
                    });
        }
    }

    @Override
    public void pause() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
            handler = null;
        }
    }

    @Override
    public void resume() {
        if (duration > 0) {
            handler = new Handler();
            handler.postDelayed(runnable, duration);
        }
    }
}
