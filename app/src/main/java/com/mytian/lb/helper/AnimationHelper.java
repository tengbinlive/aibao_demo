package com.mytian.lb.helper;

import android.view.View;
import android.view.animation.BounceInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.view.ViewHelper;

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
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", -20, 20, 0);
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

    /**
     * 缩放动画
     *
     * @param view
     */
    public void viewAnimationScal(final View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1, 1.5f, 1);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1, 1.5f, 1);
        ViewHelper.setPivotX(view, view.getWidth() >> 1);
        ViewHelper.setPivotY(view, view.getHeight() >> 1);
        ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY).setDuration(300);
        animator2.setInterpolator(mInterpolator);
        animator2.start();
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
