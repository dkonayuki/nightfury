/**************************************************************************************************************
 * Copyright (c) 2012 ORIGID GAMES STUDIO. 
 *************************************************************************************************************/

package com.origidgames.nightfurygetsfishes;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * This Activity class is used for implement UI4 layout. 
 *
 */
public class UI4 extends Activity{
	/**************************************
	 * Private class's constant 
	 *************************************/
	/** Fishes, Time TextView running CountdownTimer (in milliseconds). */
	private static final long lFishesTimeRun = 1500; 
	/** Step for elapsed time*/ 
	private static final float fElaspedTimeStep = 2.1f;
	/** Step for fishes */
	private static final int iFishesStep = 5;
	
	/***************************************
	 * Private UI4's views variable
	 ***************************************/
	private ImageView imgHighScore;
	private TextView txtFishes, txtTime;
	
	/*****************************************
	 * Private variable which is used for this activity only
	 * or common variable used between functions.  
	 ****************************************/
	
	/** How many fishes fury got ? */
	private int iFishes;
	/** Elapsed time */
	private float fElapsedTime;
	
	
	/*****************************************
	 * Define override functions 
	 * such as onCreate, onResume, etc.
	 *****************************************/
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ui4);
        
        /* Initiate UI's view variable */
        txtFishes = (TextView)findViewById(R.id.txtFishes);
        txtTime = (TextView)findViewById(R.id.txtTime);
        imgHighScore = (ImageView)findViewById(R.id.imgNewHighScore);
        
        /* Get values used for this Activity */
        Bundle bd = getIntent().getExtras();
        iFishes = bd.getInt(PublicResource.UI4.Fishes.toString());
        fElapsedTime   = (float) (UI3.TIME_LIMIT / 1000) - bd.getFloat(PublicResource.UI4.Time.toString());
        
        /* Initiate Views' first state  */
        imgHighScore.setVisibility(PublicResource.getNewHighscore(this) ? View.VISIBLE : View.INVISIBLE);
        txtTime.setText(String.format("%.1f", (float) (UI3.TIME_LIMIT / 1000)));
        txtFishes.setText("0"); 
        // Set Sound Button State
        ((ToggleButton) findViewById(R.id.btn_sound_4)).setChecked(PublicResource.getAudioPref(getBaseContext()));
    }

	@Override
	protected void onResume() {
		super.onResume();
		/* Animate Views by code */
		
		/* Set-up CountdownTimer for Fishes and Time,
		 * Do it only 2 seconds.
		 */
		
		// CountDownTimer for Fishes, it counts from 0 to got fishes
		new CountDownTimer(lFishesTimeRun, lFishesTimeRun * iFishesStep / iFishes) {

			@Override
			public void onFinish() {
				txtFishes.setText(Integer.toString(iFishes));
			}

			@Override
			public void onTick(long millisUntilFinished) {
				int iCurrentValue = Integer.parseInt(txtFishes.getText().toString());
				txtFishes.setText(Integer.toString(iCurrentValue + iFishesStep));
			}
			
		}.start();
		
		// CountDownTimer for Time, it counts from TIME_LIMIT to Remaining Time
		new CountDownTimer(lFishesTimeRun, (long) (lFishesTimeRun * fElaspedTimeStep / fElapsedTime)) {

			@Override
			public void onFinish() {
				txtTime.setText(String.format("%.1f s", (float) (UI3.TIME_LIMIT / 1000 - fElapsedTime)));
			} 

			@Override
			public void onTick(long millisUntilFinished) {
				float fCurrentValue  = Float.parseFloat(txtTime.getText().toString());
				txtTime.setText(String.format("%.1f", fCurrentValue - fElaspedTimeStep));
			}
			
		}.start();
		
	}
	
	
	/***************************************
	 * Define UI Views' events
	 ***************************************/
	
	/**
	 * Share user's name and score to fb, twitter, etc...
	 * @param btn Share Button
	 */
	protected void onShareClick(Button btn) {
		
	}
	
	/**
	 * 
	 * @param btn Retry Button
	 */
	protected void onRetryClick(Button btn) {
		
	}
	
	/**
	 * Back to Main Menu when clicked
	 * @param btn BackToMenu Button 
	 */
	protected void onBackToMenuClick(Button btn) {
		
	}
	
	/**
	 * Turn on/off background sound.
	 * @param btn Sound Button
	 */
	protected void onSoundClick(Button btn) {
		
	}
	
	/**
	 * Get txtInputName value and
	 * insert it into database.
	 * @param btn InputName Button
	 */
	protected void onInputNameClick(Button btn) {
		
	}
	
	/****************************************
	 * Define private function.
	 * All private functions' name must begin
	 * with [_] character 
	 **************************************/
}
