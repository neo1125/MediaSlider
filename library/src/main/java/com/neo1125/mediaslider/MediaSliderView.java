package com.neo1125.mediaslider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;

public class MediaSliderView extends RelativeLayout {

    public enum State {
        ready, end
    }

    private ImageView overlayView;
    private Bitmap overlayBitmap;
    private SlideViewPager viewPager;
    private MediaSliderAdapter mediaSliderAdapter;
    private long fadeAnimationDuration = 600;
    private int slideDuration = 5 * 1000;
    private int pauseBeforeSlideDuration = slideDuration;
    private int scrollDuration = 1000;
    private MediaSliderEventListner eventListner;

    public MediaSliderView(Context context) {
        this(context, null);
    }

    public MediaSliderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaSliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        viewPager = new SlideViewPager(getContext());
        viewPager.setBackgroundColor(Color.BLUE);
        viewPager.setAdapter(new SliderPagerAdapter());

        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext(), new AccelerateDecelerateInterpolator(), scrollDuration);
            mScroller.set(viewPager, scroller);
        } catch(NoSuchFieldException e){
            e.printStackTrace();
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }

        addView(viewPager, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        overlayView = new ImageView(getContext());
        overlayView.setBackgroundColor(Color.BLACK);
        overlayView.setAlpha(1.0f);
        addView(overlayView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setAdapter(MediaSliderAdapter adapter) {
        mediaSliderAdapter = adapter;
    }

    public void setEventListner(MediaSliderEventListner listner) {
        eventListner = listner;
    }

    public void setDuration(int duration) {
        slideDuration = duration;
    }

    public void start() {
        if (viewPager.getAdapter() != null && viewPager.getAdapter().getCount() > 0)
            viewPager.setCurrentItem(0, true);
    }

    public void pause() {
        pauseBeforeSlideDuration = slideDuration;
        SlideView slideView = ((SliderPagerAdapter) viewPager.getAdapter()).getCurrentView();
        slideView.pause();
        slideDuration = 0;
    }

    public void resume() {
        slideDuration = pauseBeforeSlideDuration;
        SlideView slideView = ((SliderPagerAdapter) viewPager.getAdapter()).getCurrentView();
        if (slideView.isPlayEnd())
            next();
        else
            slideView.resume();
    }

    public void next() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showOverlayLayout();
                if (viewPager.getAdapter() != null && viewPager.getAdapter().getCount() > 0)
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }
        }, fadeAnimationDuration);
    }

    private void showOverlayLayout() {
        AlphaAnimation animation = new AlphaAnimation(overlayView.getAlpha(), 1.0f);
        animation.setDuration(fadeAnimationDuration);
        animation.setFillAfter(true);
        overlayView.startAnimation(animation);
    }

    private void hideOverlayLayout() {
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(fadeAnimationDuration);
        animation.setFillAfter(true);
        overlayView.startAnimation(animation);
    }

    private void captured(SlideView view) {
        if (overlayBitmap != null) {
            overlayView.setImageBitmap(null);
            overlayBitmap.recycle();
            overlayBitmap = null;
        }

        view.buildDrawingCache();
        overlayBitmap = Bitmap.createBitmap(view.getDrawingCache());
        if (overlayBitmap != null) {
            overlayView.setImageBitmap(overlayBitmap);
        } else {
            overlayView.setImageBitmap(null);
            overlayView.setBackgroundColor(Color.BLACK);
        }
    }

    class SliderPagerAdapter extends PagerAdapter {

        private SlideView currentView;

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (currentView != object) {
                currentView = (SlideView) object;
                if (!currentView.isStart)
                    currentView.start();
            }
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (mediaSliderAdapter == null) return null;
            int index = position % mediaSliderAdapter.getCount();
            Media media = mediaSliderAdapter.instantiateMedia(index);
            SlideView slideView = SlideView.create(getContext(), media);
            slideView.setDuration(scrollDuration + slideDuration);
            slideView.addListner(new SlideViewListner() {
                @Override
                public void nextPage() {
                    captured(currentView);
                    showOverlayLayout();
                    if (eventListner != null)
                        eventListner.changedState(currentView.getType(), State.end);

                    if (slideDuration > 0)
                        next();
                }

                @Override
                public void onReady() {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideOverlayLayout();

                            if (eventListner != null)
                                eventListner.changedState(currentView.getType(), State.ready);

                        }
                    }, scrollDuration);
                }

                @Override
                public void onError(Error e) {
                    next();
                }
            });
            slideView.setUri(media.uri);
            container.addView(slideView);
            return slideView;
        }

        public SlideView getCurrentView() {
            return currentView;
        }
    }
}
