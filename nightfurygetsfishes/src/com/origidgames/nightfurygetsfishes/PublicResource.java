/**************************************************************************************************************
 * Copyright (c) 2012 ORIGID GAMES STUDIO. 
 *************************************************************************************************************/

package com.origidgames.nightfurygetsfishes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class PublicResource {
	/************************************************
	 * Extra information between Activity 
	 **********************************************/
	public enum UI4 {
		Fishes,
		Time,
		GameMode,
	}
	
	private static Animation 	anim_InFromLeft,
								anim_InFromRight,
								anim_InFromTop,
								anim_InFromBot,
								anim_OutToLeft,
								anim_OutToRight,
								anim_OutToTop,
								anim_OutToBot,
								anim_FadeIn,
								anim_FadeOut,
								anim_BotToTop,
								anim_Question,
								anim_Time;
	private static PausableRotate anim_Rotate;
	private static Bitmap bmpStar = null;
	private static SoundPool soundPool;
	private static int seAnswerCorrect,seAnswerWrong,seClock,sePageFlip,seLose,sePause,seWin;
	private static int seUpgrade50,seUpgradeChange,seUpgradeDouble,seUpgradeTime,seUpgradeSpeed;
	private static DBAdapter db;
	private static final String sPrefName = "Preferences";
	private static final String sAudio = "Audio";
	
	private static GameMode _gameMode;
	private static boolean _highscore = false;
	private static Typeface font;

    public static DBAdapter getDataBase() {
    	return db;
    }
    
    public static void closeDataBase() {
		db.close();
	}
	
	public static void LoadResource(Context ct) {
		anim_InFromLeft = AnimationUtils.loadAnimation(ct, R.anim.infromleft);
		anim_InFromRight = AnimationUtils.loadAnimation(ct, R.anim.infromright);
		anim_InFromTop= AnimationUtils.loadAnimation(ct, R.anim.infromtop);
		anim_InFromBot= AnimationUtils.loadAnimation(ct, R.anim.infrombot);
		anim_OutToLeft = AnimationUtils.loadAnimation(ct, R.anim.outtoleft);
		anim_OutToRight = AnimationUtils.loadAnimation(ct, R.anim.outtoright);
		anim_OutToBot = AnimationUtils.loadAnimation(ct, R.anim.outtobot);
		anim_OutToTop= AnimationUtils.loadAnimation(ct, R.anim.outtotop);
		anim_FadeIn = AnimationUtils.loadAnimation(ct, R.anim.fadein);
		anim_FadeOut = AnimationUtils.loadAnimation(ct, R.anim.fadeout);
		anim_Rotate = new PausableRotate(0,360,Animation.RELATIVE_TO_SELF,(float)0.5,
				Animation.RELATIVE_TO_SELF,(float)0.5);
		anim_Rotate.setDuration(90000);
		anim_BotToTop = AnimationUtils.loadAnimation(ct, R.anim.bottotop);
		anim_Question = AnimationUtils.loadAnimation(ct, R.anim.question);
		anim_Time = AnimationUtils.loadAnimation(ct, R.anim.time);
		bmpStar = BitmapFactory.decodeResource(ct.getResources(), R.drawable.img_checkpoint);
		soundPool = new SoundPool(20,AudioManager.STREAM_MUSIC,0);
		
		font = Typeface.createFromAsset(ct.getAssets(), "TrajanPro-Regular.otf");
		AssetFileDescriptor descriptor = null;
		try {
			AssetManager assetManager = ct.getAssets();
			descriptor = assetManager.openFd("answer_correct.mp3");
			seAnswerCorrect = soundPool.load(descriptor, 1);
			descriptor = assetManager.openFd("answer_wrong.mp3");
			seAnswerWrong = soundPool.load(descriptor, 1);
			descriptor = assetManager.openFd("clock.wav");
			seClock = soundPool.load(descriptor, 1);
			descriptor = assetManager.openFd("page_flip.mp3");
			sePageFlip = soundPool.load(descriptor, 1);
			descriptor = assetManager.openFd("lose.mp3");
			seLose = soundPool.load(descriptor, 1);
			descriptor = assetManager.openFd("pause.mp3");
			sePause = soundPool.load(descriptor, 1);
			descriptor = assetManager.openFd("upgrade_50.mp3");
			seUpgrade50 = soundPool.load(descriptor, 1);
			descriptor = assetManager.openFd("upgrade_change.mp3");
			seUpgradeChange = soundPool.load(descriptor, 1);
			descriptor = assetManager.openFd("upgrade_double.mp3");
			seUpgradeDouble = soundPool.load(descriptor, 1);
			descriptor = assetManager.openFd("upgrade_speedup.wav");
			seUpgradeSpeed = soundPool.load(descriptor, 1);
			descriptor = assetManager.openFd("upgrade_time.mp3");
			seUpgradeTime = soundPool.load(descriptor, 1);
			descriptor = assetManager.openFd("win.mp3");
			seWin = soundPool.load(descriptor, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db = new DBAdapter(ct);
        try {
        	String destPath = "/data/data/" + ct.getPackageName() + "/databases/";
        	File dir = new File(destPath);	
        	//File f = new File(destPath + "MyDB");
        	if (!dir.exists()) {
        		dir.mkdir();
        	}	
        	db.CopyDB(ct.getAssets().open("mydb"),new FileOutputStream(destPath + "MyDB"));
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
	
	public static Typeface getTrajanFont() { return font;}
	
	public static Animation Question() { return anim_Question;}
	public static Animation BotToTop() { return anim_BotToTop; }
	public static Animation Rotate() { return anim_Rotate; }
	public static void pauseRotate() {
		anim_Rotate.pause();
	}
	public static void resumeRotate() {
		anim_Rotate.resume();
	}
	public static Animation Time() { return anim_Time; }
	public static Animation FadeOut() { return anim_FadeOut;}
	public static Animation FadeIn() { return anim_FadeIn; }
	public static Animation InFromLeft() { return anim_InFromLeft; }
	public static Animation InFromBot() { return anim_InFromBot; }
	public static Bitmap Star(){return bmpStar;}
	
	public static void playSoundAnsRight() {
		soundPool.play(seAnswerCorrect, 1, 1, 0, 0, 1);
	}
	
	public static void playSoundAnsWrong() {
		soundPool.play(seAnswerWrong, 1, 1, 0, 0, 1);
	}
	
	public static void playSoundClock() {
		soundPool.play(seClock, 1, 1, 0, 0, 1);
	}
	
	public static void playSoundChange() {
		soundPool.play(sePageFlip, 1, 1, 0, 0, 1);
	}
	
	public static void playSoundLose() {
		soundPool.play(seLose, 1, 1, 0, 0, 1);
	}
	
	public static void playSoundPause() {
		soundPool.play(sePause, 1, 1, 0, 0, 1);
	}
	
	public static void playSoundWin() {
		soundPool.play(seWin, 1, 1, 0, 0, 1);
	}
	
	public static void playSoundUpgrade50() {
		soundPool.play(seUpgrade50, 1, 1, 0, 0, 1);
	}
	
	public static void playSoundUpgradeChange() {
		soundPool.play(seUpgradeChange, 1, 1, 0, 0, 1);
	}
	
	public static void playSoundUpgradeDouble() {
		soundPool.play(seUpgradeDouble, 1, 1, 0, 0, 1);
	}
	
	public static void playSoundUpgradeSpeedUp() {
		soundPool.play(seUpgradeSpeed, 1, 1, 0, 0, 1);
	}
	
	public static void playSoundUpgradeTime() {
		soundPool.play(seUpgradeTime, 1, 1, 0, 0, 1);
	}
	
	private static int caculateFishesNumber(int base, int upgrade) {
		int sum = base;
		for (int i = 1; i <= upgrade; i++) {
			sum = sum*105/100;
		}
		return sum;
	}
	
	public static int getFishesNumber(Context ct, GameMode gm) {
		SharedPreferences m_Pref = ct.getSharedPreferences(sPrefName, Context.MODE_PRIVATE);
		int upgrade = m_Pref.getInt("UpgradeFish", 0);
		switch (gm) {
		case NORMAL: return caculateFishesNumber(200,upgrade);
		case HARD: return caculateFishesNumber(400,upgrade);
		}
		//Default: EASY mode
	return caculateFishesNumber(100,upgrade); 
	}
	
	public static boolean getNewHighscore() {
		return _highscore;
	}
	
	public static void setNewHighscore(Context ct, boolean game) {
		_highscore = game;
	}
	
	public static int getUpgrade(Context ct, String key) {
		SharedPreferences m_Pref = ct.getSharedPreferences(sPrefName, Context.MODE_PRIVATE);
		return m_Pref.getInt(key, 0);
	}
	
	public static void setUpgrade(Context ct, String key, int n) {
		SharedPreferences m_Pref = ct.getSharedPreferences(sPrefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = m_Pref.edit();
		editor.putInt(key, n);
		editor.commit();
	}
	
	public static Boolean getAudioPref(Context ct) {
		SharedPreferences m_Pref = ct.getSharedPreferences(sPrefName, Context.MODE_PRIVATE);
		Boolean b = m_Pref.getBoolean(sAudio, true);
		return b;
	}
	
	public static void setAudioPref(Context ct,Boolean b) {
		SharedPreferences m_Pref = ct.getSharedPreferences(sPrefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = m_Pref.edit();
		if (b) editor.putBoolean(sAudio, true); else editor.putBoolean("Audio", false);
		editor.commit();
	}
	
	public static void setGameMode(Context ct, GameMode gm) {
		_gameMode = gm;
	}
	
	public static GameMode getGameMode(Context ct) {
		if (_gameMode!= null) return _gameMode;
		else return GameMode.EASY;
	}
	
	
}
