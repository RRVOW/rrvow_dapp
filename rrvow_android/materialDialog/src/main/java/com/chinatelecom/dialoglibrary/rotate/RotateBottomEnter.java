package com.chinatelecom.dialoglibrary.rotate;

import android.util.DisplayMetrics;
import android.view.View;

import com.chinatelecom.dialoglibrary.BaseAnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by ll on 2015/12/9.
 */
public class RotateBottomEnter extends BaseAnimatorSet {

    public RotateBottomEnter() {
        duration = 400;
    }

    @Override
    public void setAnimation(View view) {
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "rotationX",90, 0).setDuration(duration),
                ObjectAnimator.ofFloat(view, "translationY", 300, 0).setDuration(duration),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(duration*3/2)
        );
    }
}
