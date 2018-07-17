package com.chinatelecom.dialoglibrary.SlideEnter;

import android.util.DisplayMetrics;
import android.view.View;

import com.chinatelecom.dialoglibrary.BaseAnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class SlideLeftEnter extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "translationX", -250 * dm.density, 0), //
				ObjectAnimator.ofFloat(view, "alpha", 0.2f, 1));
	}
}
