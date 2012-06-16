/**************************************************************************************************************
 * Copyright (c) 2012 ORIGID GAMES STUDIO. 
 *************************************************************************************************************/

package com.origidgames.nightfurygetsfishes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UI2 extends Activity{
	  private WakeLock wakeLock;
	  
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.layout_ui2);
	    	PowerManager powerManager =
					(PowerManager)getSystemService(Context.POWER_SERVICE);
	        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
	        
	        Button btnBack = (Button) findViewById(R.id.btn_back);
	        btnBack.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					finish();
					startActivity(new Intent("om.origidgames.nightfuryUI1"));
				}	        	
	        });
	        
	        Button btnEasy = (Button) findViewById(R.id.btn_packet_easy);
	        btnEasy.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					finish();
					PublicResource.setGameMode(getBaseContext(), GameMode.EASY);
					startActivity(new Intent("om.origidgames.nightfuryIntro"));
				}	        	
	        });
	        
	        Button btnNormal = (Button) findViewById(R.id.btn_packet_normal);
	        btnNormal.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					finish();
					PublicResource.setGameMode(getBaseContext(), GameMode.NORMAL);
					startActivity(new Intent("om.origidgames.nightfuryIntro"));
				}	        	
	        });
	        
	        Button btnHard = (Button) findViewById(R.id.btn_packet_hard);
	        btnHard.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					finish();
					PublicResource.setGameMode(getBaseContext(), GameMode.HARD);
					startActivity(new Intent("om.origidgames.nightfuryIntro"));
				}	        	
	        });
	    }
	  protected void onResume() {
		  super.onResume();	 
		  wakeLock.acquire();
	  }
	  
	  protected void onPause() {
		  super.onPause();	
		  wakeLock.release();
	  }
}
