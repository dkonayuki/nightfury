package com.origidgames.nightfurygetsfishes;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

public class GifView extends View {
	private Movie mMovie;
	private long movieStart;

	public GifView(Context context) {
		super(context);
		initializeView();
	}
	
	public GifView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeView();
	}
	
	public GifView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeView();
	}
	
	private void initializeView() {
		InputStream is = getContext().getResources().openRawResource(R.drawable.img_checkpoint);
		mMovie = Movie.decodeStream(is);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
	    canvas.drawColor(Color.TRANSPARENT);
	    super.onDraw(canvas);
	    long now = android.os.SystemClock.uptimeMillis();
	    if (movieStart == 0) {
	        movieStart = now;
	    }
	    if (mMovie != null) {
	        int relTime = (int) ((now - movieStart) % mMovie.duration());
	        mMovie.setTime(relTime);
	        mMovie.draw(canvas, 0, 0);
	        this.invalidate();
	    }
	}
}
