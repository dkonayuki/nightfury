/**************************************************************************************************************
 * Copyright (c) 2012 ORIGID GAMES STUDIO. 
 *************************************************************************************************************/

package com.origidgames.nightfurygetsfishes;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class UI1 extends Activity{
	  private AudioManager audio;
	  private static final String BGMFile = "main_bgm.mp3";
	  private WakeLock wakeLock;
	  private MediaPlayer mediaPlayer;
	
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.layout_ui1);
	        
	        Button btnPlay = (Button) findViewById(R.id.btn_play);
	        btnPlay.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					finish();
					startActivity(new Intent("com.origidgames.nightfuryUI2"));
				}       
	        });
	        
	        Button btnHighscore = (Button) findViewById(R.id.btn_highscore);
	        btnHighscore.setOnClickListener(new OnClickListener() {        
				public void onClick(View v) {
					finish();
					startActivity(new Intent("com.origidgames.nightfuryUI6"));
				}        	
	        });
	        
	        Button btnExit = (Button) findViewById(R.id.btn_exit);
	        btnExit.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					finish();
				}	        	
	        });
	        
	        Button btnBook = (Button) findViewById(R.id.btn_bookofdragon);
	        btnBook.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					finish();
					startActivity(new Intent("com.origidgames.nightfuryUI7"));
				}        	
	        });        
	        prepareMusic();
	    }
	  
	  private void prepareMusic() {		
		  /* prepare mediaplayer */
			PowerManager powerManager =
					(PowerManager)getSystemService(Context.POWER_SERVICE);
			wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			mediaPlayer = new MediaPlayer();
			try {
				AssetManager assetManager = getAssets();
				AssetFileDescriptor descriptor = assetManager.openFd(BGMFile);
				mediaPlayer.setDataSource(descriptor.getFileDescriptor(),descriptor.getStartOffset(),descriptor.getLength());
				mediaPlayer.prepare();
				mediaPlayer.setLooping(true);
				mediaPlayer.start();	
			} catch (IOException e) {
				mediaPlayer = null;
				e.printStackTrace();
			}
			
			/*Prepare sound button*/
			audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	        ToggleButton btt_sound = (ToggleButton) findViewById(R.id.btn_sound);
			if (PublicResource.getAudioPref(getBaseContext())) btt_sound.setChecked(false); else btt_sound.setChecked(true);
			btt_sound.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						audio.setStreamMute(AudioManager.STREAM_MUSIC, true);
						PublicResource.setAudioPref(getBaseContext(), false);
					}else {
						
						audio.setStreamMute(AudioManager.STREAM_MUSIC, false);
						PublicResource.setAudioPref(getBaseContext(), true);
					}
				}
				
			});
	  }
	  
	  protected void onResume() {
		  super.onResume();
		  if (mediaPlayer != null) {
				if (!mediaPlayer.isPlaying()) mediaPlayer.start();
				if (!PublicResource.getAudioPref(getBaseContext())) audio.setStreamMute(AudioManager.STREAM_MUSIC, true);
			}
		  wakeLock.acquire();
	  }
	  
	  protected void onPause() {
		  super.onPause();
		  if (!PublicResource.getAudioPref(getBaseContext())) audio.setStreamMute(AudioManager.STREAM_MUSIC, false);
			if (mediaPlayer != null) {
				mediaPlayer.pause();
				if (isFinishing()) {
					mediaPlayer.stop();
					mediaPlayer.release();
				}
			}
		  wakeLock.release();
	  }
}
