package com.origidgames.nightfurygetsfishes;

import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;

public class PausableRotate extends RotateAnimation {
	
	

	public PausableRotate(float fromDegrees, float toDegrees, int pivotXType,
			float pivotXValue, int pivotYType, float pivotYValue) {
		super(fromDegrees, toDegrees, pivotXType, pivotXValue, pivotYType, pivotYValue);
		// TODO Auto-generated constructor stub
	}

	private long mElapsedAtPause=0;
	private boolean mPaused=false;
	 
	 
	    @Override
	    public boolean getTransformation(long currentTime, Transformation outTransformation) { 
	        if(mPaused && mElapsedAtPause==0) {
	            mElapsedAtPause=currentTime-getStartTime();
	        }
	        if(mPaused)
	            setStartTime(currentTime-mElapsedAtPause);
	        return super.getTransformation(currentTime, outTransformation);
	    }

	    public void pause() {
	        mElapsedAtPause=0;
	        mPaused=true;
	    }

	    public void resume() {
	        mPaused=false;
	    }
}
