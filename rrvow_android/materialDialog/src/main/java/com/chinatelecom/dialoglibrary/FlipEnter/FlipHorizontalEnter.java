package com.chinatelecom.dialoglibrary.FlipEnter;

import android.view.View;

import com.chinatelecom.dialoglibrary.BaseAnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class FlipHorizontalEnter extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				// ObjectAnimator.ofFloat(view, "rotationY", -90, 0));
				ObjectAnimator.ofFloat(view, "rotationY", 90, 0));
	}
}
