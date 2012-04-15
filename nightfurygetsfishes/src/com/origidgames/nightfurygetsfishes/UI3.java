package com.origidgames.nightfurygetsfishes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
		{450,220},{410,220},{370,220},{330,230},{290,190},{230,220},{190,200},{160,220},{110,230},{50,220}
	};
	private int currentPosition;
	private RelativeLayout.LayoutParams m_Params;
	private int question_checked[] = new int[QUESTION_NUMBER];
	private int answer_checked[] = new int[ANSWER_NUMBER];
	private Random m_random = new Random();
	private int stars = 0;
	private DBAdapter db;
	private WakeLock wakeLock;
	private MediaPlayer mediaPlayer;
	private static final String BGMFile = "bgm_question.mp3";
	private Cursor _question;
	private Button ans[] = new Button[5];
	private TextView question,countdown;
	private ImageView nightfury;
	private Boolean flag = true;
	private int answer_random[] = new int[5];
	private float density;
	private Boolean finish = false;
	private CountDownTimerWithPause countDown;
	private AudioManager audio;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_ui3);
		prepareMusic();
		prepareDataBase();
		prepareMenu();
		startCountDown();
		displayNewQuestion();
	
	}
	
	protected void onResume(){
		super.onResume();
		if (countDown.isPaused()) startCountDown();
		if (mediaPlayer != null) {
			if (!mediaPlayer.isPlaying()) mediaPlayer.start();
			if (!PublicResource.getAudioPref(getBaseContext())) audio.setStreamMute(AudioManager.STREAM_MUSIC, true);
		}
		wakeLock.acquire();
	}
	
	protected void onPause() {
		super.onPause();
		stopCountDown();
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
	
	protected void onDestroyed() {
		super.onDestroy();
		mediaPlayer.stop();
		mediaPlayer.release();
		if (countDown.isRunning()) stopCountDown();
	}
	
	private Cursor getCurrentQuestion() {
		return _question;
	}
	
	private void finishGame(boolean result) {
		stopCountDown();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage((result==true)?"You win":"Gameover! You lost")
    			.setPositiveButton("OK", new DialogInterface.OnClickListener() {						
					public void onClick(DialogInterface dialog, int which) {
						finish();							
					}
				});
    	AlertDialog alert = builder.create();
    	alert.show();
	}
	
	private void startCountDown() {
		countDown.create();

	}
	
	private void stopCountDown() {
		countDown.pause();

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
		} catch (IOException e) {
			mediaPlayer = null;
			e.printStackTrace();
		}
		
	}
	
	private void closeDataBase() {
		db.close();
	}
	
	private void prepareDataBase() {
		setUncheckedAll(question_checked);
		currentPosition = 0;
		 db = new DBAdapter(this);
        try {
        	String destPath = "/data/data/" + getPackageName() + "/databases/MyDB";
        	File f = new File(destPath);
        	if (!f.exists()) {
        		db.CopyDB(getBaseContext().getAssets().open("mydb"),new FileOutputStream(destPath));
        	}
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
       
        try {
			db.open();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	private void processAnswer(final int a) {
		flag=false;
		if (getAnswer()==answer_random[a]) {
			PublicResource.playSoundAnsRight();
			stars++; 
			currentPosition++;
			final Handler handler = new Handler();
			ans[a].setBackgroundResource(R.drawable.img_answer_right);
			handler.postDelayed(new Runnable() {
			  public void run() {
			    //Do something after 1s
					ans[a].setBackgroundResource(R.drawable.button_answer);
					if (stars==WIN) finishGame(true);
					else {
						flag=true;
						m_Params.setMargins(Math.round(NIGHTFURY[currentPosition][1]*density),Math.round(NIGHTFURY[currentPosition][0]*density), 0, 0);
						nightfury.setLayoutParams(m_Params);
						displayNewQuestion();
					}
			  }
			}, 1000);
		} else {
			if (stars>0) {
				stars--;
				currentPosition--;
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
					flag=true;
					m_Params.setMargins(Math.round(NIGHTFURY[currentPosition][1]*density),Math.round(NIGHTFURY[currentPosition][0]*density), 0, 0);
					nightfury.setLayoutParams(m_Params);
					displayNewQuestion();
			  }
			}, 1000);
		}

	}
	
	private void prepareMenu() {
		countDown = new CountDownTimerWithPause(TIME_LIMIT, 1000, true) {

		     public void onTick(long millisUntilFinished) {
		        countdown.setText(Long.toString(millisUntilFinished/1000));
		        if (millisUntilFinished <= 15000) PublicResource.playSoundClock();
		     }

		     public void onFinish() {
			     countdown.setText("0");
			     closeDataBase();
			     finish = true;
		    	 finishGame(false);
		     }
		  };
		m_Params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		density = getBaseContext().getResources().getDisplayMetrics().density;
		ImageView road = (ImageView) findViewById(R.id.road);
		road.startAnimation(PublicResource.FadeIn());
		ImageView clock = (ImageView) findViewById(R.id.clock);
		clock.startAnimation(PublicResource.FadeIn());
		clock.startAnimation(PublicResource.Rotate());
		countdown = (TextView) findViewById(R.id.count);
		countdown.startAnimation(PublicResource.FadeIn());
		nightfury = (ImageView) findViewById(R.id.nightfury);
		nightfury.setLayoutParams(m_Params);
		m_Params.setMargins(Math.round(NIGHTFURY[currentPosition][1]*density),Math.round(NIGHTFURY[currentPosition][0]*density), 0, 0);
		nightfury.startAnimation(PublicResource.FadeIn());
		question = (TextView) findViewById(R.id.question);
		question.startAnimation(PublicResource.InFromLeft());
		
		ans[1] = (Button) findViewById(R.id.ans1);
		ans[2] = (Button) findViewById(R.id.ans2);
		ans[3] = (Button) findViewById(R.id.ans3);
		ans[4] = (Button) findViewById(R.id.ans4);
		for (int i=1;i<=4;i++)
			ans[i].startAnimation(PublicResource.InFromBot());
		ans[1].setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(flag)processAnswer(1);
			}
			
		});
		ans[2].setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(flag)processAnswer(2);
			}
			
		});
		ans[3].setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(flag)processAnswer(3);
			}
			
		});
		ans[4].setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(flag)processAnswer(4);
			}
			
		});
	}
	
	private void displayNewQuestion() {
		int q;
		if (!finish) {
			while (question_checked[q=m_random.nextInt(QUESTION_NUMBER)]==1);
			setChecked(q);
			
			_question = null;
			try {
				_question = db.getQuestion(q+1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			question.setText(_question.getString(1));
			setUncheckedAll(answer_checked);
			for (int i=1;i<=4;i++) {
				while (answer_checked[q = m_random.nextInt(ANSWER_NUMBER)]==1);
				answer_random[i] = q + 1;
				ans[i].setText(_question.getString(q+2));	
				answer_checked[q] = 1;
			}
		}
	}
}
