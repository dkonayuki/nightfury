/**************************************************************************************************************
 * Copyright (c) 2012 ORIGID GAMES STUDIO. 
 *************************************************************************************************************/

package com.origidgames.nightfurygetsfishes;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class GifView extends View {
	private long prev;
	private final int DURATION = 100;
	private Rect src, dst;
	private int currentImg = 0;

	public GifView(Context context) {
		super(context);
		_init();
	}
	
	public GifView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_init();
	}
	
	public GifView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		_init();
	}
	
	private void _init(){
		src = new Rect();
		dst = new Rect();
		dst.set(0, 0, 26, 26);
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
	    canvas.drawColor(Color.TRANSPARENT);
	    super.onDraw(canvas);
	    long now = android.os.SystemClock.uptimeMillis();
	    if(prev == 0) prev = now;
	    if (PublicResource.Star() != null) {
	    	if(now - prev >= DURATION){
		        currentImg = (currentImg + 1)%5;
		        if (currentImg == 0) prev+= 5000;
		        else prev = now;
	    	}
	    	src.set(26*currentImg, 0, 26*(currentImg + 1), 26);
	        canvas.drawBitmap(PublicResource.Star(), 
	        				src, dst, null);	 
	    	this.invalidate();	    	
	    }
	    
	}
}
