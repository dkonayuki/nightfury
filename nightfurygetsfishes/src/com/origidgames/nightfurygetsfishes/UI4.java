/**************************************************************************************************************
 * Copyright (c) 2012 ORIGID GAMES STUDIO. 
 *************************************************************************************************************/

package com.origidgames.nightfurygetsfishes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
	private TextView txtFishes, txtTime, txtInputName;
	private Button btnInputName;
	
	/*****************************************
	 * Private variable which is used for this activity only
	 * or common variable used between functions.  
	 ****************************************/
	
	/** How many fishes fury got ? */
	private int iFishes;
	/** Elapsed time */
	private float fElapsedTime;
	/** Current game mode */
	private GameMode curGameMode;
	
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
        txtInputName = (TextView)findViewById(R.id.edtInputName);
        btnInputName = (Button)findViewById(R.id.btnInputName);
        
        /* Get values used for this Activity */
        Bundle bd = getIntent().getExtras();
        iFishes = bd.getInt(PublicResource.UI4.Fishes.toString());
        fElapsedTime   = (float) (UI3.TIME_LIMIT / 1000) - bd.getFloat(PublicResource.UI4.Time.toString());
        curGameMode = GameMode.values()[bd.getInt(PublicResource.UI4.GameMode.toString())];
        
        /* Initiate Views' first state  */
        imgHighScore.setVisibility(PublicResource.getNewHighscore() ? View.VISIBLE : View.INVISIBLE);
        txtTime.setText(String.format("%.1f", (float) (UI3.TIME_LIMIT / 1000)));
        txtFishes.setText("0"); 
        /* TODO: Add animation to txtInputName, btnInputName when there is a new high score */
        txtInputName.setEnabled(false);
        btnInputName.setEnabled(false);
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
				/* TODO: Add animation to txtInputName, btnInputName when there is a new high score */
				if (PublicResource.getNewHighscore()) {
					txtInputName.setEnabled(true);
					btnInputName.setEnabled(true);
				}
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
	 * @param btn Share Button
	 */
	public void onShareClick(View btn) {
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("text/plain");
		share.putExtra(Intent.EXTRA_TEXT, String.format("!! NEW HIGHSCORES !! I have gotten %s fishes during %s seconds !!",
														Integer.toString(iFishes), Float.toString(fElapsedTime)));
		startActivity(Intent.createChooser(share, "Share with"));
	}
	
	/**
	 * 
	 * @param btn Retry Button
	 */
	public void onRetryClick(View btn) {
		this.startActivity(new Intent("com.origidgames.nightfuryUI3"));
	}
	
	/**
	 * Back to Main Menu when clicked
	 * @param btn BackToMenu Button 
	 */
	public void onBackToMenuClick(View btn) {
		this.startActivity(new Intent("com.origidgames.nightfuryUI1"));
	}
	
	/**
	 * Turn on/off background sound.
	 * @param btn Sound Button
	 */
	public void onSoundClick(View btn) {
		
	}
	
	/**
	 * Get txtInputName value and
	 * insert it into database.
	 * @param btn InputName Button
	 */
	public void onInputNameClick(View btn)
	{
		if (txtInputName.getText() != "") {
			PublicResource.getDataBase().InsertHighscore(curGameMode, txtInputName.getText().toString(), fElapsedTime);
			/* TODO: Add animation to txtInputName, btnInputName when there is a new high score */
	        txtInputName.setEnabled(false);
	        btnInputName.setEnabled(false);
		} else {
			Toast.makeText(this, getString(R.string.InsertYourName), Toast.LENGTH_SHORT).show();
		}
	}
	
	/****************************************
	 * Define private function.
	 * All private functions' name must begin
	 * with [_] character 
	 **************************************/
}
