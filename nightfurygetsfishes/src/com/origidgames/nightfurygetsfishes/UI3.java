package com.origidgames.nightfurygetsfishes;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class UI3 extends Activity {
	private static final int QUESTION_NUMBER = 35;
	private static final int TIME_LIMIT = 90000;
	private static final int ANSWER_NUMBER = 4;
	private static final int WIN = 10;
	private static final int[] NIGHTFURY[] = {
		{450,0},{410,0},{370,0},{330,10},{290,0},{230,0},{190,0},{160,0},{110,10},{50,0}
	};
	private static float NIGHTFURY_POSITION[][] = {
		{0.33f, 0.035f}, {0.42f, 0.08f}, {0.42f, 0.19f}, {0.22f, 0.27f}, {0.24f, 0.35f}, {0.39f, 0.45f}, 
		{0.09f, 0.51f}, {0.13f, 0.60f}, {0.44f, 0.68f}, {0.52f, 0.75f}, {0.36f, 0.82f}, {0.49f, 0.97f}
	};
	
	private static final int FURY_RUN_STEP = 5;
	
	private int currentPosition;
	private RelativeLayout.LayoutParams m_Params;
	private int question_checked[] = new int[QUESTION_NUMBER];
	private int answer_checked[] = new int[ANSWER_NUMBER];
	private Random m_random = new Random();
	private int stars = 1;
	private long time_remain;
	private WakeLock wakeLock;
	private MediaPlayer mediaPlayer;
	private static final String BGMFile = "bgm_question.mp3";
	private Cursor _question;
	private Button ans[] = new Button[5];
	private TextView question,countDownText;
	private ImageView nightfury;
	private Boolean wait1s = true;
	private int answer_random[] = new int[5];
	private float density;
	private Boolean finish = false;
	private CountDownTimer countDownTimer;
	private Boolean countDownPause = false;
	private AudioManager audio;
	private GameMode _gameMode;
	private float actualVolume,maxVolume,volume;
	private ImageView clock,pause;
	private Boolean isPaused;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_ui3);
		setGoal();
		prepareMusic();
		prepareMenu();
		displayNewQuestion();
	}
	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		_setUpStarAndGoal();
	}
	
	
	private void _setUpStarAndGoal(){
		RelativeLayout layout_road = (RelativeLayout)((FrameLayout)findViewById(R.id.road)).getChildAt(0);
		//Stars
		int roadWidth = layout_road.getWidth(), roadHeight = layout_road.getHeight();
		for(int i = 0; i < layout_road.getChildCount(); i++){
			View t = layout_road.getChildAt(i);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)t.getLayoutParams();
			NIGHTFURY_POSITION[i][0] *= roadWidth;
			NIGHTFURY_POSITION[i][1] *= roadHeight;
			params.setMargins((int)(NIGHTFURY_POSITION[i][0]), 
								(int)(NIGHTFURY_POSITION[i][1]), 
										0, 0);
			t.setLayoutParams(params);
		}

	}
	
	
	private void resumeCountDownTimer() {
		createCountDown(time_remain);
		countDownPause = false;
	}
	
	private void pauseCountDownTimer() {
		countDownTimer.cancel();
		countDownPause = true;
	}

	protected void onResume(){
		super.onResume();
		if (!isPaused)
			{
			if (isCountDownPaused()) {
				resumeCountDownTimer();
			}
			if (mediaPlayer != null) {
				if (!mediaPlayer.isPlaying()) mediaPlayer.start();
				if (!PublicResource.getAudioPref(getBaseContext())) audio.setStreamMute(AudioManager.STREAM_MUSIC, true);
			}
		}
		wakeLock.acquire();
	}
	
	protected void onPause() {
		super.onPause();
		if (!isPaused)
		{
			pauseCountDownTimer();
			if (!PublicResource.getAudioPref(getBaseContext())) audio.setStreamMute(AudioManager.STREAM_MUSIC, false);
			if (mediaPlayer != null) {
				mediaPlayer.pause();
				if (isFinishing()) {
					mediaPlayer.stop();
					mediaPlayer.release();
				}
			}
		}
		wakeLock.release();
	}
	
	protected void onDestroyed() {
		super.onDestroy();
		if (mediaPlayer.isPlaying()) mediaPlayer.stop();
		mediaPlayer.release();
		if (isCountDownRunning()) pauseCountDownTimer();
	}
	
	private GameMode getGameMode() {
		return _gameMode;
	}
	
	private void setGoal() {
		ImageView goal = (ImageView) findViewById(R.id.goal);
		_gameMode = PublicResource.getGameMode(this);
		switch(_gameMode) {
		case EASY:
			goal.setBackgroundResource(R.drawable.img_easy);
			break;
		case NORMAL:
			goal.setBackgroundResource(R.drawable.img_normal);
			break;
		case HARD:
			goal.setBackgroundResource(R.drawable.img_hard);
		}
	}
	
	private Cursor getCurrentQuestion() {
		return _question;
	}
	
	private Boolean isCountDownPaused() {
		return countDownPause;
	}
	
	private Boolean isCountDownRunning() {
		return (time_remain > 0);
	}
	
	private Boolean checkNewHighscore() {
		float time, timeleft;
		Boolean newHS = false;
		Cursor c = null;
		GameMode gm = PublicResource.getHighscore(getBaseContext());
        switch (gm) {
        case EASY: 
        	c = PublicResource.getDataBase().getAllEasy();
        	break;
        case NORMAL: 
        	c = PublicResource.getDataBase().getAllNormal();
        	break;
        case HARD:
        	c = PublicResource.getDataBase().getAllHard();
        }
        
        timeleft = (float) time_remain/1000;
        c.moveToFirst();
        for (int i = 1; i <= 5; i++)
        {
        	time = new Float(c.getString(2)).floatValue();
        	if (time > timeleft) { 
        		newHS = true;
        		break;	
        	}
        	c.moveToNext();
        }
        return newHS;
	}
	
	private void finishGame(boolean result) {
		countDownTimer.cancel();
		mediaPlayer.pause();
		clock.clearAnimation();
		ImageView game = (ImageView) findViewById(R.id.gameover);
		if (result) { 
			game.setBackgroundResource(R.drawable.img_win);
			
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {		
					finish();
				}
			}, 4000);
		}
		else {
			game.setBackgroundResource(R.drawable.img_gameover);
			PublicResource.playSoundLose();
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {		
					finish();
				}
			}, 4000);
		}
		game.startAnimation(PublicResource.FadeIn());
		
	}
	
	
	private void setChecked(int i) {
		question_checked[i]=1;
	}
	
	private void setUncheckedAll(int arr[]) {
		Arrays.fill(arr, 0);
	}
	
	private int getAnswer() {
		return (getCurrentQuestion().getInt(6));
	}
	
	private Boolean isWin() {
		return (stars == WIN);
	}
	
	private void _animateFury(int nowPos, int toPos){
		//Array's Position is reversed with real fury position
		nowPos = 11 - nowPos; toPos = 11 - toPos;
		final int dx = (int)((NIGHTFURY_POSITION[toPos][0] - NIGHTFURY_POSITION[nowPos][0])/FURY_RUN_STEP);
		final int dy = (int)((NIGHTFURY_POSITION[toPos][1] - NIGHTFURY_POSITION[nowPos][1])/FURY_RUN_STEP);
		//Only 1s for animate Fury, this is same with the time which shows the next Question
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)nightfury.getLayoutParams();
		params.setMargins((int)(NIGHTFURY_POSITION[nowPos][0]),
				 (int) (NIGHTFURY_POSITION[nowPos][1]), 
				 0,
				 0);
		nightfury.setLayoutParams(params);
		new CountDownTimer(1000+1000/FURY_RUN_STEP, 1000/FURY_RUN_STEP){

			@Override
			public void onFinish() {
				
			}

			@Override
			public void onTick(long millisUntilFinished) {
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)nightfury.getLayoutParams();
				params.setMargins(params.leftMargin + dx,
									params.topMargin + dy, 
									0, 
									0);
				nightfury.setLayoutParams(params);
				
			}
			
		}.start();
	}
	
	private void processAnswer(final int a) {
		if (wait1s&&!isPaused)
		{
			wait1s=false;
			if (getAnswer()==answer_random[a]) {
				PublicResource.playSoundAnsRight();
				stars++;
				currentPosition++;
				_animateFury(currentPosition - 1, currentPosition);
				final Handler handler = new Handler();
				ans[a].setBackgroundResource(R.drawable.img_answer_right);
				
				handler.postDelayed(new Runnable() {
				  public void run() {
				    //Do something after 1s
						ans[a].setBackgroundResource(R.drawable.button_answer);
						if (isWin()) finishGame(true);
						else {
							wait1s=true;
							/*
							m_Params.setMargins(Math.round(NIGHTFURY[currentPosition][1]*density),Math.round(NIGHTFURY[currentPosition][0]*density), 0, 0);
							nightfury.setLayoutParams(m_Params);
							*/
							displayNewQuestion();
						}
				  }
				}, 1000);
			} else {
				if (stars>0) {
					int down = 0;
					switch (getGameMode()) {
					case EASY: down = 0; break;
					case NORMAL: down = m_random.nextInt(1);  break;
					case HARD: down = 1;
					}
					if (down == 1) {
						stars--;
						currentPosition--;
						_animateFury(currentPosition + 1, currentPosition);
					}
				}
				PublicResource.playSoundAnsWrong();
				final Handler handler = new Handler();
				ans[a].setBackgroundResource(R.drawable.img_answer_wrong);
				for(int i=1;i<=4;i++) if (answer_random[i]==getAnswer())
					ans[i].setBackgroundResource(R.drawable.img_answer_right);
				handler.postDelayed(new Runnable() {
				  public void run() {
				    //Do something after 1s
						ans[a].setBackgroundResource(R.drawable.button_answer);
						for(int i=1;i<=4;i++) if (answer_random[i]==getAnswer())
							ans[i].setBackgroundResource(R.drawable.button_answer);
						wait1s=true;
						/*
						m_Params.setMargins(Math.round(NIGHTFURY[currentPosition][1]*density),Math.round(NIGHTFURY[currentPosition][0]*density), 0, 0);
						nightfury.setLayoutParams(m_Params);
						*/
						displayNewQuestion();
				  }
				}, 1000);
			}
		}
	}
	
	private void createCountDown(long time) {
		countDownTimer = new CountDownTimer(time, 1000) {

		     public void onTick(long millisUntilFinished) {
		        countDownText.setText(Long.toString(millisUntilFinished/1000));
		        time_remain = millisUntilFinished;
		        if (millisUntilFinished <= 15000) {
		        	PublicResource.playSoundClock();
		        	mediaPlayer.setVolume((float)volume*time_remain/15000, (float)volume*time_remain/15000);
		        }  
		     }

		     public void onFinish() {
			     countDownText.setText("0");			     
			     finish = true;
		    	 finishGame(false);
		     }
		  };
		  countDownTimer.start();
	}
	
	private void pauseGame() {
		mediaPlayer.pause();
		isPaused = true;
		PublicResource.playSoundPause();
		pauseCountDownTimer();
		PublicResource.pauseRotate();
		pause.setVisibility(View.VISIBLE);
	}
	
	private void resumeGame() {
		mediaPlayer.start();
		isPaused = false;
		PublicResource.playSoundPause();
		resumeCountDownTimer();
		PublicResource.resumeRotate();
		pause.setVisibility(View.GONE);
	}

	private void prepareMusic() {
		
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
		actualVolume = (float) audio.getStreamVolume(AudioManager.STREAM_MUSIC);
		maxVolume = (float) audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		volume = actualVolume / maxVolume;
	}
	
	private void prepareClock() {
		clock = (ImageView) findViewById(R.id.clock);
		clock.startAnimation(PublicResource.FadeIn());
		clock.startAnimation(PublicResource.Rotate());
		countDownText = (TextView) findViewById(R.id.count);
		countDownText.startAnimation(PublicResource.FadeIn());
	}
	
	private void prepareRoad() {
		FrameLayout road = (FrameLayout) findViewById(R.id.road);
		road.startAnimation(PublicResource.FadeIn());
	}
	
	private void prepareQuestion() {
		question = (TextView) findViewById(R.id.question);
		question.startAnimation(PublicResource.InFromLeft());
	}
	
	private void prepareAnswer() {

		ans[1] = (Button) findViewById(R.id.ans1);
		ans[2] = (Button) findViewById(R.id.ans2);
		ans[3] = (Button) findViewById(R.id.ans3);
		ans[4] = (Button) findViewById(R.id.ans4);
		for (int i=1;i<=4;i++)
			ans[i].startAnimation(PublicResource.InFromBot());
		ans[1].setOnClickListener(new OnClickListener() {

			public void onClick(View v) {			
				processAnswer(1);
			}
			
		});
		ans[2].setOnClickListener(new OnClickListener() {

			public void onClick(View v) {			
				processAnswer(2);
			}
			
		});
		ans[3].setOnClickListener(new OnClickListener() {

			public void onClick(View v) {	
				processAnswer(3);
			}
			
		});
		ans[4].setOnClickListener(new OnClickListener() {

			public void onClick(View v) {		
				processAnswer(4);
			}
			
		});
	}
	
	private void prepareButton() {
		//Button sound
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
		//Button pause
		ToggleButton btt_pause = (ToggleButton) findViewById(R.id.btn_pause);
		btt_pause.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) pauseGame();
				else resumeGame();
			}
			
		});
		pause = (ImageView) findViewById(R.id.pause);
	}
	
	private void prepareMenu() {
		isPaused = false;
		setUncheckedAll(question_checked);
		currentPosition = 0;
		createCountDown(TIME_LIMIT);
		m_Params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		density = getBaseContext().getResources().getDisplayMetrics().density;
		prepareRoad();
		prepareClock();
		nightfury = (ImageView) findViewById(R.id.nightfury);
		nightfury.setLayoutParams(m_Params);
		m_Params.setMargins(Math.round(NIGHTFURY[currentPosition][1]*density),Math.round(NIGHTFURY[currentPosition][0]*density), 0, 0);
		nightfury.startAnimation(PublicResource.FadeIn());
		prepareQuestion();
		prepareAnswer();
		prepareButton();
	}
	
	private void displayNewQuestion() {
		int q;
		if (!finish) {
			// find random question
			while (question_checked[q=m_random.nextInt(QUESTION_NUMBER)]==1);
			setChecked(q);		
			
			//get question
			_question = null;
			try {
				_question = PublicResource.getDataBase().getQuestion(q+1);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			question.setText(_question.getString(1));
			setUncheckedAll(answer_checked);
			
			//get answer text
			for (int i=1;i<=4;i++) {
				while (answer_checked[q = m_random.nextInt(ANSWER_NUMBER)]==1);
				answer_random[i] = q + 1;
				ans[i].setText(_question.getString(q+2));	
				answer_checked[q] = 1;
			}
		}
	}
}
