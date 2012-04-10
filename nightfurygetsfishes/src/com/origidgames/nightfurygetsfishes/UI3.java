package com.origidgames.nightfurygetsfishes;

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
import android.os.CountDownTimer;
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
import android.widget.TextView;
import android.widget.ToggleButton;

public class UI3 extends Activity {
	private static final int QUESTION_NUMBER = 25;
	private static final int TIME_LIMIT = 60000;
	private static final int WIN = 10;
	private int question_checked[] = new int[QUESTION_NUMBER];
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
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_ui3);
		PublicResource.LoadResource(getBaseContext());
		prepareMusic();
		prepareDataBase();
		prepareMenu();
		startCountDown();
		displayNewQuestion();
	
	}
	
	protected void onResume(){
		super.onResume();
		if (mediaPlayer != null) {
			if (PublicResource.getAudioPref(this.getBaseContext())) mediaPlayer.start();
		}
		wakeLock.acquire();
	}
	protected void onPause() {
		super.onPause();

		if (mediaPlayer != null) {
			mediaPlayer.pause();
			if (isFinishing()) {
				mediaPlayer.stop();
				mediaPlayer.release();
			}
		}
		wakeLock.release();
	}
	
	private Cursor getCurrentQuestion() {
		return _question;
	}
	
	private void finishGame(boolean result) {
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
		new CountDownTimer(TIME_LIMIT, 1000) {

		     public void onTick(long millisUntilFinished) {
		        countdown.setText(Long.toString(millisUntilFinished/1000));
		     }

		     public void onFinish() {
			     countdown.setText("0");
			     closeDataBase();
		    	 finishGame(false);
		     }
		  }.start();
	}
	
	private void prepareMusic() {
		PowerManager powerManager =
				(PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mediaPlayer = new MediaPlayer();
		try {
			AssetManager assetManager = getAssets();
			AssetFileDescriptor descriptor = assetManager.openFd(BGMFile);
			mediaPlayer.setDataSource(descriptor.getFileDescriptor(),descriptor.getStartOffset(),descriptor.getLength());
			mediaPlayer.prepare();
			mediaPlayer.setLooping(true);
			ToggleButton btt_sound = (ToggleButton) findViewById(R.id.btn_sound);
			if (PublicResource.getAudioPref(getBaseContext())) btt_sound.setChecked(false); else btt_sound.setChecked(true);
			btt_sound.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						mediaPlayer.pause(); 
						PublicResource.setAudioPref(getBaseContext(), false);
					}else {
						mediaPlayer.start();
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
		setUncheckedAll();
        try {
        	String destPath = "/data/data/" + getPackageName() + "/databases/MyDB";
        	//File f = new File(destPath);
        	//if (!f.exists()) {
        		CopyDB(getBaseContext().getAssets().open("mydb"),new FileOutputStream(destPath));
        	//}
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        db = new DBAdapter(this);
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
	
	private void setUncheckedAll() {
		Arrays.fill(question_checked, 0);
	}
	
	private int getAnswer() {
		return (getCurrentQuestion().getInt(6));
	}
	
	private void processAnswer(final int a) {
		flag=false;
		if (getAnswer()==a) {
			stars++; 
			final Handler handler = new Handler();
			ans[a].setBackgroundResource(R.drawable.img_answer_right);
			handler.postDelayed(new Runnable() {
			  public void run() {
			    //Do something after 1s
					ans[a].setBackgroundResource(R.drawable.button_answer);
					if (stars==WIN) finishGame(true);
					flag=true;
					displayNewQuestion();
			  }
			}, 1000);
		} else {
			stars--;
			final Handler handler = new Handler();
			ans[a].setBackgroundResource(R.drawable.img_answer_wrong);
			ans[getAnswer()].setBackgroundResource(R.drawable.img_answer_right);
			handler.postDelayed(new Runnable() {
			  public void run() {
			    //Do something after 1s
					ans[a].setBackgroundResource(R.drawable.button_answer);
					ans[getAnswer()].setBackgroundResource(R.drawable.button_answer);
					flag=true;
					displayNewQuestion();
			  }
			}, 1000);
		}

	}
	
	private void prepareMenu() {
		ImageView road = (ImageView) findViewById(R.id.road);
		road.startAnimation(PublicResource.FadeIn());
		ImageView clock = (ImageView) findViewById(R.id.clock);
		clock.startAnimation(PublicResource.FadeIn());
		clock.startAnimation(PublicResource.Rotate());
		countdown = (TextView) findViewById(R.id.count);
		countdown.startAnimation(PublicResource.FadeIn());
		nightfury = (ImageView) findViewById(R.id.nightfury);
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
		while (question_checked[q=m_random.nextInt(QUESTION_NUMBER)]==1);
		setChecked(q);
		
		_question = null;
		try {
			_question = db.getContact(q+1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		question.setText(_question.getString(1));
		for (int i=1;i<=4;i++)
		ans[i].setText(_question.getString(i+1));
	}

    private void CopyDB(InputStream inputStream,OutputStream outputStream) throws IOException {
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = inputStream.read(buffer))>0) {
    		outputStream.write(buffer,0,length);
    	}
    	inputStream.close();
    	outputStream.close();
    }

}
