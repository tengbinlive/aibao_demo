package com.mytian.lb.activityexpand.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Property;
import android.view.View;
import android.widget.FrameLayout;

import com.debug.ViewServer;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mytian.lb.App;
import com.mytian.lb.Constant;

public abstract class AnimatedRectActivity extends Activity {

    public AnimatedRectLayout mAnimated;
    protected int mAnimationType;
    private int DURATION = 600;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().activityManager.pushActivity(this);
        setContentView(getContentView());
        mTracker = App.getInstance().getDefaultTracker();
        FrameLayout activityRoot = (FrameLayout) findViewById(android.R.id.content);
        View parent = activityRoot.getChildAt(0);

        // better way ?
        mAnimated = new AnimatedRectLayout(this);
        activityRoot.removeView(parent);
        activityRoot.addView(mAnimated, parent.getLayoutParams());
        mAnimated.addView(parent);

        mAnimationType = getIntent().getIntExtra("animation_type", AnimatedRectLayout.ANIMATION_RANDOM);
        mAnimated.setAnimationType(mAnimationType);

        ObjectAnimator animator = ObjectAnimator.ofFloat(mAnimated, ANIMATED_RECT_LAYOUT_FLOAT_PROPERTY, 1).setDuration(DURATION);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animationStartEnd();
            }
        });
        animator.start();
        if (Constant.DEBUG) {
            ViewServer.get(this).removeWindow(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("TAG - "+this.getClass().getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        App.getInstance().setCurrentActivity(this);
        if (Constant.DEBUG) {
            ViewServer.get(this).setFocusedWindow(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getInstance().activityManager.popActivity(this);
        if (Constant.DEBUG) {
            ViewServer.get(this).removeWindow(this);
        }
    }

    protected abstract int getContentView();

    @Override
    public void onBackPressed() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mAnimated, ANIMATED_RECT_LAYOUT_FLOAT_PROPERTY, 0).setDuration(DURATION);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animationBackEnd();
            }
        });
        animator.start();
    }

    public void animationBackEnd() {
        finish();
    }

    public void animationStartEnd() {
    }


    private static final Property<AnimatedRectLayout, Float> ANIMATED_RECT_LAYOUT_FLOAT_PROPERTY =
            new Property<AnimatedRectLayout, Float>(Float.class, "ANIMATED_RECT_LAYOUT_FLOAT_PROPERTY") {

                @Override
                public void set(AnimatedRectLayout layout, Float value) {
                    layout.setProgress(value);
                }

                @Override
                public Float get(AnimatedRectLayout layout) {
                    return layout.getProgress();
                }
            };

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
