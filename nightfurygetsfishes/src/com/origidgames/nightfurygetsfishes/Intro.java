package com.origidgames.nightfurygetsfishes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Intro extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		TextView intro = (TextView) findViewById(R.id.intro);
		//Typeface font = Typeface.createFromAsset(getAssets(), "viking.ttf");
		//intro.setTypeface(font);
		final Handler handler = new Handler();
		final Runnable my_run = new Runnable() {
			  public void run() {
				  finish();
				  startActivity(new Intent("com.origidgames.nightfuryUI3"));	
			  }
			};
		handler.postDelayed(my_run, 25000);
		intro.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				handler.removeCallbacks(my_run);
				startActivity(new Intent("com.origidgames.nightfuryUI3"));
			}
			
		});
		AssetManager assetManager = getAssets();
			try {
				InputStream input = assetManager.open("nightfuryIntro.txt");
				String text = loadTextFile(input);
				intro.setText(text);
				intro.startAnimation(PublicResource.BotToTop());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	}
	
	

	private String loadTextFile(InputStream input) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byte[] bytes = new byte[4096];
		int len = 0;
		while ((len=input.read(bytes))>0)
		byteStream.write(bytes,0,len);
		return new String(byteStream.toByteArray(),"UTF8");
	}
}
