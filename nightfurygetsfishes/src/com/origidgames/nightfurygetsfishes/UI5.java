/**************************************************************************************************************
 * Copyright (c) 2012 ORIGID GAMES STUDIO. 
 *************************************************************************************************************/

package com.origidgames.nightfurygetsfishes;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * This Activity class is used for implement UI5 layout. 
 *
 */
public class UI5 extends Activity{
	/**************************************
	 * Private class's constant 
	 *************************************/
	/** Failed message duration's animation */
	private static final long lFailMessageDuration = 4000;
	/*****************************************
	 * Private variable which is used for this activity only
	 * or common variable used between functions.  
	 ****************************************/
	/** Message TextView */
	private TextView tvMsg;
	/** (For Failed Animation) Current Failed Message character */
	private int iCurrentFM = 0;
	/** Let's Ride Button */
	private Button btnLetride;
	
	/*****************************************
	 * Define override functions 
	 * such as onCreate, onResume, etc.
	 *****************************************/
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_ui5);
		
		/* Initiate UI's view variable */
		tvMsg = (TextView) findViewById(R.id.tvMsg_ui5);
		btnLetride = (Button) findViewById(R.id.btnLetride);
		
		/* Initiate Views' first state  */
		// Set Sound Button State
		((ToggleButton) findViewById(R.id.btn_sound_5)).setChecked(PublicResource.getAudioPref(getBaseContext()));
		// Format Message TextView
		tvMsg.setTypeface(PublicResource.getTrajanFont());
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		/* Do failed message animation */
		new CountDownTimer(lFailMessageDuration, lFailMessageDuration / getString(R.string.FailMessage).length()){

			@Override
			public void onFinish() {				
				tvMsg.setText(getString(R.string.FailMessage));
				btnLetride.setVisibility(View.VISIBLE);
			}

			@Override
			public void onTick(long millisUntilFinished) {
				tvMsg.setText(getString(R.string.FailMessage).subSequence(0, iCurrentFM++));
			}
			
		}.start();
	}
	  
	  
}
