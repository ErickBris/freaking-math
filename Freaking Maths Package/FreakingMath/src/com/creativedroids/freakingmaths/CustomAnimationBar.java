package com.creativedroids.freakingmaths;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class CustomAnimationBar extends Animation {

	private float iniHeight;
	private float iniWidth;
	private int time;
	private float finHeight;
	private float finWidth;
	private View view;

	public CustomAnimationBar(View view, float iniWidth, float iniHeight, float finWidth,
			float finHeight, int time) {
		this.finHeight = finHeight;
		this.finWidth = finWidth;
		this.iniHeight = iniHeight;
		this.iniWidth = iniWidth;
		this.view = view;
		this.time = time;
		this.setDuration((long) time);
	}

	protected void applyTransformation(float interpolatedTime, Transformation t) {
		float trueHeight = interpolatedTime * (finHeight - iniHeight) + iniHeight;
		float trueWidth = interpolatedTime * (finWidth - iniWidth) + iniWidth;
		LayoutParams params = view.getLayoutParams();
		params.height = (int) trueHeight;
		params.width = (int) trueWidth;
		view.requestLayout();
	}
	
}
