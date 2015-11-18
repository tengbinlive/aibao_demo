package com.mytian.lb.activityexpand.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Property;
import android.view.View;
import android.widget.FrameLayout;

import com.mytian.lb.App;
import com.mytian.lb.helper.ThemeHelper;

public abstract class AnimatedRectActivity extends Activity {

    public AnimatedRectLayout mAnimated;
    protected int mAnimationType;
    private int DURATION = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.getInstance().setThemeType(this,isThemeTranslucent());
        super.onCreate(savedInstanceState);
        App.getInstance().activityManager.pushActivity(this);
        setContentView(getContentView());
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
    }

    protected abstract int getContentView();

    public boolean isThemeTranslucent(){
        return true;
    }

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

    public void animationBackEnd(){
        App.getInstance().activityManager.popActivity(this);
    }

    public void animationStartEnd(){
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
