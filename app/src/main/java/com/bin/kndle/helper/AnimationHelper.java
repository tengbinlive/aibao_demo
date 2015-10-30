package com.bin.kndle.helper;

import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by Administrator on 2015/10/30.
 */
public class AnimationHelper {

    private static AnimationHelper instance;

    private final BounceInterpolator mInterpolator = new BounceInterpolator();

    public static AnimationHelper getInstance() {
        if (instance == null) {
            instance = new AnimationHelper();
        }
        return instance;
    }

    public void viewAnimationQuiver(final View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX",-20, 20, 0);
        animator.setInterpolator(mInterpolator);
        animator.setDuration(500).start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                resetFocus(view);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void resetFocus(View view) {
        if (view == null) {
            return;
        }
        view.findFocus();
        view.requestFocus();
        view.requestFocusFromTouch();
    }

}
